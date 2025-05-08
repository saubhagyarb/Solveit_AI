package com.saubh.solveitai

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.type.content
import com.saubh.solveitai.GenerativeModel.model
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    val messages by lazy {
        mutableStateListOf<Message>()
    }

    fun sendMessage(prompt: String) {
        viewModelScope.launch {
            Log.d("Request", "sendMessage: $prompt")
            try {
                val chat = model.startChat(
                    history = messages.map {
                        content(it.role) { text(it.message) }
                    }.toList()
                )

                messages.add(Message(message = prompt, role = "user", id = System.currentTimeMillis()))
                messages.add(Message(message = "...", role = "Model", id = System.currentTimeMillis() + 1))

                val response = chat.sendMessage(prompt = prompt)
                Log.d("TAG", "sendMessage: ${response.text}")
                messages.removeAt(messages.lastIndex)
                messages.add(Message(message = response.text.toString(), role = "Model", id = System.currentTimeMillis()))
            } catch (e : Exception){
                e.printStackTrace()
                messages.removeAt(messages.lastIndex)
                messages.add(Message(message = "Something went wrong!!", role = "Model", id = System.currentTimeMillis()))
                Log.e("EXCEPTION", "sendMessage: ${e.message}")
            }
        }
    }

}