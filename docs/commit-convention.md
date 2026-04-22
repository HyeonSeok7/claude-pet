# 커밋 컨벤션

Claude Pet 저장소는 [Conventional Commits 1.0](https://www.conventionalcommits.org/) 을
따릅니다. 깃 히스토리를 읽기 쉽게 만들고, 자동 릴리즈 노트(GitHub Actions 의
`generate_release_notes: true`) 가 의미 있는 항목을 뽑아내도록 하기 위함입니다.

---

## 1. 포맷

```
<type>(<scope>): <subject>

<body>

<footer>
```

- **한 줄 요약** 만으로 충분한 경우 `body / footer` 는 생략.
- 제목은 72자 이내, 명령형 현재시제 (`add`, `fix`, `refactor`). 마침표 없음.
- 한국어·영어 혼용 허용. 팀/본인 편한 쪽으로 일관되게.

---

## 2. Type

| type | 용도 |
|---|---|
| **feat** | 사용자가 체감하는 새 기능 추가 |
| **fix** | 버그 수정 |
| **refactor** | 동작 변경 없는 리팩토링·구조 변경 |
| **perf** | 측정 가능한 성능 개선 |
| **docs** | 문서만 변경 (README / PRD / progress 등) |
| **style** | 포맷·공백·세미콜론 등 비의미 변경 |
| **test** | 테스트 추가·수정 |
| **build** | Gradle / jpackage / dependencies / icons — 빌드 산출물에 영향 |
| **ci** | GitHub Actions, 훅, 릴리즈 파이프라인 설정 |
| **chore** | 그 외 잡무 (버전 bump, 설정 파일, gitignore 등) |
| **revert** | 이전 커밋 되돌림 |

모호하면 **feat(새로 더해짐) vs fix(기존 깨진 것 고침)** 을 기준으로 먼저 고르고, 둘 다 아니면 **refactor / build / chore** 순으로.

---

## 3. Scope

이 프로젝트의 모듈·영역 단위. 커밋이 한 scope 에 몰리면 지정, 여러 scope 에 걸치면 생략.

| scope | 대상 |
|---|---|
| `ui` | Composable, StateHolder, Theme |
| `domain` | model / repository interface / usecase |
| `data` | SQLDelight, JNativeHook, platform probe |
| `di` | Koin 모듈 |
| `state` | PetStateHolder, PetUiState, PetUiEvent |
| `db` | SQLDelight `.sq`, 마이그레이션 |
| `build` | Gradle 스크립트, Gradle properties |
| `ci` | `.github/workflows`, 릴리즈 자동화 |
| `docs` | README, PRD, progress, 컨벤션 문서 |
| `scripts` | 로컬 배포 스크립트 |
| `hooks` | `.claude/hooks/*` |
| `icons` | 아이콘 리소스 (app-icon.icns 등) |
| `release` | 배포 파이프라인 전반 (여러 영역 동시 변경) |

---

## 4. Subject — 한 줄 요약

- 무엇을 했는지 **명령형** 으로 씀 (“added” X, **“add”** O).
- 왜는 body 에 (필요하면).
- 제목에 `#이슈번호` 넣지 말고 footer 에.

좋은 예:
```
feat(ui): add reset confirmation dialog triggered by right-click
fix(state): prevent Working state from sticking when Claude CLI runs
refactor(state): group private cache vars with intent comments
build(icons): regenerate 10-resolution icns from 1024x1024 source
```

피할 것:
```
update code                   ← 구체성 0
fixed a bug                   ← 과거형 + 뭐 고쳤는지 모름
PetCharacter.kt 수정          ← 파일명만
```

---

## 5. Body (선택)

- **왜 이 변경이 필요한가** 가 명확하지 않은 경우에만 작성
- 한 줄 72자 내외 wrap
- What 은 코드로 명백하니 Why 에 집중
- 여러 관점이 섞이면 커밋 자체를 쪼개는 것 검토

예시:
```
fix(state): resolve SIGTRAP crash on macOS arm64

Temurin 21.0.10 의 C2 JIT 이 Compose Desktop 실행 초기에 SIGTRAP 으로
JVM 을 죽였음 (exit value 133). `-Xint` 에서 정상 동작 → JIT 버그 확정.
로컬 개발은 JBR 21 을 가리키도록 ~/.gradle/gradle.properties 로 이식.

배포 빌드는 CI 의 Zulu 21 로 회피.
```

---

## 6. Footer (선택)

- `Closes #12`, `Refs #7`
- `BREAKING CHANGE: ...` — API·DB·설정 호환성 깨짐
- **`Co-Authored-By` 절대 사용 금지** (프로젝트 글로벌 규칙)

---

## 7. Breaking Change 표기

두 가지 방식 중 택일, 일관되게:

**① `!` 표기 (권장, 짧음)**
```
feat(db)!: migrate pet_state schema to v3

BREAKING CHANGE: animation_state 컬럼이 TEXT → INTEGER 로 변경됨.
기존 DB 는 install_id 불일치 경로로 자동 리셋되어 호환 처리됨.
```

**② footer 만**
```
refactor(api): rename PetRepository.updateAnimationState → setAnimationState

BREAKING CHANGE: 호출자 전부 교체 필요.
```

---

## 8. 이 저장소 실제 예시

좋은 커밋 (이 프로젝트에서 실제 있었을 법한):
```
feat(state): animate walk with 2-frame sprite cycle and easeInOutCubic offset
fix(ci): strip local org.gradle.java.home from gradle.properties before build
build(release): auto-collect .dmg/.msi into project-root dist/ via Gradle task
ci(hooks): block system-wide destructive commands with recursive script scan
docs(readme): add AI harness engineering section and debugging case studies
chore(release): bump to v1.0.1
```

---

## 9. 체크리스트 (커밋 전)

- [ ] 제목 72자 이내 + 명령형 + 마침표 없음
- [ ] `type` 선택이 정확한가 (feat/fix/refactor 헷갈리면 위 기준 재확인)
- [ ] scope 가 커밋 범위와 맞는가 (여러 영역이면 빼고 scope 없이)
- [ ] 한 커밋 = 한 목적 (여러 목적이 섞이면 분리)
- [ ] `Co-Authored-By` 포함 안 함
- [ ] `git diff --staged` 로 실제 변경 내용과 제목이 일치하는지 재확인

---

## 10. PR 제목

PR 제목도 동일 컨벤션. Squash merge 시 자동으로 깔끔한 한 줄 히스토리 유지.

```
feat(ui): add automatic update notification via GitHub Releases API
```

PR 본문에 "왜" 를 설명, 커밋 본문엔 최소화해도 됨.
