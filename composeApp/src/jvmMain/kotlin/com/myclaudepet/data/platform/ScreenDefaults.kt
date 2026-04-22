package com.myclaudepet.data.platform

import com.myclaudepet.domain.model.PetPosition
import java.awt.GraphicsEnvironment

object ScreenDefaults {
    private const val MARGIN_RIGHT = 320
    private const val MARGIN_BOTTOM = 340
    private const val FALLBACK_WIDTH = 1440
    private const val FALLBACK_HEIGHT = 900

    fun initialPosition(): PetPosition {
        val (w, h) = bounds()
        return PetPosition(
            x = w - MARGIN_RIGHT,
            y = h - MARGIN_BOTTOM,
        )
    }

    /**
     * 기본 디스플레이 바운드 (width x height, 물리 픽셀 기준).
     * 실패 시 보수적 FALLBACK 사용.
     */
    fun bounds(): Pair<Int, Int> {
        val b = runCatching {
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                .defaultScreenDevice
                .defaultConfiguration
                .bounds
        }.getOrNull()
        return (b?.width ?: FALLBACK_WIDTH) to (b?.height ?: FALLBACK_HEIGHT)
    }
}
