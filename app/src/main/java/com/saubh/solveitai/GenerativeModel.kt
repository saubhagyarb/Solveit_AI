package com.saubh.solveitai

import com.google.ai.client.generativeai.GenerativeModel

object GenerativeModel {
    val model : GenerativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = "AIzaSyCfvovPYLVfOxzFDIoxEo6ddoyE92i0sS0"
    )
}