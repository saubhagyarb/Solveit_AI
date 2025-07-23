package com.saubh.solveitai.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY id DESC")
    fun getAllChats(): Flow<List<Chat>>

    @Query("SELECT * FROM chats WHERE id = :chatId")
    suspend fun getChatById(chatId: String): Chat?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: Chat)

    @Update
    suspend fun updateChat(chat: Chat)

    @Query("DELETE FROM chats WHERE id = :chatId")
    suspend fun deleteChat(chatId: String)

    @Query("DELETE FROM chats")
    suspend fun deleteAllChats()

    @Query("SELECT COUNT(*) FROM chats")
    suspend fun getChatCount(): Int

    // More efficient query for checking if chat exists
    @Query("SELECT EXISTS(SELECT 1 FROM chats WHERE id = :chatId)")
    suspend fun chatExists(chatId: String): Boolean

    // Get only chat names and IDs for drawer (more efficient)
    @Query("SELECT id, name, '' as messages FROM chats ORDER BY id DESC")
    fun getChatSummaries(): Flow<List<Chat>>
}