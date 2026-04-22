#!/bin/bash
# SPEC-034 — 로컬 end-to-end 재배포 자동화.
#
# clean → packageDmg (+ collectDist + injectVolumeIcon + dmgIconize)
# → /Applications 이전본 휴지통 이동 → dmg 마운트 → 설치 → 언마운트 → 캐시 리셋.

set -euo pipefail

cd "$(dirname "$0")/.."

echo ">>> 0. Stop running dev app"
pkill -f "com.myclaudepet.MainKt" 2>/dev/null || true

echo ">>> 1. Detach lingering ClaudePet volumes"
for v in /Volumes/ClaudePet /Volumes/ClaudePet\ 2 /Volumes/ClaudePet\ 3; do
  [ -d "$v" ] && hdiutil detach -force "$v" 2>/dev/null || true
done

echo ">>> 2. Move previous /Applications/ClaudePet.app to Trash"
if [ -d /Applications/ClaudePet.app ]; then
  mv /Applications/ClaudePet.app "$HOME/.Trash/ClaudePet.app.old-$(date +%s)"
fi

echo ">>> 3. Gradle clean + packageDmg"
./gradlew clean :composeApp:packageDmg --rerun-tasks

DMG="dist/ClaudePet-1.0.0.dmg"
if [ ! -f "$DMG" ]; then
  echo "!!! $DMG not produced — abort" >&2
  exit 1
fi

echo ">>> 4. Mount new dmg"
hdiutil attach "$DMG" -nobrowse -mountpoint /Volumes/ClaudePet >/dev/null

echo ">>> 5. Install to /Applications"
cp -R /Volumes/ClaudePet/ClaudePet.app /Applications/

echo ">>> 6. Detach"
hdiutil detach /Volumes/ClaudePet >/dev/null

echo ""
echo "✅ Done."
echo "   Mount $DMG in Finder → check the desktop disk icon is NOT Duke."
echo "   /Applications/ClaudePet.app → check the app icon is the pet."
echo ""
echo "   아이콘이 캐시 때문에 이전과 다르게 보이면 맥을 재부팅하세요."
echo "   (자동 캐시 리셋은 '시스템 설정' 등 무관한 부분까지 영향이 있어 제거했습니다.)"
