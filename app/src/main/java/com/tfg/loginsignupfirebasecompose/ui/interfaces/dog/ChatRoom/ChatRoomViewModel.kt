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
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val uid: String by mutableStateOf(authRepository.getCurrentUser()?.uid ?: "")
    private val _chats = MutableStateFlow<List<Triple<Chat, User, Dog>>>(emptyList())
    val chats: StateFlow<List<Triple<Chat, User, Dog>>> = _chats

    init {
        fetchChats()
    }

    private fun fetchChats() {
        viewModelScope.launch {
            try {
                // Obtener las IDs de las salas de chat del usuario
                val chatRoomIds = userRepository.getUserChatRooms(uid)
                if (chatRoomIds.isNotEmpty()) {
                    val chatList = userRepository.getChatsByIds(chatRoomIds)

                    // Para cada chat, obtener el nombre del otro usuario
                    val chatWithDetails  = chatList.map { chat ->
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

                    _chats.value = chatWithDetails as List<Triple<Chat, User, Dog>>
                }
            } catch (e: Exception) {
                Log.e("ChatRoomViewModel", "Error loading user chats", e)
            }
        }
    }
}