package com.myclaudepet.ui.tray

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource

object PetTrayIcon {

    /**
     * `idle_default.png` 가 classpath 에 있으면 그걸 트레이 아이콘으로 사용하고,
     * 없으면 주황색 원(fallback) 으로 대체한다.
     */
    fun load(): Painter {
        val bitmap = loadSpriteOrNull()
        return if (bitmap != null) BitmapPainter(bitmap) else PetDotPainter
    }

    @Suppress("DEPRECATION")
    private fun loadSpriteOrNull(): ImageBitmap? = runCatching {
        useResource("pet/idle_default.png") { loadImageBitmap(it) }
    }.getOrNull()
}

private object PetDotPainter : Painter() {
    override val intrinsicSize = androidx.compose.ui.geometry.Size(16f, 16f)

    override fun DrawScope.onDraw() {
        drawCircle(
            color = Color(0xFFFFC371),
            radius = size.minDimension / 2f,
            center = center,
        )
    }
}
