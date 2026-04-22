#!/bin/bash
# 이 스크립트는 이전에 `lsregister -kill -r` + `killall Dock/Finder` 를 호출했으나,
# 해당 명령은 macOS 의 LaunchServices 데이터베이스와 Finder/Dock 프로세스를
# 전역으로 재설정하여 "시스템 설정" 연결 등 관련 없는 부분까지 망가뜨린 이력이 있다.
#
# 따라서 이 스크립트는 **의도적으로 아무 동작도 하지 않는다**.
# 앱 아이콘 캐시가 필요하면 사용자가 명시적으로 맥을 재부팅하거나 터미널에서
# 직접 안전한 범위만 실행한다.

echo "[reset-icon-caches] disabled — 안전을 위해 시스템 전역 명령을 실행하지 않습니다."
echo "[reset-icon-caches] 아이콘이 안 바뀌면 '재부팅' 하시면 반드시 반영됩니다."
exit 0
