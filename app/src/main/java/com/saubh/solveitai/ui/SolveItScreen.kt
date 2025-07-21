package com.saubh.solveitai.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saubh.solveitai.ui.components.AppBottomBar
import com.saubh.solveitai.ui.components.AppTopBar
import com.saubh.solveitai.ui.components.ShowAlertDialog

@Composable
fun SolveItApp(){
    val viewModel : ChatViewModel = viewModel()
    val showDialog = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {AppTopBar(viewModel = viewModel, onBackPressed = {showDialog.value = true})},
        modifier = Modifier.fillMaxSize(),
        bottomBar = { Box(Modifier.imePadding()) {
            AppBottomBar(viewModel = viewModel)
        } }
    ) { innerPadding ->
        if (showDialog.value){
            ShowAlertDialog(
                onDismissRequest = {
                    showDialog.value = false
                    viewModel.messages.clear()
                },
                onConfirmation = {
                    showDialog.value = false
                },
                dialogTitle = "Do you want to save this chat?",
                dialogText = "It will help you you to bet back to this chat again whenever you want."
            )
        }
        ChatScreen(
            modifier = Modifier
                .padding(innerPadding),
            viewModel = viewModel,
            onBackPressed = {showDialog.value = true}
        )
    }
}