# Claude Pet (CMP) — Product Requirements

## 1. 목적

원작자의 macOS 전용 Swift 프로젝트(cchh494/claude-pet)를 참고해,
**Windows / macOS 크로스 플랫폼** 데스크톱 펫을 Kotlin / Compose Multiplatform 으로
재구현한다. 게임 메커닉과 상태 분류 아이디어만 참고하고, 이미지·대사는 전부
오리지널 제작 (저작권 독립).

## 2. 플랫폼

- macOS 13+
- Windows 10/11
- 단일 Kotlin/Compose Multiplatform 코드베이스

## 3. 핵심 사용자 시나리오

1. 데스크톱 우측 하단에 작은 캐릭터가 떠 있다.
2. 좌클릭 → 캐릭터가 반응 대사를 말풍선으로 띄움.
3. 우클릭 → 메뉴(상태 보기 / 환경설정 / 종료).
4. 드래그 → 캐릭터를 다른 위치로 옮김. 위치는 저장됨.
5. 타이핑을 하면 화면 아래 카운터가 올라가고 포만감이 회복된다.
6. 아무것도 안 하면 포만감이 서서히 감소하고 캐릭터 표정이 바뀐다.
7. 상호작용이 쌓이면 호감도가 오르고, 5레벨마다 대사 톤이 변한다.

## 4. 기능 요구사항 (EARS)

| ID | EARS |
|---|---|
| FR-01 | WHEN 앱이 실행되면 SHALL 투명/무프레임/항상-위 데스크톱 윈도우를 우측 하단에 띄운다. |
| FR-02 | WHEN 사용자가 펫을 좌클릭하면 SHALL 랜덤 긍정 대사를 말풍선으로 2초간 표시한다. |
| FR-03 | WHEN 사용자가 펫을 우클릭하면 SHALL 컨텍스트 메뉴(상태/설정/종료)를 연다. |
| FR-04 | WHEN 사용자가 펫을 드래그하면 SHALL 위치를 갱신하고 DB에 저장한다. |
| FR-05 | WHEN 전역 키 입력이 감지되면 SHALL 타이핑 카운터를 1 증가시키고 포만감을 +0.05 회복(상한 100)한다. |
| FR-06 | WHILE 앱이 실행 중이면 SHALL 30초마다 포만감을 -1 감소(하한 0)시킨다. |
| FR-07 | WHEN 포만감이 0~20 구간이면 SHALL 캐릭터 애니메이션을 `sad`로 전환한다. |
| FR-08 | WHEN 포만감이 80~100 구간이면 SHALL `happy`로 전환한다. |
| FR-09 | WHEN 포만감이 회복되면 SHALL 호감도를 +1 증가(상한 99999)한다. |
| FR-10 | WHEN 호감도가 5의 배수 레벨을 돌파하면 SHALL 대사 팩을 다음 티어로 언락한다. |
| FR-11 | WHEN 시스템 트레이 아이콘이 클릭되면 SHALL 펫 윈도우 표시/숨김을 토글한다. |
| FR-12 | IF DB 파일이 없으면 SHALL 초기 상태(레벨 1, 포만감 100, 위치 화면 우하단)로 생성한다. |

## 5. 비기능 요구사항

- NFR-01: 시작 시간 < 2초 (on M1/i5급).
- NFR-02: 유휴 CPU 사용률 < 1%.
- NFR-03: 포만감/호감도는 앱 재시작 시 유지.
- NFR-04: 모든 리터럴 문자열은 `ui/theme/PetStrings.kt` 에 한국어 기본.
- NFR-05: 30 FPS 이상 렌더링 유지.

## 6. 스택 결정 (Rationale)

| 후보 | 장점 | 단점 | 결정 |
|---|---|---|---|
| Swift (원작) | Mac 네이티브 성능, ForceTouch 직접 | Windows 불가 | ✗ |
| Electron | 웹 생태계 | 메모리/번들 거대, 투명 윈도우 이슈 | ✗ |
| Tauri (Rust) | 가벼움 | 데스크톱 펫용 UI/애니메이션 라이브러리 빈약 | ✗ |
| Flutter Desktop | 애니메이션 강함 | Dart 생태계, 전역 키 후킹 불안정 | △ |
| **Compose Multiplatform** | Kotlin 코드 공유, 선언형, 애니메이션 튼튼, 투명 윈도우 지원 | 번들 크기 JVM 이슈 (jpackage로 해결) | **✓ 채택** |

## 7. Out of Scope (v1)

- 클라우드 동기화 (레벨/호감도 로컬 only).
- 다국어 (한국어만).
- 다중 모니터 전용 고급 배치.
- Linux 지원 (구조상 가능하나 테스트하지 않음).
- 자동 업데이트.
- 로그인/계정.

