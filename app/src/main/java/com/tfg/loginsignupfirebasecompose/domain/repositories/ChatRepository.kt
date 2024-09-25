package com.tfg.loginsignupfirebasecompose.domain.repositories

import com.tfg.loginsignupfirebasecompose.data.collectionsData.Message
import kotlinx.coroutines.flow.StateFlow

interface ChatRepository {
    suspend fun getMessageIdsForChat(chatId: String): List<String>
    suspend fun getMessageById(messageId: String): Message?
    suspend fun addMessage(newMessage: Message)
    suspend fun getOtherUserId(chatId: String, senderId: String): String
    suspend fun isCreatedChat(userId: StateFlow<String>, dogId: String, ownerId: String): String


}
