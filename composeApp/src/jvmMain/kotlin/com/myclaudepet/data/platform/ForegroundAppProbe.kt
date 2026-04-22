package com.myclaudepet.data.platform

import java.util.concurrent.TimeUnit

/**
 * 현재 포그라운드 앱 이름 조회.
 *
 * macOS 만 지원 (System Events osascript). Windows/Linux 는 별도 구현 필요 → null.
 */
object ForegroundAppProbe {
    private val isMac: Boolean by lazy {
        System.getProperty("os.name", "").lowercase().contains("mac")
    }

    fun current(): String? {
        if (!isMac) return null
        return runCatching {
            val process = ProcessBuilder(
                "osascript",
                "-e",
                "tell application \"System Events\" to " +
                    "name of first application process whose frontmost is true",
            ).redirectErrorStream(true).start()
            val finished = process.waitFor(1500, TimeUnit.MILLISECONDS)
            if (!finished) {
                process.destroyForcibly()
                return@runCatching null
            }
            process.inputStream.bufferedReader().readText().trim()
                .takeIf { it.isNotBlank() }
        }.getOrNull()
    }
}
