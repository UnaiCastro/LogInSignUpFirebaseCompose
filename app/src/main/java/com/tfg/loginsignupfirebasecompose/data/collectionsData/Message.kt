package com.tfg.loginsignupfirebasecompose.data.collectionsData

data class Message(
    val messageId: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val messageContent: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
