#!/usr/bin/env bash
# PostToolUse Hook: Kotlin 파일 편집 시 간단 검증
# - println 잔존 여부 경고
# - TODO/FIXME 개수 보고
# - !! (non-null assertion) 사용 감지
set -euo pipefail

input="$(cat)"
file=$(printf '%s' "$input" | /usr/bin/python3 -c 'import sys,json; d=json.load(sys.stdin); print((d.get("tool_input") or {}).get("file_path",""))' 2>/dev/null || true)

[ -z "$file" ] && exit 0
case "$file" in
  *.kt|*.kts) ;;
  *) exit 0 ;;
esac
[ -f "$file" ] || exit 0

warnings=()
grep -nE '^[^/]*\bprintln\b' "$file" >/dev/null 2>&1 && warnings+=("println 사용 발견 — 로거로 교체 권장")
grep -nE '!![^=]' "$file" >/dev/null 2>&1 && warnings+=("!! (non-null assertion) 사용 발견 — 주석으로 사유 명시 필요")
todo_count=$(grep -cE 'TODO|FIXME' "$file" 2>/dev/null || echo 0)

if [ ${#warnings[@]} -gt 0 ] || [ "$todo_count" -gt 0 ]; then
  printf '{"hookSpecificOutput":{"hookEventName":"PostToolUse","additionalContext":"[kotlin-lint] %s | TODO/FIXME: %s"}}\n' \
    "$(IFS='; '; echo "${warnings[*]:-none}")" "$todo_count"
fi
exit 0
