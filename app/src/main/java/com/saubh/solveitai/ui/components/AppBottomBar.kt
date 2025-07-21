package com.saubh.solveitai.ui.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil3.compose.rememberAsyncImagePainter
import com.saubh.solveitai.R
import com.saubh.solveitai.ui.ChatViewModel
import java.util.Locale

@Composable
fun AppBottomBar(viewModel : ChatViewModel) {
    var message by remember { mutableStateOf("") }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showPhotoOptions by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { capturedBitmap ->
        bitmap = capturedBitmap
    }

    val activity = context as Activity

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val spokenText = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
            if (spokenText != null) {
                message = spokenText
            } else {
                Toast.makeText(context, "Failed to recognize speech", Toast.LENGTH_SHORT).show()
            }
        }
    )


    val navigationBarsPadding = WindowInsets.Companion.navigationBars.asPaddingValues()
    val bottomPadding = if (navigationBarsPadding.calculateBottomPadding() < 8.dp) {
        16.dp
    } else {
        navigationBarsPadding.calculateBottomPadding()
    }

    Column {
        bitmap?.let { image ->
            Box(
                modifier = Modifier.Companion
                    .height(100.dp)
                    .width(100.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier.Companion
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(image),
                        contentDescription = "Selected image",
                        modifier = Modifier.Companion
                            .fillMaxHeight()
                            .align(Alignment.Companion.Center),
                        contentScale = ContentScale.Companion.Fit
                    )
                }

                AnimatedIconButton(
                    onClick = { bitmap = null },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove image",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.Companion.size(18.dp)
                        )
                    },
                    modifier = Modifier.Companion
                        .align(Alignment.Companion.TopEnd)
                        .padding(4.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                            CircleShape
                        )
                        .size(24.dp)
                )
            }
        }

        if (showPhotoOptions) {
            Dialog(onDismissRequest = { showPhotoOptions = false }) {
                Surface(
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    Column(modifier = Modifier.Companion.padding(16.dp)) {
                        Text(
                            "Select photo from",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.Companion.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.Companion.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                                modifier = Modifier.Companion
                                    .weight(1f)
                                    .clickable {
                                        showPhotoOptions = false
                                        cameraLauncher.launch(null)
                                    }
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = "Camera",
                                    modifier = Modifier.Companion.size(48.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.Companion.height(8.dp))
                                Text("Camera", style = MaterialTheme.typography.bodyMedium)
                            }

                            Column(
                                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                                modifier = Modifier.Companion
                                    .weight(1f)
                                    .clickable {
                                        showPhotoOptions = false
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(
                                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                    }
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = "Gallery",
                                    modifier = Modifier.Companion.size(48.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.Companion.height(8.dp))
                                Text("Gallery", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = bottomPadding),
            verticalAlignment = Alignment.Companion.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = message,
                maxLines = 4,
                onValueChange = { message = it },
                modifier = Modifier.Companion.weight(1f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Companion.Sentences,
                    autoCorrectEnabled = true,
                    keyboardType = KeyboardType.Companion.Text
                ),
                leadingIcon = {
                    AnimatedIconButton(
                        onClick = { showPhotoOptions = true },
                        modifier = Modifier.Companion.size(24.dp),
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = "Add photo",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                },
                trailingIcon = {
                    AnimatedIconButton(
                        onClick = {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                )
                                == PackageManager.PERMISSION_GRANTED
                            ) {
                                val intent =
                                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                        putExtra(
                                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                        )
                                        putExtra(
                                            RecognizerIntent.EXTRA_LANGUAGE,
                                            Locale.getDefault()
                                        )
                                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
                                    }
                                speechRecognizerLauncher.launch(intent)
                            } else {
                                ActivityCompat.requestPermissions(
                                    activity,
                                    arrayOf(Manifest.permission.RECORD_AUDIO),
                                    100
                                )
                            }
                        },
                        modifier = Modifier.Companion.size(24.dp),
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Say something",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                },
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
                    if (message.isNotBlank() || bitmap != null) {
                        if (bitmap != null) viewModel.sendMessage(message, bitmap!!)
                        else viewModel.sendMessage(message)
                        message = ""
                        bitmap = null
                    }
                },
                modifier = Modifier.Companion.size(48.dp),
                shape = CircleShape,
                enabled = message.isNotBlank() || bitmap != null,
                contentPadding = PaddingValues(0.dp),
            ) {
                Icon(
                    imageVector = ImageVector.Companion.vectorResource(R.drawable.send),
                    contentDescription = "Send",
                    tint = if (message.isNotBlank() || bitmap != null)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}