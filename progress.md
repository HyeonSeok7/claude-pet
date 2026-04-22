# Claude Pet (CMP) — Progress

> 단일 모듈 Compose Multiplatform Desktop. Windows / macOS 공용.
> PRD: [docs/prd.md](docs/prd.md)

## 전체 구조

```
cluade-pet/
├── .claude/              ← 프로젝트 특화 규칙/에이전트/훅
├── composeApp/
│   └── src/jvmMain/
│       ├── kotlin/com/myclaudepet/
│       │   ├── ui/       ← Composable, StateHolder, Theme
│       │   ├── domain/   ← Pure Kotlin: model / repository / usecase
│       │   ├── data/     ← SQLDelight, JNativeHook, DataStore
│       │   └── di/       ← Koin modules
│       ├── resources/
│       └── sqldelight/
├── docs/prd.md
└── progress.md
```

## 범례

- `[ ]` 미완료 · `[x]` 완료 · `[~]` 진행 중 · `[!]` 블록

---

## Phase 1 · 기반 ✓

### SPEC-001 · `.claude` 프로젝트 특화 설정
EARS: WHEN 프로젝트가 초기화되면 SHALL `.claude/` 에 CLAUDE.md / rules / agents / skills / hooks 가 존재한다.

- [x] `.claude/CLAUDE.md`
- [x] `.claude/rules/layer-dependency.md`
- [x] `.claude/rules/kotlin-conventions.md`
- [x] `.claude/rules/compose-ui.md`
- [x] `.claude/agents/compose-ui-expert.md`
- [x] `.claude/agents/kotlin-arch-reviewer.md`
- [x] `.claude/agents/sqldelight-schema.md`
- [x] `.claude/skills/rr/SKILL.md`
- [x] `.claude/hooks/post-edit-kotlin.sh` + `.claude/settings.json`

### SPEC-002 · PRD / 진행 문서 ✓
- [x] `docs/prd.md` (EARS 포맷)
- [x] `progress.md` (본 문서)

---

## Phase 2 · 빌드 체계

### SPEC-003 · Gradle 프로젝트
EARS: WHEN 개발자가 `./gradlew :composeApp:run` 을 실행하면 SHALL Compose Desktop 앱이 뜬다.

- [x] `settings.gradle.kts`
- [x] `gradle.properties`
- [x] `gradle/libs.versions.toml`
- [x] `build.gradle.kts` (root)
- [x] `composeApp/build.gradle.kts`
- [ ] Gradle Wrapper (`./gradlew`) — **확인 필요** (사용자가 `gradle wrapper` 1회 실행 필요)
- [ ] 빌드 검증: `./gradlew :composeApp:compileKotlinJvm` — **확인 필요** (로컬 미실행)

---

## Phase 3 · 도메인 ✓

### SPEC-004 · 모델 ✓
- [x] `domain/model/Pet.kt`
- [x] `domain/model/Satiation.kt` (value class)
- [x] `domain/model/Affinity.kt` (value class, level 파생)
- [x] `domain/model/PetMood.kt`
- [x] `domain/model/Dialogue.kt` + `DialogueTier.kt` + `PetPosition.kt`

### SPEC-005 · Repository 계약 ✓
- [x] `domain/repository/PetRepository.kt`
- [x] `domain/repository/InputEventSource.kt`

### SPEC-006 · UseCase ✓
- [x] `ObservePetUseCase`
- [x] `InteractWithPetUseCase`
- [x] `FeedOnKeystrokeUseCase`
- [x] `TickSatiationUseCase`
- [x] `MovePetUseCase`

---

## Phase 4 · 데이터 ✓

### SPEC-007 · SQLDelight 스키마 ✓
- [x] `src/commonMain/sqldelight/com/myclaudepet/db/PetState.sq` (KMP 기본 경로 — 초기 `jvmMain/sqldelight`에 뒀다가 `commonMain/sqldelight`로 이전해 generated 소스 해결)
- [x] 테이블 `pet_state` — 도메인 `Pet` data class와 이름 충돌 회피
- [x] `data/database/DriverFactory.kt` (항상 Schema.create 호출 — CREATE TABLE IF NOT EXISTS로 idempotent, 빈 DB 크래시 방지)
- [x] `data/database/PetDatabaseProvider.kt`
- [x] `data/database/AppPaths.kt` (OS별 기본 경로)

### SPEC-008 · Repository 구현 ✓
- [x] `data/repository/SqlDelightPetRepository.kt`

### SPEC-009 · 전역 입력 소스 ✓
- [x] `data/input/JNativeHookInputSource.kt`
- [x] 권한 없을 때 silent fallback

### SPEC-010 · 시간 제공자 ✓
- [x] `data/time/Clock.kt`

---

## Phase 5 · UI ✓

