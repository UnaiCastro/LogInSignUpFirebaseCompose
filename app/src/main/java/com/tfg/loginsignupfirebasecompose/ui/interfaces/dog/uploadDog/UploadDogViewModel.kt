package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.uploadDog

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.data.collectionsData.User
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadDogViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dogRepository: DogRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    var name: String by  mutableStateOf("")
    var gender by  mutableStateOf("")
    var breed by mutableStateOf("")
    var description by mutableStateOf("")
    var age by mutableStateOf("")
    var price by mutableStateOf("")
    var status by mutableStateOf("")
    var imageUri by mutableStateOf<Uri?>(null)

    val uid: String by mutableStateOf(authRepository.getCurrentUser()?.uid ?: "")
    private val _currentUser = MutableStateFlow<User>(User())
    val currentUser: StateFlow<User> = _currentUser


    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = userRepository.getUserDetailsById(uid) ?: User()
        }
    }

    fun uploadDog(
        name: String,
        gender: String,
        breed: String,
        description: String,
        age: Int,
        price: Int,
        status: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            val dog = Dog(
                name = name,
                gender = gender,
                breed = breed,
                description = description,
                age = age,
                price = price,
                status = status,
                imageUrl = imageUri.toString(),
                owner_id = uid
            )
            dogRepository.uploadDog(
                dog
            )
        }
    }

}