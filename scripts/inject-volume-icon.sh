#!/bin/bash
# SPEC-035 — dmg 마운트 볼륨 아이콘 주입 (Xcode CLI Tools 의존성 없음).
#
# jpackage 는 dmg 내부 볼륨 루트에 `.VolumeIcon.icns` 를 넣지 않아
# Finder 가 디스크 이미지에 Duke fallback 을 표시한다. 이 스크립트는
# 생성된 dmg 를 UDRW 로 재변환 → read-write 마운트 → 볼륨 아이콘 복사 →
# `HasCustomIcon` FinderInfo 플래그(xattr) 세팅 → UDZO 재압축 순으로 처리.
#
# Usage:
#   ./scripts/inject-volume-icon.sh [DMG_PATH] [ICON_PATH]
# 기본값:
#   DMG_PATH  = dist/ClaudePet-1.0.0.dmg
#   ICON_PATH = composeApp/icons/app-icon.icns

set -euo pipefail

DMG_PATH="${1:-dist/ClaudePet-1.0.0.dmg}"
ICON_PATH="${2:-composeApp/icons/app-icon.icns}"

if [ ! -f "$DMG_PATH" ]; then
  echo "[inject-volume-icon] dmg not found: $DMG_PATH" >&2
  exit 1
fi
if [ ! -f "$ICON_PATH" ]; then
  echo "[inject-volume-icon] icon not found: $ICON_PATH" >&2
  exit 1
fi

# `tmpfs` 가 아닌 사용자 홈 아래에 workdir. hdiutil attach 권한이 안정적.
WORKDIR="$HOME/.cache/claudepet-volicon-$$"
mkdir -p "$WORKDIR"
# 프로젝트 규칙: rm 금지. 종료 시 워크디렉토리는 휴지통으로.
cleanup() {
  # 언마운트 시도 (실패 무시)
  hdiutil detach "$MOUNT_POINT" -quiet 2>/dev/null || true
  [ -d "$WORKDIR" ] && mv "$WORKDIR" "$HOME/.Trash/claudepet-volicon-$(date +%s)" 2>/dev/null || true
}
trap cleanup EXIT

RW_DMG="$WORKDIR/rw.dmg"
MOUNT_POINT="$WORKDIR/mnt"
mkdir -p "$MOUNT_POINT"

echo "[inject-volume-icon] 1/6 convert UDZO → UDRW"
hdiutil convert "$DMG_PATH" -format UDRW -o "$RW_DMG" -quiet

echo "[inject-volume-icon] 2/6 attach read-write"
hdiutil attach "$RW_DMG" -nobrowse -readwrite -noverify -owners off -mountpoint "$MOUNT_POINT" >/dev/null

echo "[inject-volume-icon] 3/6 replace .VolumeIcon.icns"
# jpackage 가 이미 기본 icns 를 넣어둔 경우 read-only 권한이라 cp 가 거부당한다.
# 기존 파일이 있으면 쓰기 권한 풀고 휴지통으로 옮긴 뒤 우리 icns 복사.
if [ -e "$MOUNT_POINT/.VolumeIcon.icns" ]; then
  chmod u+w "$MOUNT_POINT/.VolumeIcon.icns" 2>/dev/null || true
  mv "$MOUNT_POINT/.VolumeIcon.icns" "$HOME/.Trash/old-VolumeIcon-$(date +%s).icns" 2>/dev/null || true
fi
cp "$ICON_PATH" "$MOUNT_POINT/.VolumeIcon.icns"
chmod 644 "$MOUNT_POINT/.VolumeIcon.icns"

echo "[inject-volume-icon] 4/6 set HasCustomIcon flag on volume root (xattr)"
# FinderInfo 32 bytes 중 Finder flags offset 8-9 에 0x0400 (kHasCustomIcon) 세트.
xattr -wx com.apple.FinderInfo \
  "00 00 00 00 00 00 00 00 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00" \
  "$MOUNT_POINT"

echo "[inject-volume-icon] 5/6 detach"
hdiutil detach "$MOUNT_POINT" -quiet

echo "[inject-volume-icon] 6/6 convert UDRW → UDZO (compressed) + replace"
OUT_TMP="$WORKDIR/out.dmg"
hdiutil convert "$RW_DMG" -format UDZO -imagekey zlib-level=9 -o "$OUT_TMP" -quiet
mv "$DMG_PATH" "$HOME/.Trash/$(basename "$DMG_PATH").old-$(date +%s)"
mv "$OUT_TMP" "$DMG_PATH"

echo "[inject-volume-icon] ✓ done → $DMG_PATH"
