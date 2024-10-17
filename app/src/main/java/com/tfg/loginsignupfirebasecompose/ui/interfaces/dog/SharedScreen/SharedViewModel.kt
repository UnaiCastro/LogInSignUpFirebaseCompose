package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.SharedScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dogRepository: DogRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _dogsShared = MutableStateFlow<List<Dog>>(emptyList())
    val dogsShared: StateFlow<List<Dog>> = _dogsShared.asStateFlow()

    init {
        fetchSharedDogs()
    }

    private fun fetchSharedDogs() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val currentUser = userRepository.getUserDetailsById(userId)
            val sharedDogIds = currentUser?.sharedDogs ?: emptyList()

            if (sharedDogIds.isNotEmpty()) {
                val sharedDogs = dogRepository.getDogsByIds(sharedDogIds)
                _dogsShared.value = sharedDogs
            }
        }
    }

    fun removeDogFromShared(dogId: String) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            userRepository.removeSharedDog(userId, dogId)
            dogRepository.updateSharedBy(dogId, userId, add = false)
            fetchSharedDogs()
        }
    }
}


