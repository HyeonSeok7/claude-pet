package com.myclaudepet.ui.pet

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myclaudepet.domain.model.Pet
import com.myclaudepet.domain.model.Satiation
import com.myclaudepet.ui.theme.PetColors
import com.myclaudepet.ui.theme.PetDimens

@Composable
fun StatsOverlay(
    pet: Pet,
    modifier: Modifier = Modifier,
) {
    val satiationFraction by animateFloatAsState(
        targetValue = (pet.satiation.raw / Satiation.MAX).toFloat(),
        label = "satiation",
    )
    val affinityFraction by animateFloatAsState(
        targetValue = pet.affinity.progressWithinLevel,
        label = "affinity",
    )

    Column(
        modifier = modifier
            .background(PetColors.StatsBackdrop, RoundedCornerShape(PetDimens.StatsCardCorner))
            .padding(PetDimens.StatsCardPadding),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Lv.${pet.level}",
                color = PetColors.StatsForeground,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "${pet.affinity.points} pt",
                color = PetColors.StatsForeground.copy(alpha = 0.85f),
                fontSize = 10.sp,
            )
        }
        Bar(fraction = satiationFraction, color = PetColors.SatiationBar)
        Bar(fraction = affinityFraction, color = PetColors.AffinityBar)
    }
}

@Composable
private fun Bar(fraction: Float, color: Color) {
    val safe = fraction.coerceIn(0f, 1f)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(PetDimens.StatsBarHeight)
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.15f)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(safe)
                .height(PetDimens.StatsBarHeight)
                .clip(RoundedCornerShape(50))
                .background(color),
        )
    }
}
