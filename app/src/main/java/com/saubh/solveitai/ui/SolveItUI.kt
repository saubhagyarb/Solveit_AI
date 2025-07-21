package com.saubh.solveitai.ui

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.saubh.solveitai.ui.components.AnimationSpecs
import com.saubh.solveitai.R
import com.saubh.solveitai.data.Message
import com.saubh.solveitai.ui.components.ChatCard
import com.saubh.solveitai.ui.components.DrawBackground
import com.saubh.solveitai.ui.components.SuggestionChips

@Composable
    fun ChatScreen(
        modifier: Modifier = Modifier,
        viewModel : ChatViewModel
    ) {
        val messages = viewModel.messages
        Surface(
            modifier = modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DrawBackground()
            if (messages.isEmpty()) {
                EmptyScreen(viewModel = viewModel)
            } else {
                ChatList(messages = messages)
            }
        }
    }

    @Composable
    fun ChatList(messages: List<Pair<Message, Bitmap?>>) {
        val listState = rememberLazyListState()
        val previousSize = remember { mutableIntStateOf(messages.size) }
        LaunchedEffect(messages.size) {
            if (messages.size > previousSize.intValue) {
                listState.animateScrollToItem(0)
            }
            previousSize.intValue = messages.size
        }

        Box(
            modifier = Modifier.Companion
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.Companion.fillMaxSize(),
                reverseLayout = true
            ) {
                items(messages.reversed()) {
                    ChatCard(message = it)
                }
            }
        }
    }

    @Composable
    fun EmptyScreen(viewModel : ChatViewModel) {
        var visible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            visible = true
        }

        val alphaAnimation by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = AnimationSpecs.quickFadeIn,
            label = "fade"
        )

        val scaleAnimation by animateFloatAsState(
            targetValue = if (visible) 1f else 0.8f,
            animationSpec = AnimationSpecs.quickScale,
            label = "scale"
        )

        val gradientBrush = Brush.Companion.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
            ),
            start = Offset.Companion.Zero,
            end = Offset.Companion.Infinite,
            tileMode = TileMode.Companion.Clamp
        )

        Box(modifier = Modifier.Companion.fillMaxSize()) {
            Column(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .alpha(alphaAnimation)
                    .scale(scaleAnimation)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Companion.CenterHorizontally
            ) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bot_anim))
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                )
                LottieAnimation(
                    modifier = Modifier.Companion.size(250.dp),
                    composition = composition,
                    progress = { progress }
                )

                Text(
                    text = "Good ${viewModel.getTimeOfDay()}!, Let curiosity guide you",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Companion.Bold,
                        brush = gradientBrush
                    ),
                    modifier = Modifier.Companion
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Companion.Center
                )

                Spacer(modifier = Modifier.Companion.height(16.dp))

                Text(
                    "Let's explore together. What's on your mind?",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Companion.Medium
                    ),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Companion.Center
                )
                SuggestionChips(viewModel = viewModel)
            }
        }
    }