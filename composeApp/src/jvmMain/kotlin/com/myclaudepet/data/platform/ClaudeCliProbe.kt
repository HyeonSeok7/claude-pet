package com.myclaudepet.data.platform

import java.util.concurrent.TimeUnit

/**
 * `claude` 프로세스가 현재 실행 중인지 확인한다.
 *
 * macOS / Linux 는 `pgrep -x claude` 로 정확 일치 검색.
 * 매칭 시 exit 0, 없으면 exit 1.
 */
object ClaudeCliProbe {
    private val isPosix: Boolean by lazy {
        val os = System.getProperty("os.name", "").lowercase()
        os.contains("mac") || os.contains("nux") || os.contains("nix")
    }

    fun isRunning(): Boolean {
        if (!isPosix) return false
        return runCatching {
            val process = ProcessBuilder("pgrep", "-x", "claude")
                .redirectErrorStream(true)
                .start()
            val finished = process.waitFor(1000, TimeUnit.MILLISECONDS)
            if (!finished) {
                process.destroyForcibly()
                return@runCatching false
            }
            process.exitValue() == 0
        }.getOrDefault(false)
    }
}
