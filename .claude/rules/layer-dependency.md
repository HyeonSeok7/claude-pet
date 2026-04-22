# 레이어 의존성 규칙

## 의존 방향 (엄격)

```
ui ──▶ domain ◀── data
```

- `ui` 는 `domain` 만 import 가능. `data` 직접 import 금지.
- `data` 는 `domain` 의 인터페이스/모델만 import 가능. `ui` 절대 금지.
- `domain` 은 아무 레이어도 import 하지 않는다. Pure Kotlin.

## 금지된 의존성 in `domain/`

- `androidx.compose.*`
- `org.jetbrains.compose.*`
- `com.github.kwhat.jnativehook.*`
- `app.cash.sqldelight.*`
- `java.awt.*`, `javax.swing.*`
- `io.insert-koin.*` (도메인은 DI 프레임워크를 모름)

`domain` 에 허용되는 것: `kotlin.*`, `kotlinx.coroutines.*`, `kotlinx.datetime.*`, `kotlinx.serialization.*`.

## Repository 패턴

- 인터페이스는 `domain/repository/` 에 선언.
- 구현은 `data/repository/` 에, 이름은 `{기술}{도메인}Repository` 형태.
  - 예: `SqlDelightPetRepository`, `DataStoreSettingsRepository`.
- DI 모듈에서 인터페이스 ↔ 구현 바인딩.

## UseCase 규칙

- UseCase 하나 = 단일 공개 `operator fun invoke(...)` 또는 `suspend operator fun invoke(...)`.
- UseCase 내부에서 다른 UseCase를 호출해도 OK. 순환 호출은 금지.
- UseCase가 1줄 위임만 하면 만들지 말고 Repository를 직접 쓴다 (과도한 추상화 방지).

## ViewModel/Presenter 대체

Compose Desktop은 Android ViewModel 없음. 대신:

- 각 화면 루트 Composable 옆에 `PetScreenStateHolder` (Plain Kotlin class) 두고 `rememberCoroutineScope()` 주입.
- StateHolder는 `StateFlow<UiState>` 와 `onEvent(UiEvent)` 를 노출.
- StateHolder는 `ui/` 에 존재하지만 오직 `domain` UseCase만 의존.
