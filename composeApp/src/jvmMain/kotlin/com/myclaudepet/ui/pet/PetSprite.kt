package com.myclaudepet.ui.pet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import com.myclaudepet.domain.model.PetAnimationState
import kotlinx.coroutines.delay

/**
 * 주어진 상태의 PNG 를 JVM classpath 에서 로드한다.
 * Walking 상태는 `idle_walk_01.png` ↔ `idle_walk_02.png` 2프레임 교차 렌더.
 * 리소스가 없으면 null → 호출자는 Canvas fallback.
 *
 * Compose resources 라이브러리(`Res.drawable.*`) 대신 전통적 JVM classpath
 * 로딩을 쓰는 이유: 사용자가 AI 로 생성한 PNG 를 `resources/pet/` 에 드롭하면
 * 즉시 로드되도록 하기 위함.
 */
@Suppress("DEPRECATION")
@Composable
fun rememberPetSprite(state: PetAnimationState): ImageBitmap? {
    if (state == PetAnimationState.Walking) return rememberWalkingFrames()
    val path = "pet/${state.resourceName}.png"
    return remember(state) {
        runCatching { useResource(path) { loadImageBitmap(it) } }.getOrNull()
    }
}

@Suppress("DEPRECATION")
@Composable
private fun rememberWalkingFrames(): ImageBitmap? {
    val frames = remember {
        listOfNotNull(
            loadFrame("pet/idle_walk_01.png"),
            loadFrame("pet/idle_walk_02.png"),
        )
    }
    if (frames.isEmpty()) return null
    if (frames.size == 1) return frames.first()

    var index by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(FRAME_INTERVAL_MS)
            index = (index + 1) % frames.size
        }
    }
    return frames[index]
}

@Suppress("DEPRECATION")
private fun loadFrame(path: String): ImageBitmap? = runCatching {
    useResource(path) { loadImageBitmap(it) }
}.getOrNull()

// 걷기 2프레임 교차 주기. 한 leg(1800ms) 안에 4~5번 교체되어 "2걸음" 느낌.
private const val FRAME_INTERVAL_MS = 420L
