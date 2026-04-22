# Kotlin 코드 컨벤션

## 언어 기능

- **data class**: 도메인 모델/DTO 기본. 동작 없으면 항상 data class.
- **value class**: 단일 필드 + 타입 안정성이 필요한 경우. 예: `@JvmInline value class Affinity(val raw: Int)`.
- **sealed interface** > `sealed class`. 2개 이상 서브타입을 가진 합-타입에 사용.
- **enum**: 상태가 **닫혀있고** 부가 데이터가 없을 때만.
- **expression body**: 한 줄 함수는 `= expr` 형태.
- **trailing comma**: 항상 사용 (diff 최소화).

## 불변성

- 프로퍼티 `val` 우선. `var` 는 StateFlow 뒤 또는 mutableStateOf 뒤 내부 구현에만.
- 컬렉션은 `List`/`Set`/`Map` (read-only 인터페이스) 반환.
- 데이터 모델에 mutable collection 필드 금지.

## Null 안전

- `?.` 체이닝 허용, `!!` 는 주석 없이 금지.
- Result 표현: `kotlin.Result` 보다 도메인 전용 `sealed interface PetError` + `Either`-유사 래퍼 없이 `sealed interface` + `Success/Failure` 패턴 권장. 단, 단순 로딩 상태에는 `kotlin.Result` 허용.

## Coroutines

- `suspend` 함수는 이름에 `fetch`/`load`/`observe` 등 비동기성을 암시.
- Flow는 cold stream. Hot이 필요하면 `StateFlow` / `SharedFlow`.
- 스코프:
  - UI 단발 이벤트: `rememberCoroutineScope()`.
  - 앱 라이프타임: `ApplicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)` (DI 주입).
- 타이머/주기 작업: `flow { while(isActive) { emit(Unit); delay(...) } }` 패턴.

## 포맷

- 들여쓰기 4 spaces (Kotlin 공식).
- import wildcard 금지.
- 한 파일에 top-level 선언 5개 초과 시 분리 검토.

## 금지

- `companion object` 로 전역 정적 상수 남발 금지. 의존성 없으면 `object Foo`.
- `lateinit var` 지양. 초기화가 복잡하면 factory function으로.
- Java 상호운용이 아닌 곳에서 `@JvmStatic` 금지.