### SPEC-011 · 테마 / 토큰 ✓
- [x] `ui/theme/PetTheme.kt`
- [x] `ui/theme/PetColors.kt`
- [x] `ui/theme/PetDimens.kt`
- [x] `ui/theme/PetStrings.kt` (대사 팩 25개)

### SPEC-012 · 펫 Composable ✓
- [x] `ui/pet/PetCharacter.kt` (Canvas 드로잉 + 호흡 애니메이션)
- [x] `ui/pet/SpeechBubble.kt`
- [x] `ui/pet/StatsOverlay.kt`

### SPEC-013 · StateHolder ✓
- [x] `ui/state/PetUiState.kt`
- [x] `ui/state/PetUiEvent.kt`
- [x] `ui/state/PetStateHolder.kt`

### SPEC-014 · 메인 화면 + 윈도우 ✓
- [x] `ui/pet/PetScreen.kt`
- [x] Main.kt 에서 투명/무프레임/항상-위/드래그 윈도우

### SPEC-015 · 시스템 트레이 ✓
- [x] `ui/tray/PetTray.kt`
- [x] Show/Hide / Quit 메뉴

---

## Phase 6 · 조립 ✓

### SPEC-016 · DI ✓
- [x] `di/AppModule.kt`
- [x] `di/AppScope.kt`

### SPEC-017 · 진입점 ✓
- [x] `Main.kt`
- [x] 백그라운드 틱 런처 (PetStateHolder.launchTick)
- [x] 초기 Pet 로드는 runBlocking 1회

### SPEC-018 · 문서화 ✓
- [x] `README.md` (Mac/Win 실행, 접근성 권한 안내)

---

## Phase 7 · 배포 (v1 마무리)

### SPEC-019 · jpackage 빌드
- [x] macOS `./gradlew :composeApp:packageDmg` 성공 (`ClaudePet-1.0.0.dmg` 123 MB 생성)
- [x] 번들 JRE 에 `java.sql / java.instrument / jdk.unsupported` 모듈 포함 (SQLDelight 구동 필수)
- [x] 설치 후 `/Applications/ClaudePet.app` 바이너리 직접 실행 → JVM 정상 기동 확인
- [ ] Windows `.msi` — Windows 머신에서 빌드 필요 (별도 SPEC)

### SPEC-019b · 개발 환경 JDK 고정
- [x] Temurin/Corretto 21.0.10 의 macOS arm64 SIGTRAP 이슈 확인
- [x] JBR 21.0.10+7-b1163.110 수동 설치 (`~/Library/Java/JavaVirtualMachines/jbrsdk-21.0.10-...`)
- [x] `gradle.properties` 에 `org.gradle.java.home` 로 JBR 경로 고정 (로컬 개발 전용, 배포물 무관)
- [x] `kotlin-stdlib-jdk7/jdk8:1.9.24` 레거시 jar 트랜지티브 배제 (`configurations.all { exclude(...) }`)

---

## 알려진 제약 / 확인 필요

- [ ] Gradle 로컬 빌드 미검증 — 사용자 환경에서 `gradle wrapper` 실행 후 `./gradlew :composeApp:run` 으로 검증 필요
- [ ] macOS 첫 실행 시 "손쉬운 사용" 권한 부여 필요 (없으면 타이핑 카운터만 동작 안 함, 나머지 정상)
- [ ] Windows 첫 실행 시 SmartScreen 경고 가능 (jpackage 서명 미적용)

## 자체 리뷰에서 해결된 이슈

- [x] SQLDelight 이름 충돌 (`Pet` table → `pet_state` 로 변경)
- [x] `uiState?.let { rememberWindowState(...) }` → composition 루트에서 1회 생성
- [x] Koin 구버전 `KoinJavaComponent` → `GlobalContext.get()`
- [x] `data → ui` 역방향 의존 (`ScreenDefaults` 를 `ui/pet/` → `data/platform/` 으로 이동)
- [x] 빈 DB 파일 존재 시 `Schema.create` 미호출 크래시 → 항상 호출로 변경
- [x] 대사 리스트 `ui/theme/PetStrings.Dialogues` → `domain/model/DialogueCatalog` 로 이동
- [x] `PetCharacter.kt` / `StatsOverlay.kt` fully-qualified 타입 import 정리

## 추가 SPEC — 접근성 권한 UX

### SPEC-020 · 권한 실패 감지 + 다이얼로그 ✓
EARS: WHEN `InputEventSource.start()` 가 `PermissionDenied` 를 반환하면 SHALL UI가 모달 다이얼로그로 설정 열기 버튼을 노출한다.

