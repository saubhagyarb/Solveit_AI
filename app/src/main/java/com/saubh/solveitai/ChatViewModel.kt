package com.saubh.solveitai

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    val messages by lazy {
        mutableStateListOf<Message>()
    }

    val model : GenerativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = BuildConfig.apiKey
    )

    fun sendMessage(prompt: String) {
        viewModelScope.launch {
            Log.d("Request", "sendMessage: $prompt")
            try {
                val chat = model.startChat(
                    history = messages.map {
                        content(it.role) { text(it.message) }
                    }.toList()
                )

                messages.add(Message(message = prompt, role = "user"))
                delay(500)
                messages.add(Message(message = "...", role = "Model"))

                val response = chat.sendMessage(prompt)
                messages.removeAt(messages.lastIndex)
                messages.add(Message(message = response.text.toString(), role = "Model"))
            } catch (e : Exception){
                messages.removeAt(messages.lastIndex)
                messages.add(Message(message = "Something went wrong!!", role = "Model"))

            }
        }
    }
}