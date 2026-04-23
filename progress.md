# Claude Pet — Progress

> 현재 작업 중인 한 기능 사이클의 SPEC 정의.
> 기능 완료 시 다음 사이클에 맞춰 재작성됨.
> 과거 사이클은 CHANGELOG / git log / PRD 참조.

## 목표

OS 무관하게 "Claude 사용 여부" 만으로 Working 상태가 동기화되도록, Windows 작업 감지를 macOS 와 동등한 경로로 완성하고 Claude CLI 감지를 단일 구현으로 통합한다.

관련 PRD: §10 (FR-19 개정, FR-22, FR-23, FR-24, FR-25, NFR-06~08)

## 원칙 / 제약

- 감지 단위는 "Claude Code CLI 프로세스 존재 여부". 설치 방식(전용 바이너리 / npm global / 쉘 래퍼) 에 독립적.
- OS 동등: Windows 와 macOS 는 같은 감지 경로 · 같은 전이 조건 · 같은 확신도.
- 우회 금지: 플랫폼별로 조건을 느슨하게 하거나 "Windows 는 CLI 만으로 Working" 같은 비대칭 로직 만들지 않는다.
- Windows 포그라운드 감지는 JNA (net.java.dev.jna) 도입 — 번들 크기 약 1.5 MB 증가 허용.
- `ProcessHandle.allProcesses()` 폴링은 5초 주기 이내. 유휴 CPU 영향 < 0.5%p.
- 감지 실패 시 조용히 false/null 폴백. 앱 정상 동작 유지.

## SPEC 분해

### SPEC-001 · Claude CLI 감지 ProcessHandle 전환 (macOS·Windows 공통)
EARS: WHEN CLI 감지가 호출되면 SHALL `ProcessHandle.allProcesses()` 를 순회하며 (a) `Info.command()` basename 이 `claude`/`claude.exe` 이거나 (b) `Info.commandLine()` 에 Claude Code 스크립트 경로 토큰(`@anthropic-ai/claude-code`, `/.bin/claude`, `\.bin\claude`, `/claude.cmd`, `\claude.ps1`) 이 포함되면 true 를 반환한다. POSIX `pgrep` 의존성은 제거한다.

- [x] `data/platform/ClaudeCliProbe.kt` 를 `ProcessHandle` 기반으로 재작성
- [x] 이미지 이름 정확 일치 판정 (basename, 대소문자 무시)
- [x] Command line 토큰 패턴 매칭 (contains 기반, 플랫폼 경로 구분자 포괄)
- [x] `isPosix` 분기 · `pgrep` ProcessBuilder 호출 제거
- [ ] macOS 회귀 검증: 기존 `pgrep -x claude` 가 잡던 시나리오가 그대로 동작 — **확인 필요** (실제 claude 프로세스로 수동 검증)
- [ ] macOS 신규 감지 검증: npm global 설치된 `claude` 도 `commandLine()` 토큰으로 감지 — **확인 필요**
- [x] 빌드 검증: `./gradlew :composeApp:compileKotlinJvm` 성공
- [x] 실행 검증: `./gradlew :composeApp:run` macOS 기동 (MainKt PID 감지). Working 전이 회귀는 UI 수동 확인 필요

### SPEC-002 · Windows 포그라운드 감지 (JNA 도입) — **롤백, Phase 10 이관**
EARS: WHEN Windows 에서 `ForegroundAppProbe.current()` 가 호출되면 SHALL `User32.GetForegroundWindow` → `GetWindowThreadProcessId` → `Kernel32.OpenProcess` + `QueryFullProcessImageNameW` 를 통해 포그라운드 프로세스의 실행 파일 basename 을 반환한다. 실패 시 null.

**결과**: 초안 구현(v1.1.0 ~ v1.1.2) 이 Windows jpackage 번들과 결합 시 "Failed to launch JVM" 원인으로 의심되어 JNA 의존성 전부 롤백(v1.1.2). 이후 조사로 빌드 자체는 정상(Windows Server 2025 runner 에서 정상 기동 재현), 실패는 친구 PC 의 Defender 차단으로 추정. JNA 재도입은 **Phase 10** 에서 jpackage 앱 서명 도입 또는 JDK 22+ FFM 전환 후 재검토.

