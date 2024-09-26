package com.tfg.loginsignupfirebasecompose.domain.repositories

import com.tfg.loginsignupfirebasecompose.data.collectionsData.Message

interface MessageRepository {
    suspend fun addMessage(newMessage: Message): String
    suspend fun getMessageById(lastMessageId: String): Message?
}