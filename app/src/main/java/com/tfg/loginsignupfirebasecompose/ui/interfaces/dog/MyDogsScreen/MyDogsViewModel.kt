package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.MyDogsScreen

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

    private fun fetchMyDogs() {
        viewModelScope.launch {
            // Llama a la función getSharedDogsObject para obtener la lista de perros compartidos
            _myDogs.value = dogRepository.getDogsByOwner(currentUser)
        }
    }

}