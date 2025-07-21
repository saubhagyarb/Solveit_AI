package com.saubh.solveitai.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saubh.solveitai.R
import com.saubh.solveitai.ui.ChatViewModel

@Composable
fun AppTopBar(viewModel : ChatViewModel, onBackPressed : ()-> Unit) {

    Surface {
        Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(WindowInsets.Companion.statusBars.asPaddingValues())
                .height(48.dp)
        ) {
            if (viewModel.messages.isNotEmpty()) {
                AnimatedIconButton(
                    onClick = onBackPressed,
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBackIosNew,
                            contentDescription = "Delete chat",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    modifier = Modifier.Companion
                        .align(Alignment.Companion.CenterStart)
                        .padding(start = 8.dp)
                )
            } else {
                //TODO add a 3 dot btn where the history will be there to select
            }

            Row(
                modifier = Modifier.Companion.align(Alignment.Companion.Center),
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = ImageVector.Companion.vectorResource(R.drawable.cloud_build_svgrepo_com),
                    contentDescription = "App logo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.Companion
                        .size(28.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Companion.Bold
                )
            }
        }
    }
}