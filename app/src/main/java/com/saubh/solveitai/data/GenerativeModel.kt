package com.saubh.solveitai.data

import com.google.ai.client.generativeai.GenerativeModel

object GenerativeModel {
    val model : GenerativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.apiKey
    )
}