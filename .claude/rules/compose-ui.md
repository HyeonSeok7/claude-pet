# Compose UI 규칙 (Desktop)

## Composable 시그니처

```kotlin
@Composable
fun PetCharacter(
    state: PetUiState,
    onEvent: (PetUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) { ... }
```

- `modifier` 는 **마지막 파라미터 직전** 또는 **마지막**에 위치. 기본값 `Modifier`.
- 이벤트 콜백은 `onXxx: () -> Unit` 또는 `onEvent: (Event) -> Unit`. 혼용 금지 (한 파일에서는 통일).
- State 는 불변 data class. 내부에서 `copy(...)` 로만 변화.

## State Hoisting

- **Stateful Composable** 은 최상위 1개 (`PetScreen`).
- 하위는 모두 **stateless**: `state` 파라미터 받고 `onEvent` 로 통지.

## 애니메이션

- 단순: `animateFloatAsState`, `animateDpAsState`.
- 복합 전이: `updateTransition` + `transition.animateFloat`.
- 캐릭터: `LottieAnimation` (io.github.alexzhirkevich/compottie) — 필요할 때만 도입.
- 프레임 기반 타이머: `LaunchedEffect(Unit) { while(true) { withFrameMillis { ... } } }`.

## Desktop 윈도우

- 메인 창: `Window(onCloseRequest, state = rememberWindowState(...), undecorated = true, transparent = true, alwaysOnTop = true)`.
- 드래그 가능 영역: `WindowDraggableArea { ... }`.
- 트레이: `Tray(icon, menu = { Item(...) })`.

## 테마

- `ui.theme.PetTheme { content() }` 로 앱 루트 래핑.
- 색상 토큰: `PetColors` object.
- 치수 토큰: `PetDimens` object.
- 폰트: `ui.theme.PetTypography`.

## 성능

- 리스트는 `LazyColumn`/`LazyRow`. 50개 이상 `Column` 금지.
- `remember { ... }` 내부에 비싼 계산 격리.
- 불필요한 recomposition 발생 시 `@Stable` / `@Immutable` 검토.

## 접근성

- 모든 클릭 가능한 요소에 `Modifier.semantics { contentDescription = "..." }` 또는 `Role`.
- 텍스트 색 대비 WCAG AA 이상.
