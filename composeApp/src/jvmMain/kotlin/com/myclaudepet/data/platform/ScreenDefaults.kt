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
     * 저장된 위치가 현재 화면 범위 안에 있는지. 외장 모니터 해제 등으로
     * 좌표가 화면 밖이면 창이 안 보여 사용자가 "안 뜬다" 로 인식한다.
     * margin 은 창이 완전히 가려지지 않도록 한쪽 100px 여유.
     */
    fun isOnScreen(pos: PetPosition, margin: Int = 100): Boolean {
        val (w, h) = bounds()
        return pos.x in 0..(w - margin) && pos.y in 0..(h - margin)
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
