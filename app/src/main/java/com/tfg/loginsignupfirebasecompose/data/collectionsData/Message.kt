package com.tfg.loginsignupfirebasecompose.data.collectionsData

import com.google.firebase.Timestamp

data class Message(
    var messageId: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val messageContent: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
