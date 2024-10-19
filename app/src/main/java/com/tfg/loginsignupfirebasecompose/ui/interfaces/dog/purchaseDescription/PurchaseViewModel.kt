package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.purchaseDescription

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.data.collectionsData.User
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.ChatRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.PurchaseRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val dogRepository: DogRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val purchaseRepository: PurchaseRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    val uid = authRepository.getCurrentUser()?.uid ?: ""
    private val _dog = MutableStateFlow<Dog?>(null)
    val dog: StateFlow<Dog?> = _dog

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    private val _owner = MutableStateFlow<User?>(null)
    val owner: StateFlow<User?> = _owner

    fun getDogById(dogId: String) {
        viewModelScope.launch {
            val fetchedDog = dogRepository.getDogById(dogId)
            _dog.value = fetchedDog
            getOwnerByDogId(fetchedDog?.owner_id ?: "")
        }
    }

    private fun getOwnerByDogId(ownerId: String) {
        viewModelScope.launch {
            val fetchedOwner = userRepository.getUserDetailsById(ownerId)
            _owner.value = fetchedOwner
        }
    }

    fun adoptOrBuy(dog: Dog, userOwnerId: String, dogId: String) {
        Log.d(
            "PurchaseViewModel",
            "Adopción o compra iniciada para el perro con ID: ${dogId}, propietario ID: $userOwnerId"
        )
        viewModelScope.launch {
            purchaseRepository.newPurchase(dogId, dog.price, uid, userOwnerId)
            dogRepository.adoptOrBuy(dogId, uid, dog.status)
            userRepository.addNewDog(dogId, uid)
            userRepository.deleteDog(dogId, userOwnerId)
        }
    }

    fun navigateToChat(dogId: String) {
        viewModelScope.launch {
            val dog = dogRepository.getDogById(dogId)
            val createdChat: String = chatRepository.isCreatedChat(uid, dogId, dog!!.owner_id)
            userRepository.addChatToRoomChat(createdChat, uid)
            userRepository.addChatToRoomChat(createdChat, dog.owner_id)
            _navigationEvent.value = "chat/$createdChat"
        }
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }
}

