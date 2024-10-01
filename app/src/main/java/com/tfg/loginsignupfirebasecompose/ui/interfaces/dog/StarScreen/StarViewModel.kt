package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.StarScreen

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarViewModel @Inject constructor (
    private val userRepository: UserRepository,
    private val dogRepository: DogRepository,
    private val authRepository: AuthRepository

): ViewModel(){

    private val _starredDogs = MutableStateFlow<List<Dog>>(emptyList())
    val starredDogs: StateFlow<List<Dog>> = _starredDogs

    val currentUser = authRepository.getCurrentUser()!!.uid

    init {
        fetchStarredDogs()
    }

    private fun fetchStarredDogs() {
        viewModelScope.launch {
            // Llama a la función getSharedDogsObject para obtener la lista de perros compartidos
            _starredDogs.value = userRepository.getStarredDogsObject(currentUser)
        }
    }

    fun toggleStarredDog(dog: Dog) = viewModelScope.launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
        val updatedStarredDogs = _starredDogs.value.toMutableList()

        if (updatedStarredDogs.contains(dog)) {
            updatedStarredDogs.remove(dog)
        } else {
            updatedStarredDogs.add(dog)
        }

        _starredDogs.value = updatedStarredDogs
        userRepository.updateStarredDogsById(uid, dog.dogId)
    }




}