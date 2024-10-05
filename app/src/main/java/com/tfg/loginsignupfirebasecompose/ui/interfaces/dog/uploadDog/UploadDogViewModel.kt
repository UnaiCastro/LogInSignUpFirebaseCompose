package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.uploadDog

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.data.collectionsData.User
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class UploadDogViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dogRepository: DogRepository,
    private val authRepository: AuthRepository
): ViewModel()
{

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
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()


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
        Log.d("UploadDogViewModel", "Uploading dog with name: $name")
        viewModelScope.launch {
            Log.d("UploadDogViewModel", "Coroutine launched for dog upload")
            try {
                Log.d("UploadDogViewModel", "Trying to upload dog image")
                if (imageUri != null) {
                    Log.d("UploadDogViewModel", "Image URI is not null")
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child("dogs/${UUID.randomUUID()}.jpg")
                    imageRef.putFile(imageUri).await()
                    val downloadUri = imageRef.downloadUrl.await()

                    val dog = Dog(
                        name = name,
                        gender = gender,
                        breed = breed,
                        description = description,
                        age = age,
                        price = price,
                        status = status,
                        profileImageUrl = downloadUri.toString(),
                        owner_id = uid
                    )

                    // Subir los detalles del perro al repositorio (base de datos)
                    dogRepository.uploadDog(dog = dog)
                    Log.d("UploadDogViewModel", "Dog uploaded successfully")
                } else {
                    // Manejar el caso donde no hay imagen seleccionada
                    Log.e("UploadDogViewModel", "No image selected")
                }
            } catch (e: CancellationException) {
                Log.e("UploadDogViewModel", "Task was cancelled: ${e.message}")
            } catch (e: Exception) {
                // Manejar cualquier error que ocurra durante la subida
                Log.e("UploadDogViewModel", "Error uploading dog: ${e.message}")
            }
        }
    }


}