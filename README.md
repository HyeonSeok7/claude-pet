<div align="center">

# Claude Pet 🐣

**Windows / macOS 공용 데스크톱 펫** — Kotlin × Compose Multiplatform

> AI Claude 를 의인화한 작고 수줍은 반려 캐릭터. 화면 구석에서 움직이고, 점프하고, 배고프면 말을 건넵니다.

![platform](https://img.shields.io/badge/platform-macOS%20%7C%20Windows-lightgrey)
![kotlin](https://img.shields.io/badge/Kotlin-2.1-7F52FF?logo=kotlin)
![compose](https://img.shields.io/badge/Compose_Multiplatform-1.7-4285F4?logo=jetpackcompose)
![jdk](https://img.shields.io/badge/JDK-21-orange)
![license](https://img.shields.io/badge/license-MIT-green)

<img src="composeApp/src/jvmMain/resources/pet/idle_smile.png" width="160" alt="pet smile"/>
<img src="composeApp/src/jvmMain/resources/pet/idle_working.png" width="160" alt="pet working"/>
<img src="composeApp/src/jvmMain/resources/pet/idle_hungry.png" width="160" alt="pet hungry"/>

</div>

---

## ✨ TL;DR

- 🎯 **Compose Multiplatform Desktop** 으로 macOS/Windows 한 코드베이스에서 빌드되는 반려 펫
- 🧠 **100 % AI 바이브 코딩** (Claude Code). 한 줄도 직접 타이핑하지 않음 — 대신 **"AI 가 잘 일하도록" 하네스를 설계** 하는 데 시간을 씀
- 🏗️ **31 개 SPEC 누적** (SPEC-001 ~ SPEC-031), EARS 포맷 요구사항 → 세션 단위 분해 → 자가 검증 루프
- 🔧 실전 디버깅 스토리: **OpenJDK 21.0.10 C2 JIT SIGTRAP 추적**, jpackage 의 `java.sql` 모듈 누락, macOS osascript 포그라운드 감지 오매칭, dmg 파일 아이콘 캐시 지옥 등

---

## 📑 Table of Contents

1. [다운로드](#-다운로드)
2. [주요 기능](#-주요-기능)
3. [기술 스택과 선택 이유](#-기술-스택과-선택-이유)
4. [아키텍처](#️-아키텍처)
5. [AI 하네스 엔지니어링](#-ai-하네스-엔지니어링)
6. [개발 여정 — 실전 디버깅 스토리](#-개발-여정--실전-디버깅-스토리)
7. [빌드·실행](#-빌드실행)
8. [자동 릴리즈 (CI/CD)](#-자동-릴리즈-cicd)
9. [크레딧 & 라이선스](#-크레딧--라이선스)

---

## 📦 다운로드

[Releases](../../releases) 페이지 최신 태그에서 받아주세요.

| OS | 파일 | 설치 |
|---|---|---|
| **macOS** (Apple Silicon · Intel) | `ClaudePet-X.Y.Z.dmg` | 마운트 → `ClaudePet.app` 을 **Applications** 로 드래그 |
| **Windows** (10 / 11) | `ClaudePet-X.Y.Z.msi` | 더블클릭 → 설치 마법사 |

<details>
<summary><strong>macOS 보안 경고 해결</strong> (클릭)</summary>

Apple Developer ID 서명 미적용 상태라 Gatekeeper 경고가 뜰 수 있습니다:

```bash
xattr -dr com.apple.quarantine /Applications/ClaudePet.app
open /Applications/ClaudePet.app
```

첫 실행 시 **타이핑 카운터용 접근성 권한** 요청이 뜹니다. 앱 다이얼로그의 `완료 (재시작)` 버튼이 권한 부여 후 JVM 전체 재시작까지 원클릭 처리합니다.
</details>

---

## 🎮 주요 기능

| 카테고리 | 기능 |
|---|---|
| **상태 기반 캐릭터** | 10 가지 상태 스프라이트 + 걷기 2프레임 교차 (`default · smile · boring · jumping · touch · working_prepare · working · working_end · hungry · fed · walking`) |
| **자율 행동 루프** | 10 초 고정 간격으로 창 내부 좌우 왕복 걷기 (easeInOutCubic + sin bob), 1 / 6 확률 자발 점프, 5 분 무상호작용 시 `Boring` 전이 |
| **사용자 제스처** | 좌클릭 → `Smile` · 더블클릭 → `Jump` · 우클릭 → "처음부터 시작…" · 드래그 → 위치 영속 저장 |
| **포만감 사이클** | 30 초당 -1. ≤ 20 이면 `Hungry` 자동 전이 · 타이핑 또는 트레이 `🍚 밥 주기` 로 회복 |
| **호감도 레벨 티어** | 100 pt / Lv 누적. 5 단계 티어 (Formal · Friendly · Casual · Close · Intimate) — 레벨 오를수록 대사 톤이 존댓말 → 친근 반말 → 애정 표현으로 전이. 5 tier × 10 state + 이벤트 = **100 + 줄 오리지널 대사** |
| **Working 감지** | macOS `osascript` 포그라운드 앱 이름 확인 → VS Code · IntelliJ · Cursor · Xcode · Terminal · iTerm2 · Claude Desktop 포커스 시 `Working` 전이. `pgrep -x claude` 로 Claude Code CLI 세션도 인식 |
| **말풍선 UX** | 상태 전이 시 일회성 대사 (2.5 초) → 지속 상태 진입 시 "일하는 중…" 같은 라벨로 자동 교체 |
| **재설치 자동 초기화** | 배포 빌드마다 UUID `install_id.txt` 주입. 부팅 시 DB 값과 비교해 불일치 시 진행 데이터 리셋 (개발 빌드는 `claudepet.dev` 시스템 프로퍼티로 우회) |
| **자동 업데이트 알림** | 시작 시 GitHub Releases API 조회 → 새 버전 있으면 다이얼로그 + 브라우저 바로 열기 |

---

## 🧰 기술 스택과 선택 이유

| 계층 | 기술 | 선택 이유 (짧게) |
|---|---|---|
| 언어 | **Kotlin 2.1** | null 안전 · sealed interface · value class |
| UI | **Compose Multiplatform 1.7 (Desktop)** | `Window(undecorated, transparent, alwaysOnTop)` + `ContextMenuArea` 로 데스크톱 펫에 필요한 모든 윈도우 플래그 지원 |
| 런타임 | **JetBrains Runtime 21** | Temurin / Corretto 21.0.10 이 macOS arm64 + Skiko 조합에서 C2 JIT SIGTRAP 크래시 — JBR 21 이 AppKit / Skiko 패치를 선반영 |
| 동시성 | **kotlinx.coroutines** | `SupervisorJob + Dispatchers.Default` 앱 스코프 단일 주입, `StateFlow` / `SharedFlow` 단방향 상태 |
| DI | **Koin 4** | `startKoin` 1 회 · 생성자 주입 · 런타임 설정만으로 전환 편리 |
| 영속 | **SQLDelight 2** + SQLite JDBC | `.sq` 에서 타입 안전 쿼리 생성 · `PRAGMA user_version` 으로 직접 마이그레이션 제어 |
| 전역 키 | **JNativeHook 2.2** | Win / Mac 동시 지원 · macOS Accessibility 권한 실패를 `StartResult.PermissionDenied` 로 명시 처리 |
| 직렬화 | kotlinx.serialization | GitHub Releases API JSON 파싱 (의존 추가 없이 기존 사용 중) |
| 패키징 | **jpackage via `compose.desktop.nativeDistributions`** | 플랫폼별 `.dmg` / `.msi` / `.deb` — JRE 모듈 번들 포함 |
| CI | GitHub Actions (`macos-14` + `windows-latest`) | 맥 arm64 + 윈도우 x64 병렬 빌드 후 `softprops/action-gh-release` 로 Release 자동 첨부 |

---

## 🏗️ 아키텍처

**Clean + MVI-lite** 3 레이어. Compose Desktop 엔 Android ViewModel 이 없으므로 `PetStateHolder` (Plain Kotlin) 가 `StateFlow<UiState>` 를 담당.

```
com.myclaudepet
├── ui/        ← Composable, UiState, UiEvent, StateHolder       (Compose)
├── domain/    ← model / repository(interface) / usecase          (Pure Kotlin)
└── data/      ← SQLDelight · JNativeHook · platform probe · 업데이트 소스
```

- 의존 방향: `ui → domain ← data` — `.claude/rules/layer-dependency.md` 에 규칙 명시, 편집 훅에서 자동 검증
- `domain` 은 Compose / Android / JNativeHook / SQLDelight / Koin / AWT / Swing 전부 금지. `kotlin.*` · `kotlinx.coroutines.*` · `kotlinx.datetime.*` · `kotlinx.serialization.*` 만 허용
- UseCase 는 단일 `operator fun invoke(...)` — 한 줄 위임에 그치면 UseCase 만들지 않고 Repository 직접 호출 (과도한 추상화 방지)

### 상태 기계

펫의 전이를 한 그림으로:

```
 ┌────────── Default (기본) ──────────┐
 │   │ ↑                               │
 │ click                               │ idle 5 분+
 │   ↓                                 ↓
 │  Smile (1.5s) ──auto──→ Default    Boring ──click──→ Smile
 │                                     
 │ satiation ≤ 20                      
 │   ↓                                 
 │  Hungry ──feed──→ Fed (3s) ──auto──→ Default  
 │                                     
 │ 행동 루프 10s tick                  
 │   ↓                                 
 │  Walking (2s 왕복) / Jumping(0.5s) ──auto──→ Default
 │                                     
 │ macOS 포그라운드 IDE / Terminal / Claude
 │   ↓                                 
 │  WorkingPrepare (2s) → Working ──이탈──→ WorkingEnd (2s) → Default
 └─────────────────────────────────────┘
```

---

## 🤖 AI 하네스 엔지니어링

> "코드를 AI 에게 쓰게 하는 것" 과 "AI 가 쓸 만한 코드를 생산하게 만드는 것" 은 다릅니다.
> 이 프로젝트의 절반은 후자에 투자됐습니다.

### 🔩 `.claude/` 프로젝트 특화 하네스

```
.claude/
├── CLAUDE.md                    기술 스택·아키텍처·네이밍·금지사항 (AI 가 매 세션 로드)
├── rules/
│   ├── layer-dependency.md        ui→domain←data 의존 방향 강제
│   ├── kotlin-conventions.md      data class / value class / sealed / !! 금지 등
│   └── compose-ui.md              Composable 시그니처 · state hoisting · dp 토큰 사용
├── agents/
│   ├── compose-ui-expert.md       Compose UI 전담 (투명 윈도우, 애니메이션, 리컴포지션)
│   ├── kotlin-arch-reviewer.md    레이어 위반·네이밍 불일치·var 남용 자동 리뷰
│   └── sqldelight-schema.md       .sq 작성·마이그레이션 전담
├── skills/
│   └── rr/SKILL.md                /rr 커맨드 → progress.md 체크박스 자동 갱신
├── hooks/
│   └── post-edit-kotlin.sh        Kotlin 편집 후 자동 검증 트리거
└── settings.json                  퍼미션, 환경변수, rm 차단 훅
```

### 📄 문서 기반 반복 개발 (SPEC Loop)

`docs/prd.md` 와 `progress.md` 는 **AI 와 사람 사이 계약서**.

```
요구사항 도착
   │
   ├─ ① 소크라테스식 질문 → 빠진 맥락 채움
   │
   ├─ ② PRD 에 EARS 포맷으로 등록
   │    WHEN <이벤트>, IF <조건>, SHALL <동작>, WHILE <유지>
   │
   ├─ ③ SPEC-N 으로 세션 단위 분해
   │    각 SPEC 에 Completion Promise + Out of Scope 명시
   │
   ├─ ④ 구현 → 컴파일 검증 → 실행 검증 → stderr/exit 확인
   │
   └─ ⑤ progress.md 체크박스 업데이트 → 다음 SPEC
```

현재 **SPEC-001 ~ SPEC-031** 누적 (`progress.md` 전체 히스토리).

### 🧪 자가 검증 루프

AI 가 "컴파일 성공 = 동작 성공" 이라 착각하지 않도록 3 단계 검증:

| 단계 | 검증 방법 | 놓칠 수 있는 것 |
|---|---|---|
| 1. 컴파일 | `./gradlew compileKotlinJvm` | 의존성 결함, 타입 불일치 — OK |
| 2. 개발 실행 | `./gradlew run` 백그라운드 → `pgrep` 생존 확인 + stderr tail | 런타임 초기화 실패, JIT 크래시, `NoClassDefFoundError` |
| 3. 배포 실행 | `./gradlew packageDmg` → 실제 `.app` 바이너리 직접 실행 | jpackage 모듈 누락 (예: `java.sql`), 번들 리소스 경로 문제 |

이 3 단계가 없었으면 `java.sql` 누락 (번들 JRE 에만 있음, 개발 JVM 엔 있음) 같은 버그가 출시 후 터졌을 것.

### 🛠️ 서브에이전트 & 훅

- **Explore** 에이전트: 큰 리팩토링 전 전체 코드베이스 조사 병렬 실행 → 메인 컨텍스트 절약
- **Plan** 에이전트: 다중 파일 리팩토링 전략 설계
- **PostEdit Kotlin 훅**: `.kt` 편집 시 자동 관련 검증
- **`rm` 차단 훅**: 실수 삭제 방지 — 대신 `~/.Trash/` 이동
- **메모리 시스템**: `~/.claude/projects/.../memory/` 에 취향·피드백 누적 → 모든 세션 자동 로드

---

## 🔬 개발 여정 — 실전 디버깅 스토리

실제로 겪은 이슈 중 포트폴리오로 공유할 만한 것들.

### 🐛 Case #1 — Temurin 21.0.10 JIT SIGTRAP (exit 133)

**증상**: `./gradlew run` → Gradle 이 `exit value 133` 만 뱉고 앱이 뜨지 않음. JVM 이 스택트레이스도 남기지 않음.

**추적**:
1. `./gradlew --info` 로 JVM 명령줄 추출 후 직접 실행 → `Trace/BPT trap: 5` 확인 (SIGTRAP)
2. `-Xint` (JIT 비활성) 로 실행 → JVM 프로세스 생존 → **JIT 자체의 버그로 확정**
3. Adoptium 공식 API 로 Temurin 21 최신 = 21.0.10. 같은 버전 AWS Corretto 도 동일 현상 → **OpenJDK 21.0.10 업스트림 JIT 버그**
4. **JetBrains Runtime 21.0.10 (AppKit / Skiko 패치 선반영 빌드)** 로 전환 → 정상 동작

### 🐛 Case #2 — jpackage 번들 JRE 의 `java.sql` 누락

**증상**: 로컬 `run` 은 정상인데 `.dmg` 로 설치한 앱이 즉시 종료. stderr 에 `NoClassDefFoundError: java/sql/DriverManager`.

**원인**: Compose Desktop `createRuntimeImage` 가 `jlink` 로 최소 JRE 를 만드는데 SQLDelight / sqlite-jdbc 가 JPMS 모듈 선언이 없어 `java.sql` 의존을 감지하지 못함.

**해결**: `./gradlew :composeApp:suggestRuntimeModules` 로 plugin 이 추천한 값을 그대로 `nativeDistributions.modules(...)` 에 반영.

```kotlin
modules("java.instrument", "java.sql", "jdk.unsupported")
```

### 🐛 Case #3 — macOS 포그라운드 앱 감지 오매칭

**증상**: IntelliJ 포커스해도 펫이 Working 전이 안 함.

**원인**: `osascript ... name of first application process` 이 `"idea"` (소문자 · 축약) 를 반환하는데, 화이트리스트엔 `"IntelliJ IDEA"` 로 넣어둠. `front.contains("IntelliJ IDEA", ignoreCase = true) == false`.

**해결**: 실제 반환값 기준으로 화이트리스트 재작성 (`idea`, `pycharm`, `Code`, `Cursor`, …), 또 "Claude" 매칭이 "ClaudePet" 자기 자신까지 매칭하는 문제는 **자기 자신 스킵 조건** 으로 해결.

### 🐛 Case #4 — Claude CLI 상시 실행으로 Working 영구 고정

**증상**: Claude Code CLI 를 상시 돌리는 사용자는 `pgrep -x claude` 가 항상 true → `isWorkContext = true` → **다른 상태(걷기·점프·Boring) 전이 차단**.

**해결**: `isWorkContext = isWorkApp || (isClaudeCli && isFrontTerminal)` 로 **CLI 감지를 Terminal 포커스와 AND 결합**. Chrome 등 다른 앱 볼 때는 자연스럽게 Default 복귀하여 걷기/점프 행동이 정상 노출.

### 🐛 Case #5 — `kotlin-stdlib-jdk7/jdk8:1.9.24` 트랜지티브 충돌

**증상**: Koin `startKoin` 에서 `NoSuchMethodError: kotlin.text.HexExtensionsKt.getBYTE_TO_LOWER_CASE_HEX_DIGITS()`.

**원인**: Koin 4 / Compose 등이 트랜지티브로 1.9.24 레거시 jar 를 끌어옴. Kotlin 2.0+ 에서 stdlib 에 merge 된 이 jar 가 **`kotlin-stdlib:2.1.0` 을 그림자** 처리.

**해결**: `configurations.all { exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7") ... }`. 한 줄.

---

## 🧑‍💻 빌드·실행

### 사전 준비
- **JDK 21** (JetBrains Runtime 21 권장: https://github.com/JetBrains/JetBrainsRuntime/releases)
- 로컬 개발용 `gradle.properties` 의 `org.gradle.java.home` 을 본인 JBR 경로로 지정 (이미 템플릿 있음)

### 실행
```bash
./gradlew :composeApp:run
```

### 네이티브 설치 파일 빌드
```bash
./gradlew :composeApp:packageDmg      # macOS (.dmg)
./gradlew :composeApp:packageMsi      # Windows (.msi, Windows 에서 실행)
```
산출물은 프로젝트 루트 `dist/` 에 자동 복사 + `.dmg` 파일에 앱 아이콘 자동 주입 (`fileicon` Gradle 태스크).

---

## 🚀 자동 릴리즈 (CI/CD)

`.github/workflows/release.yml` 이 **tag push** 를 감지해 자동으로:

```
tag push (vX.Y.Z)
    │
    ├─ macos-14 runner  → packageDmg    → artifact: macos-dmg
    ├─ windows-latest   → packageMsi    → artifact: windows-msi
    │
    └─ ubuntu-latest    → download artifacts → softprops/action-gh-release
                          → GitHub Releases 에 자동 첨부
```

새 버전 배포는 3 줄이면 끝:

```bash
# gradle.properties 의 projectVersion 을 bump
git commit -am "chore: bump v1.0.1"
git tag v1.0.1
git push && git push --tags
```

약 5 ~ 10 분 후 [Releases](../../releases) 페이지에 `.dmg` · `.msi` 가 붙습니다.

---

## 🗺️ 로드맵

- [x] v1 기본 (캐릭터 렌더, 대사, 레벨, 포만감)
- [x] v2 Phase 8 — 걷기·점프·터치·Working 감지·자동 업데이트
- [ ] Apple Developer ID 공증 (xattr 수동 해제 제거)
- [ ] Windows 에서 포그라운드 앱 감지 (현재 macOS 전용)
- [ ] 다중 모니터 지원 개선
- [ ] i18n (한국어 → 영어)
- [ ] DMG 볼륨 창 배경 커스텀 (jpackage 제약 회피)

---

## 🙏 크레딧 & 라이선스

- **원작 (영감)**: [cchh494/claude-pet](https://github.com/cchh494/claude-pet) — Swift / macOS, All Rights Reserved.
  게임 메커닉 / 상태 분류 아이디어만 참고. **이미지와 대사는 전부 이 프로젝트에서 오리지널 제작**.
- **이미지 생성**: Google Gemini (픽셀 아트 프롬프트) + remove.bg (배경 제거)
- **코딩**: 100 % [Claude Code](https://claude.com/claude-code) (Anthropic)
- **라이선스**: MIT (코드 · 번들 PNG 스프라이트 모두 포함)

---

<div align="center">
  <sub>🧵 Crafted by AI-harness engineering · Built with love for tiny desktop companions</sub>
</div>
