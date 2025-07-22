package com.saubh.solveitai.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saubh.solveitai.ui.components.AppBottomBar
import com.saubh.solveitai.ui.components.AppTopBar
import com.saubh.solveitai.ui.components.DrawerContent
import com.saubh.solveitai.ui.components.ShowAlertDialog
import kotlinx.coroutines.launch

@Composable
fun SolveItApp() {
    val viewModel: ChatViewModel = viewModel()
    val showDialog = remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    if (showDialog.value) {
        ShowAlertDialog(
            onDismissRequest = { showDialog.value = false },
            onConfirmation = {
                viewModel.saveCurrentChat()
                showDialog.value = false
            },
            dialogTitle = "Save Chat",
            dialogText = "Do you want to save this chat?"
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                viewModel = viewModel,
                scope = scope,
                drawerState = drawerState
            )
        },
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    viewModel = viewModel,
                    onBackPressed = { showDialog.value = true },
                    onMenuPressed = { scope.launch { drawerState.open() } }
                )
            },
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                Box(Modifier.imePadding()) {
                    AppBottomBar(viewModel = viewModel)
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            if (showDialog.value) {
                ShowAlertDialog(
                    onDismissRequest = {
                        showDialog.value = false
                        viewModel.messages.clear()
                    },
                    onConfirmation = {
                        viewModel.saveCurrentChat()
                        showDialog.value = false
                        viewModel.messages.clear()
                    },
                    dialogTitle = "Do you want to save this chat?",
                    dialogText = "It will help you to get back to this chat again whenever you want."
                )
            }

            ChatScreen(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel,
                onBackPressed = { showDialog.value = true }
            )
        }
    }
}