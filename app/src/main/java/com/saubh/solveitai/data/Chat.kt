package com.saubh.solveitai.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val name: String,
    val messages: List<Message>
)
