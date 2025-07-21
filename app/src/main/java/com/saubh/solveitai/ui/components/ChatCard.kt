package com.saubh.solveitai.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.saubh.solveitai.R
import com.saubh.solveitai.data.Message
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun ChatCard(message: Pair<Message, Bitmap?>) {
    val isModel = message.first.role == "Model"
    var isExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isModel) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Companion.Top
    ) {
        if (isModel) {
            Icon(
                painter = painterResource(R.drawable.bot),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.Companion
                    .size(24.dp)
                    .padding(end = 4.dp)
                    .align(Alignment.Companion.Bottom)
            )
        }

        Box(
            modifier = Modifier.Companion
//                    .widthIn(max = 300.dp)
                .background(
                    color = if (isModel)
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                    else
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isModel) 4.dp else 16.dp,
                        bottomEnd = if (isModel) 16.dp else 4.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                message.second?.let { bitmap ->
                    Image(
                        painter = rememberAsyncImagePainter(bitmap),
                        contentDescription = "Selected image",
                        modifier = Modifier.Companion
                            .size(250.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Companion.Crop
                    )
                }

                val fullText = message.first.message
                val displayText = if (!isExpanded && fullText.length > 300) {
                    fullText.take(300) + "..."
                } else {
                    fullText
                }

                val showSeeMore = remember(fullText) {
                    fullText.length > 300
                }

                MarkdownText(
                    modifier = Modifier.Companion.align(Alignment.Companion.Start),
                    markdown = displayText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 22.sp,
                        letterSpacing = 0.25.sp,
                        color = if (isModel)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    isTextSelectable = true,
                    syntaxHighlightColor = MaterialTheme.colorScheme.surfaceDim,
                    syntaxHighlightTextColor = MaterialTheme.colorScheme.onSurface,
                    linkColor = MaterialTheme.colorScheme.primary
                )

                if (showSeeMore || isExpanded) {
                    Text(
                        text = if (isExpanded) "See less" else "See more",
                        modifier = Modifier.Companion
                            .clickable { isExpanded = !isExpanded }
                            .padding(top = 4.dp)
                            .align(Alignment.Companion.End),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        if (!isModel) {
            Icon(
                painter = painterResource(R.drawable.user),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.Companion
                    .size(24.dp)
                    .padding(start = 4.dp)
                    .align(Alignment.Companion.Bottom)
            )
        }
    }
}