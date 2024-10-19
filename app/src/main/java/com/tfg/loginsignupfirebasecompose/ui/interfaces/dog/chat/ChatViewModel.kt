package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Message
import com.tfg.loginsignupfirebasecompose.data.collectionsData.User
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.ChatRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.MessageRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val authRepository: AuthRepository,
    private val db: FirebaseFirestore
) : ViewModel() {


    val currentIdUser = authRepository.getCurrentUser()!!.uid

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _otherUser = MutableStateFlow<User?>(null)
    val otherUser: StateFlow<User?> = _otherUser

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun loadCurrentUser(userId: String) {
        viewModelScope.launch {
            val user = userRepository.getUserDetailsById(userId)
            _currentUser.value = user
        }
    }

    fun loadMessageIds(chatId: String) {
        viewModelScope.launch {
            try {
                val ids = chatRepository.getMessageIdsForChat(chatId)
                if (ids.isNotEmpty()) {
                    val loadedMessages = ids.map { messageRepository.getMessageById(it) }
                    _messages.value = loadedMessages as List<Message>
                } else {
                    Log.e("ChatViewModel", "No message IDs found for chat: $chatId")
                    _messages.value = emptyList()
                }

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error loading message IDs for chat: $chatId", e)
            }
        }
    }


    fun sendMessage(messageContent: String, chatId: String, senderId: String) {
        viewModelScope.launch {
            val chatObject = chatRepository.getChatById(chatId)
            val receiverId = if (senderId == chatObject?.user1id) {
                chatObject?.user2id
            } else {
                chatObject?.user1id
            }

            val newMessage = Message(
                chatId = chatId,
                senderId = senderId,
                receiverId = receiverId.toString(),
                messageContent = messageContent,
                timestamp = Timestamp.now()
            )
            Log.d(
                "Chat",
                "user1id: ${chatObject?.user1id}, user2id: ${chatObject?.user2id}, senderId: $senderId, receiverId: $receiverId"
            )

            val messageId = messageRepository.addMessage(newMessage)
            chatRepository.updateChatWithNewMessage(chatId, messageId)
            loadMessageIds(chatId)
        }
    }

    fun loadOtherUser(chatId: String) {
        viewModelScope.launch {
            val chat = chatRepository.getChatById(chatId)
            val otherUserId = if (chat?.user1id == currentIdUser) {
                chat?.user2id
            } else {
                chat?.user1id
            }

            val user = userRepository.getUserDetailsById(otherUserId.toString())
            _otherUser.value = user
        }
    }
}
