package com.saubh.solveitai.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.saubh.solveitai.ui.components.AnimationSpecs
@Composable
fun AnimatedIconButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.85f else 1f,
        animationSpec = AnimationSpecs.quickScale,
        label = "scale"
    )

    IconButton(
        onClick = {
            pressed = true
            onClick()
        },
        modifier = modifier.scale(scale)
    ) {
        icon()
    }
}