- [x] `domain/repository/InputEventSource.StartResult` enum 추가
- [x] `JNativeHookInputSource` 가 `NativeHookException.DARWIN_AXAPI_DISABLED` 구분 반환
- [x] `domain/repository/PlatformBridge` 인터페이스 (UI → 플랫폼 동작 위임)
- [x] `data/platform/SystemSettings.kt` + `SystemPlatformBridge` 구현
- [x] `ui/pet/PermissionDialog.kt` (DialogWindow + 설정 열기)
- [x] `PetUiEvent.OpenAccessibilitySettings` / `DismissPermissionDialog` 추가
- [x] Main.kt 에 `PermissionDialog` 마운트
- [x] DI 모듈에 `PlatformBridge` 바인딩

## Out of Scope (PRD §7 참조)

- 클라우드 동기화
- 다국어(한국어 only)
- Linux
- 자동 업데이트
- 계정 시스템

## Completion Promise

PRD §8 의 모든 항목이 체크되면 v1 완료.

---

## Phase 8 · 찐따 펫 확장 (v2, PRD §9)

> 참조: cchh494/claude-pet (Swift, All Rights Reserved).
> 이미지·대사는 복제 금지. 메커닉만 참고, 오리지널 재구현.

### SPEC-021 · 이미지 로딩 인프라 + AI 프롬프트 ✓
EARS: WHEN `PetUiState.state` 가 10가지 상태 중 하나가 되면 SHALL 해당 PNG 로 렌더, 리소스 없을 때 Canvas fallback.

- [x] `composeApp/src/jvmMain/resources/pet/` 디렉토리 + `.gitkeep`
- [x] `ui/pet/PetSprite.kt` — 리소스 로더 (useResource + runCatching, null fallback)
- [x] `domain/model/PetAnimationState.kt` — 10가지 enum + `PetMood.toAnimationState()` 호환 매핑
- [x] `ui/pet/PetCharacter.kt` 를 스프라이트 우선 + Canvas fallback 구조로 교체
- [x] `docs/ai-sprite-prompts.md` — 10개 상태별 AI 프롬프트 + 공통 스타일 가이드
- [x] 빌드 검증: `./gradlew :composeApp:compileKotlinJvm` 성공 (deprecation warning 2건은 의도적 유지)
- [x] 실행 검증: `./gradlew :composeApp:run` 기동 성공, Canvas fallback 렌더 (PNG 미배치 상태)
- [ ] **사용자 액션 필요**: `docs/ai-sprite-prompts.md` 의 프롬프트로 PNG 10장 생성 후 `composeApp/src/jvmMain/resources/pet/` 에 배치

### SPEC-022-R · 기반 통합 + 상태 대사 + 수동/자동 리셋 ✓
EARS: WHEN 상태 전이가 일어나면 SHALL 해당 상태 대사를 자동 노출; WHEN 새 배포 빌드가 실행되거나 우클릭 초기화가 실행되면 SHALL level·satiation·keystrokes 를 리셋.

- [x] `PetState.sq` 에 `animation_state`, `install_id` 컬럼 추가 + `resetProgress`, `updateAnimationState`, `updateInstallId`, `selectInstallId` 쿼리
- [x] `DriverFactory` 에 `PRAGMA user_version` 기반 마이그레이션 (v1→v2: DROP + recreate)
- [x] `data/install/InstallId.kt` — 번들 리소스 `install_id.txt` 로더
- [x] `composeApp/build.gradle.kts` — `generateInstallId` 태스크, 배포 태스크에만 의존, `jvmMain/resources` 에 srcDir 추가
- [x] `SqlDelightPetRepository` — 부팅 시 install_id 불일치면 자동 `resetProgress` + `updateInstallId`
- [x] `domain/model/Pet.animationState` 필드 도입, DB 매핑 갱신
- [x] `PetAnimationState.dialogueTrigger` 매핑 (10개 상태 → 10개 트리거)
- [x] `DialogueTrigger` 확장: Idle, Smile, Boring, Jumping, Touch, Hungry, Fed, WorkingPrepare, Working, WorkingEnd
- [x] `DialogueCatalog` — Formal tier 상태별 찐따톤 대사 30여 개 추가
- [x] `PetStateHolder` — 상태 전이 감지 시 자동 대사 노출, `IDLE_SPEECH_ODDS = 3` 로 명확화, speech 타이머 race 해결 (이전 job cancel), `syncAnimationStateFromSatiation` 으로 포만도 ≤ 20 시 Hungry 전이
- [x] `ResetPetUseCase` + DI 등록
- [x] `PetUiEvent` 에 `RequestReset / ConfirmReset / CancelReset` + `PetUiState.resetConfirmVisible`
- [x] `ContextMenuArea` 로 펫 우클릭 시 "처음부터 시작…" 메뉴
- [x] `ResetDialog` — 확인 다이얼로그 (취소/초기화)
- [x] `Main.kt` 에 `ResetDialog` 마운트
- [x] `StatsOverlay` — "Lv.3  50/100" 진행도 수치 표기
- [x] `Affinity.pointsInCurrentLevel` 파생 프로퍼티
- [x] `PetCharacter(state, mood)` 로 시그니처 변경, `toAnimationState()` 호환 함수 제거
- [x] 빌드 검증: `./gradlew :composeApp:compileKotlinJvm` 성공
- [x] 실행 검증: `./gradlew :composeApp:run` JBR 로 기동, 크래시 없음

