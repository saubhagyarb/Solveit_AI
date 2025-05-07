package com.saubh.solveitai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.saubh.solveitai.ui.theme.SolveItAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel by viewModels<ChatViewModel>()
        setContent {
            SolveItAITheme {
                Scaffold(
                    topBar = { AppTopBar(viewModel = viewModel)},
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { Box(Modifier.imePadding()) {
                        AppBottomBar(viewModel = viewModel)
                    } }
                ) { innerPadding ->
                    ChatScreen(
                        modifier = Modifier
                            .padding(innerPadding),
                        messages = viewModel.messages,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

