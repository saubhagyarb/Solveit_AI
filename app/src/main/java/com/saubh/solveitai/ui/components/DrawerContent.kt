package com.saubh.solveitai.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.saubh.solveitai.ui.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(viewModel: ChatViewModel, scope : CoroutineScope, drawerState : DrawerState){
    val savedChats by viewModel.savedChats.collectAsState(initial = emptyList())
    ModalDrawerSheet(
        modifier = Modifier
            .width(320.dp) // Fixed width instead of fillMaxHeight for better UX
            .fillMaxHeight()
            .padding(end = 16.dp), // Add padding to prevent full screen coverage
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerContentColor = MaterialTheme.colorScheme.onSurface
    ) {
        BackHandler {
            scope.launch {
                drawerState.close()
            }
        }
        // Header section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(24.dp)
        ) {
            Text(
                text = "Saved Chats",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "${savedChats.size} conversations",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (savedChats.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No saved chats yet",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Your saved conversations will appear here",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        } else {
            // Chat list
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                savedChats.forEach { chat ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                viewModel.loadChat(chat)
                                scope.launch { drawerState.close() }
                            },
                        shape = RoundedCornerShape(16.dp), // Fully rounded corners
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp,
                            hoveredElevation = 4.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = chat.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                // Add timestamp or message count if available
                                Text(
                                    text = "Tap to continue",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }

                            // Delete button in a proper clickable container
                            IconButton(
                                onClick = { viewModel.deleteChat(chat.id) },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Chat",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Footer with additional options
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                onClick = {
                    viewModel.deleteAllChat()
                }
            ) {
                Text(
                    text = "Clear All Chats",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
}}