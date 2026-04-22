package com.myclaudepet.ui.pet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myclaudepet.ui.theme.PetColors
import com.myclaudepet.ui.theme.PetDimens

@Composable
fun SpeechBubble(
    text: String?,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = text != null,
        enter = fadeIn() + scaleIn(initialScale = 0.85f),
        exit = fadeOut() + scaleOut(targetScale = 0.85f),
        modifier = modifier,
    ) {
        val shape = RoundedCornerShape(PetDimens.BubbleCorner)
        Text(
            text = text.orEmpty(),
            color = PetColors.BubbleText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .widthIn(max = PetDimens.BubbleMaxWidth)
                .background(PetColors.BubbleSurface, shape)
                .border(1.dp, PetColors.BubbleBorder, shape)
                .padding(horizontal = PetDimens.BubblePadding, vertical = 8.dp),
        )
    }
}
