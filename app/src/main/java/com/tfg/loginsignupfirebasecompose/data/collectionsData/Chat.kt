package com.tfg.loginsignupfirebasecompose.data.collectionsData

data class Chat(
    val chatId: String = "",
    val dogId: String = "",
    val user1id: String = "",
    val user2id: String = "",
    val lastMessage: String = "",
    val created_at: Long = System.currentTimeMillis(),
    val messages: List<Message> = emptyList()
)

