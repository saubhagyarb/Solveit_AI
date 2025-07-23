package com.saubh.solveitai.ui

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.type.content
import com.saubh.solveitai.data.AppDatabase
import com.saubh.solveitai.data.GenerativeModel
import com.saubh.solveitai.data.Message
import com.saubh.solveitai.data.Chat
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val chatDao = database.chatDao()

    val savedChats = chatDao.getAllChats()

    val messages by lazy {
        mutableStateListOf<Pair<Message, Bitmap?>>()
    }

    // Track current chat ID for updates
    private val _currentChatId = mutableStateOf<String?>(null)
    val currentChatId get() = _currentChatId.value

    fun sendMessage(prompt: String) {
        viewModelScope.launch {
            try {
                val chat = GenerativeModel.model.startChat(
                    history = messages.map {
                        content(it.first.role) { text(it.first.message) }
                    }.toList()
                )

                messages.add(
                    Pair(
                        Message(
                            message = prompt,
                            role = "user",
                            id = System.currentTimeMillis()
                        ), null
                    )
                )

                messages.add(
                    Pair(
                        Message(
                            message = "...",
                            role = "Model",
                            id = System.currentTimeMillis() + 1
                        ), null
                    )
                )

                val response = chat.sendMessage(prompt = prompt)
                messages.removeAt(messages.lastIndex)
                messages.add(
                    Pair(
                        Message(
                            message = response.text.toString(),
                            role = "Model",
                            id = System.currentTimeMillis()
                        ), null
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                if (messages.isNotEmpty()) {
                    messages.removeAt(messages.lastIndex)
                    messages.add(
                        Pair(
                            Message(
                                message = "Something went wrong!!",
                                role = "Model",
                                id = System.currentTimeMillis()
                            ), null
                        )
                    )
                }
            }
        }
    }

    fun sendMessage(prompt: String, image: Bitmap) {
        viewModelScope.launch {
            Log.d("Request", "sendMessage: $prompt")
            try {
                messages.add(
                    Pair(
                        Message(
                            message = prompt,
                            role = "user",
                            id = System.currentTimeMillis()
                        ), image
                    )
                )
                messages.add(
                    Pair(
                        Message(
                            message = "...",
                            role = "Model",
                            id = System.currentTimeMillis() + 1
                        ), null
                    )
                )

                val response = GenerativeModel.model.generateContent(
                    content {
                        image(image)
                        text(prompt)
                    }
                )
                Log.d("TAG", "sendMessage: ${response.text}")
                messages.removeAt(messages.lastIndex)
                messages.add(
                    Pair(
                        Message(
                            message = response.text.toString(),
                            role = "Model",
                            id = System.currentTimeMillis()
                        ), null
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                messages.removeAt(messages.lastIndex)
                messages.add(
                    Pair(
                        Message(
                            message = "Something went wrong!!",
                            role = "Model",
                            id = System.currentTimeMillis()
                        ), null
                    )
                )
                Log.e("EXCEPTION", "sendMessage: ${e.message}")
            }
        }
    }

    fun getTimeOfDay(): String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hourOfDay) {
            in 0..11 -> "Morning"
            in 12..16 -> "Afternoon"
            else -> "Evening"
        }
    }

    fun saveCurrentChat() {
        if (messages.isNotEmpty()) {
            viewModelScope.launch {
                val firstUserMessage = messages.find { it.first.role == "user" }?.first?.message ?: "New Chat"

                val chatId = _currentChatId.value ?: System.currentTimeMillis().toString()

                val chat = Chat(
                    id = chatId,
                    name = firstUserMessage.take(30),
                    messages = messages.map { it.first }
                )

                chatDao.insertChat(chat)

                if (_currentChatId.value == null) {
                    _currentChatId.value = chatId
                }
            }
        }
    }

    fun loadChat(chat: Chat) {
        messages.clear()
        messages.addAll(chat.messages.map { Pair(it, null) })
        _currentChatId.value = chat.id
    }

    fun startNewChat() {
        messages.clear()
        _currentChatId.value = null
    }

    fun deleteChat(chatId: String) {
        viewModelScope.launch {
            chatDao.deleteChat(chatId)
            if (_currentChatId.value == chatId) {
                startNewChat()
            }
        }
    }

    fun deleteAllChat() {
        viewModelScope.launch {
            chatDao.deleteAllChats()
            startNewChat()
        }
    }
}