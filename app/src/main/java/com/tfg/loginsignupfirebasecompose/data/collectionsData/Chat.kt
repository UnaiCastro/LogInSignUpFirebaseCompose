package com.tfg.loginsignupfirebasecompose.data.collectionsData

data class Chat(
    val chatId: String = "",
    val dogId: String = "",
    val user1id: String = "",
    val user2id: String = "",
    val lastMessage: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

