<!--
새 버전 릴리즈 시 이 파일을 복사해서 변경 사항만 채운 뒤 아래 명령으로 적용:

  cp docs/release-notes-template.md /tmp/notes.md
  # /tmp/notes.md 편집: <VERSION> 치환 + 변경 사항 작성
  gh release edit vX.Y.Z --repo hspineone/claude-pet --notes-file /tmp/notes.md
-->

## 변경 사항

- (이번 버전에서 바뀐 내용을 bullet 으로 나열)

---

> ⚠️ 업데이트 시 반드시 ClaudePet 앱을 종료한 후 설치해 주세요.

## Windows 설치 방법

1. `ClaudePet-<VERSION>.msi` 파일 다운로드
2. 파일 더블클릭
3. 설치 마법사 진행
4. 시작 메뉴 또는 바탕화면 바로가기로 **ClaudePet** 실행

> SmartScreen 경고가 뜨면 "추가 정보" → "실행" 클릭 (Apple / Microsoft 서명 미적용 상태)

## macOS 설치 방법

1. `ClaudePet-<VERSION>.dmg` 파일 다운로드
2. DMG 파일 열기 → 앱을 **Applications** 폴더로 드래그
3. 터미널에서 아래 명령어 실행 (Gatekeeper 해제):
   ```
   xattr -cr /Applications/ClaudePet.app
   ```
4. Applications 또는 Spotlight 에서 **ClaudePet** 더블클릭하여 실행
5. 첫 실행 시 접근성 권한 다이얼로그:
   - `시스템 설정 열기` → **개인정보 보호 및 보안 → 손쉬운 사용** 에서 ClaudePet 허용
   - 앱 다이얼로그의 `완료 (재시작)` 버튼 클릭 → JVM 재시작까지 자동 처리
