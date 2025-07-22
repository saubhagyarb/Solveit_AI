package com.saubh.solveitai.ui

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
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
            val firstUserMessage = messages.find { it.first.role == "user" }?.first?.message ?: "New Chat"
            viewModelScope.launch {
                val chat = Chat(
                    id = System.currentTimeMillis().toString(),
                    name = firstUserMessage.take(30),
                    messages = messages.map { it.first }
                )
                chatDao.insertChat(chat)
            }
        }
    }

    fun loadChat(chat: Chat) {
        messages.clear()
        messages.addAll(chat.messages.map { Pair(it, null) })
    }

    fun deleteChat(chatId: String) {
        viewModelScope.launch {
            chatDao.deleteChat(chatId)
        }
    }

    fun deleteAllChat(){
        viewModelScope.launch {
            chatDao.deleteAllChats()
        }
    }
}