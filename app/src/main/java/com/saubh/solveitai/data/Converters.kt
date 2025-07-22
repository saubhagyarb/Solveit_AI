package com.saubh.solveitai.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromMessageList(messages: List<Message>): String {
        return gson.toJson(messages)
    }

    @TypeConverter
    fun toMessageList(value: String): List<Message> {
        val listType = object : TypeToken<List<Message>>() {}.type
        return gson.fromJson(value, listType)
    }
}
