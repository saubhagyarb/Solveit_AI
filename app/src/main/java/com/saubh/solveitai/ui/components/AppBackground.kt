package com.saubh.solveitai.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun DrawBackground() {
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary

    Canvas(modifier = Modifier.Companion.fillMaxSize()) {
        drawCircle(
            brush = Brush.Companion.radialGradient(
                colors = listOf(primary.copy(alpha = 0.1f), Color.Companion.Transparent),
                center = Offset(size.width * 0.2f, size.height * 0.2f),
                radius = size.width * 0.5f
            ),
            radius = size.width * 0.6f,
            center = Offset(size.width * 0.8f, size.height * 0.2f)
        )

        drawCircle(
            brush = Brush.Companion.radialGradient(
                colors = listOf(tertiary.copy(alpha = 0.1f), Color.Companion.Transparent),
                center = Offset(size.width * 0.7f, size.height * 0.8f),
                radius = size.width * 0.5f
            ),
            radius = size.width * 0.6f,
            center = Offset(size.width * 0.2f, size.height * 0.8f)
        )
    }
}