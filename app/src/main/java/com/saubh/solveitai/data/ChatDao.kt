package com.saubh.solveitai.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY id DESC")
    fun getAllChats(): Flow<List<Chat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: Chat)

    @Query("DELETE FROM chats WHERE id = :chatId")
    suspend fun deleteChat(chatId: String)

    @Query("DELETE FROM chats")
    suspend fun deleteAllChats()
}
