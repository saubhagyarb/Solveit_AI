package com.saubh.solveitai.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saubh.solveitai.ui.components.AppBottomBar
import com.saubh.solveitai.ui.components.AppTopBar

@Composable
fun SolveItApp(){
    val viewModel : ChatViewModel = viewModel()
    Scaffold(
        topBar = {AppTopBar(viewModel = viewModel)},
        modifier = Modifier.fillMaxSize(),
        bottomBar = { Box(Modifier.imePadding()) {
            AppBottomBar(viewModel = viewModel)
        } }
    ) { innerPadding ->
        ChatScreen(
            modifier = Modifier
                .padding(innerPadding),
            viewModel = viewModel
        )
    }
}