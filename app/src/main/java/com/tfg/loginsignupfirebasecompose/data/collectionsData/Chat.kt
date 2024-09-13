package com.tfg.loginsignupfirebasecompose.data.collectionsData

data class Chat(
    val chatId: String = "",
    val user1Id: String = "",
    val user2Id: String = "",
    val lastMessage: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