### SPEC-023 · 행동 루프 (자동이동 + 점프 + 터치 + Boring) ✓
EARS: WHILE idle 이면 SHALL 10~30초 랜덤 이동, 1/6 확률 점프, 5분+ 상호작용 없으면 Boring.

- [x] `PetStateHolder.launchBehaviorLoop()` — 10~30초 랜덤 간격 tryBehaviorTick
- [x] 이동: `ScreenDefaults.bounds()` 로 화면 clamp, `_targetPosition` SharedFlow 방출
- [x] `Main.kt` 가 `targetPosition` 구독 → `windowState.position` 반영
- [x] 점프: `Jumping` 상태 0.5초 + `PetCharacter.offset(y)` tween 애니메이션 (창은 고정, 캐릭터만 튐)
- [x] 터치: `onDoubleTap` → `DoubleClicked` 이벤트 → `Touch` 상태 0.5초 후 Default 복귀
- [x] Boring: `lastInteractionMillis` 추적, 5분 초과 + Default 상태일 때 `Boring` 전이
- [x] 컴파일/실행 검증 성공

### SPEC-024 · 밥 주기 + Fed 상태 ✓
EARS: WHEN 트레이 "밥 주기" 선택 시 SHALL satiation=Full, affinity+5, `Fed` 상태 3초 유지 후 Default.

- [x] `domain/usecase/FeedPetUseCase.kt` 신규
- [x] `PetUiEvent.Feed` 이벤트
- [x] `Main.kt` Tray 메뉴에 "🍚 밥 주기" 항목 추가
- [x] `PetStateHolder.onFeed()` — 3초 유지 후 Default 복귀
- [x] DI 에 `FeedPetUseCase` 등록, StateHolder 파라미터 추가
- [x] 포만도 ≤ 20 자동 Hungry 전이는 SPEC-022-R 에서 이미 처리

### SPEC-025 · 찐따톤 대사 풀 전 티어 확장 ✓
EARS: WHEN 상태 전이 시 SHALL 해당 tier 의 상태별 대사 풀에서 랜덤 발화; 티어 상승 시 자연 변화.

- [x] `DialogueCatalog.kt` 재작성: Formal/Friendly/Casual/Close/Intimate 5개 tier × 10개 상태 + 3개 이벤트 트리거 = 100+ 대사
- [x] Formal: 찐따톤 존댓말 / Friendly: 살짝 풀린 존댓말 / Casual: 반말 / Close: 친밀 반말 / Intimate: 깊은 애정
- [x] `pick()` fallback 유지 — 특정 tier 에 없는 조합은 전체 풀에서 랜덤

### SPEC-026 · macOS IDE 감지 + Working 상태 ✓
EARS: WHEN macOS 포그라운드 앱이 화이트리스트에 속하면 SHALL WorkingPrepare → Working; 이탈 시 WorkingEnd → Default.

- [x] `data/platform/ForegroundAppProbe.kt` — `osascript` System Events 로 현재 포그라운드 앱 이름
- [x] `PlatformBridge.foregroundAppName()` 메서드 추가, `SystemPlatformBridge` 에서 Probe 위임
- [x] `PetStateHolder.launchForegroundProbe()` — 5초 주기 폴링, 화이트리스트 일치 감지
- [x] 화이트리스트: IntelliJ IDEA, PyCharm, WebStorm, Android Studio, Code(VSCode), Cursor, Xcode, Terminal, iTerm/iTerm2, Sublime Text
- [x] 진입: Prepare 2초 유지 → Working. 이탈: WorkingEnd 2초 유지 → Default
- [x] Windows/Linux 는 `ForegroundAppProbe.current()` 가 null 반환 → 전이 없음 (no-op)

### SPEC-027 · v2 배포 검증 ✓
- [x] `./gradlew :composeApp:packageDmg --rerun-tasks` 성공
- [x] `/Applications/ClaudePet.app` 설치 + 바이너리 직접 실행 정상 기동 (PID 9432)
- [x] 번들 아이콘 `ClaudePet.icns` 적용 확인

### Out of Scope (Phase 8)

- Windows 에서 포그라운드 앱 감지 (추후 SPEC)
- 앱 서명/공증
- 프레임 애니메이션 (정적 PNG 10장만)
- 다국어

### Completion Promise (Phase 8)

PRD §9.4 의 모든 항목이 체크되면 v2 완료.
