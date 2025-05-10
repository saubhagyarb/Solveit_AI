package com.saubh.solveitai

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset

object AnimationSpecs {
    val quickFadeIn = tween<Float>(
        durationMillis = 150,
        easing = FastOutSlowInEasing
    )

    val quickScale = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )

    val quickSlide = tween<IntOffset>(
        durationMillis = 200,
        easing = LinearOutSlowInEasing
    )
}