- [x] ~~`gradle/libs.versions.toml` 에 `net.java.dev.jna:jna-platform` 5.18.1 추가~~ → 롤백
- [x] ~~`composeApp/build.gradle.kts` 에 `implementation(libs.jna.platform)` 추가~~ → 롤백
- [x] ~~`data/platform/ForegroundAppProbe.kt` 를 OS 분기 + JNA 기반 Windows 구현~~ → macOS osascript 만 유지로 원복
- [ ] ~~nativeDistributions 모듈 설정 검토~~ → Phase 10
- [ ] ~~Windows 실환경 실측~~ → **Phase 10** (코드 서명·대체 API 도입 후 재시도)

### SPEC-003 · 작업 화이트리스트 확장 (양 OS · AI 채팅 데스크탑 포함)
EARS: WHEN 포그라운드 앱 이름이 화이트리스트와 대조되면 SHALL 플랫폼별 바이너리명 + AI 채팅 데스크탑을 포괄한 최신 집합을 사용한다. 매칭은 대소문자 무시.

- [x] 화이트리스트 상수를 `PetStateHolder` 상단에서 `domain/platform/WorkAppWhitelist.kt` 로 분리 (Pure Kotlin, Platform enum 인자 기반 매칭)
- [x] macOS 집합: 기존 + **Windsurf, Zed** + **ChatGPT** 추가 (Fleet, Claude 는 기존에 이미 포함)
- [x] Windows 집합 신설 (IDE/에디터, 터미널, AI 채팅 데스크탑). ConEmu 계열은 prefix 매칭
- [x] "터미널 그룹" 판정 함수 `isTerminal(frontName, platform)` 분리
- [x] `PetStateHolder.launchForegroundProbe()` 가 `platform` lazy val 로 OS 감지 후 `WorkAppWhitelist.isWorkApp/isTerminal` 호출
- [x] 빌드 검증: `./gradlew :composeApp:compileKotlinJvm` 성공
- [x] 실행 검증: macOS `./gradlew :composeApp:run` 기동 성공 (MainKt PID 감지)
- [ ] **확인 필요**: macOS 수동 UI 검증 (IntelliJ / Terminal / Claude Desktop 포커스 시 Working 전이)
- [ ] **Phase 10 이관**: Windows Working 전이 실측 — SPEC-002 롤백으로 현재 사이클에서는 불가
- [ ] **Phase 10 이관**: Claude.exe / ChatGPT.exe 실제 바이너리명 검증

## Out of Scope

- Antigravity / Codex 데스크탑 바이너리명 확정 — 사용자가 실제 설치본 확인 후 별도 작업
- Claude 외 AI CLI (Codex, Gemini) 통합 감지
- Windows 서명 / Authenticode
- Linux 작업 감지

## Completion Promise

- [x] `ClaudeCliProbe` 가 `ProcessHandle` 기반으로 재구현 (macOS·Windows 공통 경로)
- [ ] **Phase 10 이관** — Windows `ForegroundAppProbe.current()` non-null 반환
- [ ] **Phase 10 이관** — Windows VS Code / PowerShell 포그라운드 Working 전이
- [ ] **Phase 10 이관** — Windows Claude CLI + Terminal 포커스 Working 전이
- [ ] **Phase 10 이관** — Claude Desktop / ChatGPT Desktop Working 전이 (Windows)
- [ ] macOS 기존 시나리오 회귀 없음 (수동 UI 검증 필요)
- [x] `./gradlew :composeApp:packageDmg`/`packageMsi` 번들 빌드 성공 (v1.1.3 / runner Windows Server 2025 에서 기동 재현 확인)

## Phase 9 사이클 종료

현재 사이클에서 **SPEC-001 (Claude CLI 감지 통합) 및 SPEC-003 (화이트리스트 확장)** 은 달성. **SPEC-002 (Windows 포그라운드 감지)** 는 JNA + jpackage + 서명 미적용 조합의 Defender 차단 이슈로 롤백, 다음 사이클(Phase 10)로 이관.

**다음 사이클 전 선행 해결 과제**:
- Authenticode 코드 서명 도입 또는 JDK 22+ FFM 로 JNA 대체
- Windows Defender heuristic 차단 회피 전략 확정
