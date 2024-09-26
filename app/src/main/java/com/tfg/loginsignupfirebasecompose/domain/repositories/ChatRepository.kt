package com.tfg.loginsignupfirebasecompose.domain.repositories

import com.tfg.loginsignupfirebasecompose.data.collectionsData.Chat
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Message

interface ChatRepository {
    suspend fun getMessageIdsForChat(chatId: String): List<String>
    suspend fun getMessageById(messageId: String): Message?
    suspend fun getOtherUserId(chatId: String, senderId: String): String
    suspend fun isCreatedChat(userId: String, dogId: String, ownerId: String): String
    suspend fun getChatById(chatId: String): Chat?
    suspend fun updateChatWithNewMessage(chatId: String, messageId: String)
}
