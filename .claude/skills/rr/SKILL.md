---
name: rr
description: progress.md SPEC 진행 현황 갱신. 완료/진행 중 체크박스 업데이트, 새 SPEC 추가, Out of Scope 재정리.
---

# /rr — Progress Tracker

`progress.md` 를 SPEC 단위로 갱신한다.

## 입력

- 사용자가 구두로 알려주는 완료 항목.
- git diff 로 감지되는 변경 파일.
- 새 요구사항 (새 SPEC).

## 동작

1. `progress.md` 를 읽는다.
2. 관련 SPEC 섹션의 체크박스 `[ ]` → `[x]`.
3. 새로 파악된 작업은 해당 Phase에 SPEC 추가 (EARS 포맷).
4. 완료된 Phase는 제목에 ✓ 표시.
5. Out of Scope 항목이 이번 작업에 포함됐다면 Out of Scope에서 제거하고 SPEC으로 승격.

## 출력

- 수정된 `progress.md`.
- 변경 요약 (추가/수정/완료 건수) 를 사용자에게 1-2줄로 보고.

## 규칙

- 체크되지 않은 SPEC에 대해 "완료됐을 것으로 보입니다" 같은 추측 금지. 사용자에게 질문.
- SPEC 번호는 재사용 금지 (삭제해도 번호 유지).
- 모든 SPEC은 EARS 포맷 포함: `WHEN ... / IF ... / SHALL ...`.
