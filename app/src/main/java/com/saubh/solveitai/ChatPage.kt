package com.saubh.solveitai

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.util.Calendar
import java.util.Locale

@Composable
fun ChatScreen(modifier: Modifier = Modifier,
               messages: List<Message>,
               viewModel: ChatViewModel) {
    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        DrawCanvas()
        if (messages.isEmpty()) {
            EmptyScreen(viewModel = viewModel)
        } else {
            ChatList(messages = messages)
        }
    }
}

@Composable
fun ChatList(messages: List<Message>) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            reverseLayout = true
        ) {
            items(messages.reversed()) {
                ChatCard(message = it)
            }
        }
    }
}

@Composable
fun ChatCard(message: Message) {
    val isModel = message.role == "Model"
    val bubbleColor = if (isModel)
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
    else
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
    val textColor = if (isModel)
        MaterialTheme.colorScheme.onSurfaceVariant
    else
        MaterialTheme.colorScheme.onPrimaryContainer
    val icon = if (isModel)
        painterResource(R.drawable.bot)
    else
        painterResource(R.drawable.user)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isModel) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        if (isModel) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 4.dp)
                    .align(alignment = Alignment.Bottom)
            )
        }

        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .background(
                    color = bubbleColor,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isModel) 4.dp else 16.dp,
                        bottomEnd = if (isModel) 16.dp else 4.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
                MarkdownText(
                    markdown = message.message,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 22.sp,
                        letterSpacing = 0.25.sp,
                        color = textColor
                    ),
                    isTextSelectable = true,
                    syntaxHighlightColor = MaterialTheme.colorScheme.surfaceDim,
                    syntaxHighlightTextColor = MaterialTheme.colorScheme.onSurface,
                    linkColor = MaterialTheme.colorScheme.primary
                )
        }

        if (!isModel) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 4.dp)
                    .align(alignment = Alignment.Bottom)
            )
        }
    }
}

@Composable
fun AppTopBar(viewModel: ChatViewModel) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WindowInsets.statusBars.asPaddingValues())
                .height(48.dp)
        ) {
            if (viewModel.messages.isNotEmpty()) {
                IconButton(
                    onClick = { viewModel.messages.clear() },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBackIosNew,
                        contentDescription = "Delete chat",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.cloud_build_svgrepo_com),
                    contentDescription = "App logo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}



@Composable
fun AppBottomBar(viewModel: ChatViewModel) {
    var message by remember { mutableStateOf("") }
    val navigationBarsPadding = WindowInsets.navigationBars.asPaddingValues()
    val bottomPadding = if (navigationBarsPadding.calculateBottomPadding() < 8.dp) {
        16.dp
    } else {
        navigationBarsPadding.calculateBottomPadding()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = bottomPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
            placeholder = {
                Text(
                    "Ask anything...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = false
        )

        Button(
            onClick = {
                if (message.isNotBlank()) {
                    viewModel.sendMessage(message)
                    message = ""
                }
            },
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            enabled = message.isNotBlank(),
            contentPadding = PaddingValues(0.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.send),
                contentDescription = "Send",
                tint = if (message.isNotBlank())MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun getTimeOfDayString(): String {
    val calendar = Calendar.getInstance(Locale.getDefault())
    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

    return when (hourOfDay) {
        in 0..11 -> "Morning"
        in 12..16 -> "Afternoon"
        else -> "Evening"
    }
}

@Composable
fun EmptyScreen(viewModel: ChatViewModel) {
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
        ),
        start = Offset.Zero,
        end = Offset.Infinite,
        tileMode = TileMode.Clamp
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bot_anim))
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )

            Box(
                modifier = Modifier
                    .size(240.dp)
                    .scale(scale)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                            ),
                            radius = 240f
                        ),
                        shape = CircleShape
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    modifier = Modifier.fillMaxSize(),
                    composition = composition,
                    progress = { progress },
                )
            }

            Text(
                text = "Good ${getTimeOfDayString()}!, Let curiosity guide you",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    brush = gradientBrush
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Let's explore together. What's on your mind?",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            SuggestionChips(viewModel = viewModel)
        }
    }
}

@Composable
private fun SuggestionChips(viewModel: ChatViewModel) {
    val suggestions = listOf(
        "Tell me a story" to Icons.Rounded.AutoStories,
        "Daily weather" to Icons.Rounded.WbSunny,
        "Fun facts" to Icons.Rounded.Lightbulb,
        "Help me learn" to Icons.Rounded.School
    )

    val gradientBorderBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 3
    ) {
        suggestions.forEach { (text, icon) ->
            SuggestionChip(
                onClick = { viewModel.sendMessage(text) },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(horizontal = 4.dp),
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                ),
                border = BorderStroke(1.dp, gradientBorderBrush),
                label = {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun DrawCanvas(){
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(primary.copy(alpha = 0.1f), Color.Transparent),
                center = Offset(size.width * 0.2f, size.height * 0.2f),
                radius = size.width * 0.5f
            ),
            radius = size.width * 0.6f,
            center = Offset(size.width * 0.8f, size.height * 0.2f)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(tertiary.copy(alpha = 0.1f), Color.Transparent),
                center = Offset(size.width * 0.7f, size.height * 0.8f),
                radius = size.width * 0.5f
            ),
            radius = size.width * 0.6f,
            center = Offset(size.width * 0.2f, size.height * 0.8f)
        )
    }
}