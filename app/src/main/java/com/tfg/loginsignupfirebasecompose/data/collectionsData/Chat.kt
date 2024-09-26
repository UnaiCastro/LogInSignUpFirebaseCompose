package com.tfg.loginsignupfirebasecompose.data.collectionsData

data class Chat(
    var chatId: String = "",
    val dogId: String = "",
    val user1id: String = "",
    val user2id: String = "",
    val lastMessage: String = "",
    val created_at: String = "",
    var messages: List<String> = emptyList()
)

