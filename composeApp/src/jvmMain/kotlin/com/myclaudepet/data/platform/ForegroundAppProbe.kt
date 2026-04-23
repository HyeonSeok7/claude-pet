package com.myclaudepet.data.platform

import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

/**
 * 현재 포그라운드 앱 이름 조회.
 *
 * - **macOS**: `osascript` 의 System Events 로 frontmost 프로세스 이름.
 * - **Windows / 기타**: null. (v1.1.0 에서 JNA 기반 Windows 구현을 시도했으나
 *   jpackage 번들과 "Failed to launch JVM" 충돌로 회수. 재도입은 Phase 10 에서
 *   JDK 22+ FFM API 전환 또는 대체 방식 조사 후 결정.)
 *
 * 실패 시 조용히 null 반환 + `java.util.logging` FINE 레벨 로깅.
 */
object ForegroundAppProbe {

    private val logger: Logger by lazy {
        Logger.getLogger(ForegroundAppProbe::class.java.name)
    }

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
        }.onFailure { logger.log(Level.FINE, "mac foreground probe failed", it) }
            .getOrNull()
    }
}
