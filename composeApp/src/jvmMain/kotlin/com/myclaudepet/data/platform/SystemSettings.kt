package com.myclaudepet.data.platform

import java.awt.Desktop
import java.net.URI

object SystemSettings {

    fun openAccessibilitySettings() {
        val os = System.getProperty("os.name").lowercase()
        runCatching {
            when {
                os.contains("mac") -> openUri("x-apple.systempreferences:com.apple.preference.security?Privacy_Accessibility")
                os.contains("win") -> Runtime.getRuntime().exec(arrayOf("cmd", "/c", "start", "ms-settings:privacy"))
                else -> Unit
            }
        }
    }

    private fun openUri(uri: String) {
        val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else return
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(URI(uri))
        }
    }
}
