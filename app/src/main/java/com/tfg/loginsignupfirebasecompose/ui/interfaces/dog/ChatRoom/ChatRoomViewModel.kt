package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.ChatRoom

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Chat
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
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
class ChatRoomViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    val uid: String by mutableStateOf(authRepository.getCurrentUser()?.uid ?: "")
    private val _chats = MutableStateFlow<List<Triple<Chat, User, Dog>>>(emptyList())
    val chats: StateFlow<List<Triple<Chat, User, Dog>>> = _chats

    // Usar un mapa para almacenar los últimos mensajes por chat ID
    private val _lastMessages = MutableStateFlow<Map<String, String>>(emptyMap())
    val lastMessages: StateFlow<Map<String, String>> = _lastMessages

    init {
        fetchChats()
        fetchLastMessage()
    }

    private fun fetchLastMessage() {
        viewModelScope.launch {
            try {
                // Obtener los IDs de los chats
                val chatRoomIds = userRepository.getUserChatRooms(uid)
                if (chatRoomIds.isNotEmpty()) {
                    // Para cada chat, obtener el último mensaje
                    chatRoomIds.forEach { chatId ->
                        val chat = chatRepository.getChatById(chatId)
                        chat?.let {
                            // Obtener el ID del último mensaje
                            val lastMessageId = it.lastMessage
                            // Si hay un ID de último mensaje, buscar el contenido del mensaje
                            if (lastMessageId != null) {
                                val message = messageRepository.getMessageById(lastMessageId)
                                message?.let { msg ->
                                    // Actualizar el mapa de últimos mensajes
                                    _lastMessages.value = _lastMessages.value + (chatId to msg.messageContent)
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatRoomViewModel", "Error loading last messages", e)
            }
        }
    }

    private fun fetchChats() {
        viewModelScope.launch {
            try {
                val chatRoomIds = userRepository.getUserChatRooms(uid)
                if (chatRoomIds.isNotEmpty()) {
                    val chatList = userRepository.getChatsByIds(chatRoomIds)
                    val chatWithDetails = chatList.map { chat ->
                        val otherUserId = if (chat.user1id == uid) chat.user2id else chat.user1id
                        val otherUserDetails = userRepository.getUserDetailsById(otherUserId) ?: User(
                            userId = otherUserId,
                            name = "Unknown",
                            email = "",
                            profileImageUrl = "",
                            type = "",
                            address = "",
                            phone = "",
                            coordinates = emptyMap(),
                            likedEstablishments = emptyList(),
                            starred_dogs = emptyList(),
                            sharedDogs = emptyList(),
                            chat_rooms = emptyList(),
                            dogs = emptyList()
                        )

                        val dogDetails = userRepository.getDogDetailsById(chat.dogId) ?: Dog(
                            dogId = chat.dogId,
                            name = "Unknown Dog",
                            breed = "",
                            age = 0,
                            gender = "",
                            description = "",
                            imageUrl = "",
                            owner_id = "",
                            status = "",
                            price = 0,
                            shared_dog_userId = emptyList()
                        )
                        Triple(chat, otherUserDetails, dogDetails)
                    }

                    _chats.value = chatWithDetails
                }
            } catch (e: Exception) {
                Log.e("ChatRoomViewModel", "Error loading user chats", e)
            }
        }
    }

}