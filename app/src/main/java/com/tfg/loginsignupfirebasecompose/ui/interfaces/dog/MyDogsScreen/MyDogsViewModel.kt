package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.MyDogsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyDogsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dogRepository: DogRepository,
    private val authRepository: AuthRepository

): ViewModel() {
    private val _myDogs = MutableStateFlow<List<Dog>>(emptyList())
    val myDogs: StateFlow<List<Dog>> = _myDogs

    val currentUser = authRepository.getCurrentUser()!!.uid

    init {
        fetchMyDogs()
    }

    fun fetchMyDogs() {
        viewModelScope.launch {
            _myDogs.value = dogRepository.getDogsByOwner(currentUser)
            Log.d("MyDogsViewModel", "My dogs: ${_myDogs.value}")
        }
    }

    fun deleteDog(dogId: String) {
        viewModelScope.launch {
            dogRepository.deleteDog(dogId)
            userRepository.deleteDog(dogId, currentUser)
            fetchMyDogs()
        }
    }

}