package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Message
import com.tfg.loginsignupfirebasecompose.data.collectionsData.User
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.ChatRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
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
    private val dogRepository: DogRepository,
    private val messageRepository: MessageRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    val currentIdUser = authRepository.getCurrentUser()!!.uid

    private val _messageIds = MutableStateFlow<List<String>>(emptyList())
    val messageIds: StateFlow<List<String>> = _messageIds

    // Cambiar de Message? a List<Message>
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

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
            val ids = chatRepository.getMessageIdsForChat(chatId)
            _messageIds.value = ids

            // Cargar mensajes por ID y actualizar _messages
            val loadedMessages = ids.map { chatRepository.getMessageById(it) }
            _messages.value = loadedMessages as List<Message>
        }
    }

    fun sendMessage(messageContent: String, chatId: String, senderId: String) {
        viewModelScope.launch {
            val newMessage = Message(
                chatId = chatId,
                senderId = senderId,
                receiverId = getReceiverId(chatId, senderId),  // Lógica para obtener el receptor
                messageContent = messageContent
            )
            chatRepository.addMessage(newMessage)

            // Volver a cargar los mensajes después de enviar uno nuevo
            loadMessageIds(chatId)
        }
    }

    private fun getReceiverId(chatId: String, senderId: String): String {
        var chatReceiver = ""
        viewModelScope.launch {
            chatReceiver = chatRepository.getOtherUserId(chatId, senderId)
        }
        return chatReceiver
    }
}
