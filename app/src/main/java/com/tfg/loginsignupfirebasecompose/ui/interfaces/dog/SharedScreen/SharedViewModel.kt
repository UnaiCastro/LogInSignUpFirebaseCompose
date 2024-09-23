package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.SharedScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository

):ViewModel() {

    private val _dogsShared = MutableStateFlow<List<Dog>>(emptyList())
    val dogsShared: StateFlow<List<Dog>> = _dogsShared

    val currentUser = authRepository.getCurrentUser()!!.uid

    init {
        loadSharedDogs()
    }

    private fun loadSharedDogs() {
        viewModelScope.launch {
            // Llama a la función getSharedDogsObject para obtener la lista de perros compartidos
            _dogsShared.value = userRepository.getSharedDogsObject(currentUser)
        }
    }

}