## 8. Completion Promise

v1 완료 조건:

- [ ] macOS에서 `./gradlew :composeApp:run` 실행 시 펫이 뜬다.
- [ ] Windows에서 동일 명령 실행 시 펫이 뜬다.
- [ ] FR-01 ~ FR-12 전부 동작.
- [ ] 앱 종료 → 재실행 시 위치/레벨/호감도가 복원된다.
- [ ] `./gradlew packageDistributionForCurrentOS` 로 각 OS 설치 파일 생성 가능.

---

## 9. v2 — 찐따 펫 확장 (Phase 8)

참조 원작: cchh494/claude-pet (Swift). **이미지/대사 텍스트는 저작권 이슈로 미복제**, 게임 메커닉/상태 분류/수치는 공개 아이디어로 재구현. 오리지널 이미지는 사용자가 AI 로 생성 후 배치. 오리지널 대사는 본 문서/코드에서 직접 창작.

### 9.1 상태 분류 (10가지)

| 키 | 파일명 | 상태 의미 |
|---|---|---|
| `default` | `idle_default.png` | 기본 |
| `smile` | `idle_smile.png` | 좌클릭/탭 반응 |
| `boring` | `idle_boring.png` | 오래 상호작용 없음 |
| `jumping` | `idle_jumping.png` | 점프 애니메이션 |
| `touch` | `idle_touch.png` | 강한 입력/반대방향 이동 |
| `working_prepare` | `idle_working_prepare.png` | IDE 포그라운드 진입 |
| `working` | `idle_working.png` | IDE 사용 중 |
| `working_end` | `idle_working_end.png` | IDE 종료/백그라운드 |
| `hungry` | `idle_hungry.png` | 포만도 ≤ 20 |
| `fed` | `idle_fed.png` | 먹이 받음 (3초) |

### 9.2 추가 기능 요구사항 (EARS)

| ID | EARS |
|---|---|
| FR-13 | WHEN 10가지 상태 중 하나로 전이하면 SHALL 해당 PNG 스프라이트로 렌더하고, 없으면 Canvas fallback 을 사용한다. |
| FR-14 | WHILE 앱이 idle 이면 SHALL 10~30초 간격으로 화면 좌표계에서 랜덤 위치로 이동(상태=default 유지, 이동 궤적은 보간)한다. |
| FR-15 | WHEN 이동 타이머가 5% 확률에 당첨되면 SHALL 상태=jumping 으로 0.5초 Y축 애니메이션을 실행한 뒤 복귀한다. |
| FR-16 | WHILE 앱이 실행 중이면 SHALL 분당 1 씩 포만도를 감소시킨다. |
| FR-17 | WHEN 포만도가 20 이하이면 SHALL 상태=hungry 로 전이하고 최소 10초마다 찐따톤 배고픔 대사를 말풍선으로 띄운다. |
| FR-18 | WHEN 트레이 메뉴 "🍚 밥 주기" 또는 펫 좌클릭(hungry 상태)이 발생하면 SHALL 포만도를 100 으로 올리고, 상태=fed 로 3초 유지 후 default 로 복귀, 호감도 +5 한다. |
| FR-19 | WHEN macOS 포그라운드 앱이 작업 화이트리스트(IDE/에디터/터미널) 에 속하면 SHALL working_prepare → working 전이를 발생시킨다. (Windows v2 범위 외) |
| FR-20 | WHEN 상태가 전이되면 SHALL 해당 상태에 대응하는 대사 풀에서 랜덤 1개를 SpeechBubble 로 2.5초간 노출한다. |
| FR-21 | WHEN 호감도 레벨이 5의 배수를 돌파하면 SHALL 대사 티어를 한 단계 언락하고 이후 선택 풀을 누적 확장한다. |

### 9.3 Out of Scope (Phase 8)

- Windows 에서의 작업 감지 (NSWorkspace 상응이 없음, 별도 SPEC)
- 타이핑 카운터 (기존 FR-05 로 이미 존재, Phase 8 재작업 없음)
- 앱 서명·공증
- Lottie/GIF 프레임 시퀀스 (정적 PNG)
- i18n (한국어만)

### 9.4 Completion Promise (Phase 8)

- [ ] 10가지 상태 스프라이트 로딩 (사용자 PNG 또는 Canvas fallback)
- [ ] 자동 이동/점프가 백그라운드에서 관측됨
- [ ] 포만도 자동감소 + 먹이주기 + 호감도 +5 가 영속됨
- [ ] 상태 전이마다 대사 1개 노출, 호감도 레벨에 따라 티어 확장
- [ ] IDE 포그라운드 감지로 working 전이 (macOS)
- [ ] `./gradlew :composeApp:packageDmg` 결과 .dmg 에서 전 기능 동작
