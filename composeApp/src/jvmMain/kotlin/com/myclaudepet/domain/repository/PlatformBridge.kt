package com.myclaudepet.domain.repository

interface PlatformBridge {
    fun openAccessibilitySettings()

    /**
     * 현재 포그라운드(최상위) 앱의 고유 식별 문자열.
     * macOS: `osascript` 로 얻은 프로세스 이름.
     * Windows/Linux: 현재 미구현 → null 반환.
     */
    fun foregroundAppName(): String?

    /**
     * Claude Code CLI (`claude` 프로세스) 가 백그라운드에 실행 중인지.
     * 포그라운드 앱과 무관하게 감지되므로, Terminal 이 포커스 아니어도
     * claude 세션이 돌고 있으면 Working 상태로 간주할 수 있다.
     */
    fun isClaudeCliRunning(): Boolean

    /**
     * 기본 브라우저에서 URL 열기. 업데이트 다이얼로그에서 GitHub Releases 페이지로 이동.
     */
    fun openUrl(url: String)
}
