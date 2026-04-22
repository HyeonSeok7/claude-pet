package com.myclaudepet.data.platform

import com.myclaudepet.domain.repository.PlatformBridge

class SystemPlatformBridge : PlatformBridge {
    override fun openAccessibilitySettings() {
        SystemSettings.openAccessibilitySettings()
    }

    override fun foregroundAppName(): String? = ForegroundAppProbe.current()

    override fun isClaudeCliRunning(): Boolean = ClaudeCliProbe.isRunning()

    override fun openUrl(url: String) {
        runCatching { java.awt.Desktop.getDesktop().browse(java.net.URI(url)) }
    }
}
