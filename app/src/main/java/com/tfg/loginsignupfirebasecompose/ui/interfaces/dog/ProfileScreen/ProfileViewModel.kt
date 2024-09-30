package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.ProfileScreen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.tfg.loginsignupfirebasecompose.data.Firebase.AppScreens
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val storage: FirebaseStorage,
) : ViewModel() {

    val uid: String by mutableStateOf(authRepository.getCurrentUser()?.uid ?: "")
    private val _currentUser = MutableStateFlow<String>("")
    val currentUser: StateFlow<String> = _currentUser

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _adress = MutableStateFlow<String>("")
    val adress: StateFlow<String> = _adress

    private val _profileImageUrl = mutableStateOf<String?>(null)
    val profileImageUrl: State<String?> = _profileImageUrl

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    init {
        if (uid.isNotEmpty()) {
            getUserName()
            getEmail()
            fetchProfileImage()
            getAdress()
        }
    }

    private fun getAdress() {
        viewModelScope.launch {
            val adress = userRepository.getAddress(uid)
            _adress.value = (adress ?: "Dirección desconocido").toString()
        }
    }

    private fun fetchProfileImage() {
        viewModelScope.launch {
            val imageUrl = userRepository.getProfileImageUrl(uid)
            _profileImageUrl.value = imageUrl
        }
    }

    private fun getUserName() {
        viewModelScope.launch {
            val name = userRepository.getUserName(uid)
            _currentUser.value = (name ?: "Usuario desconocido").toString()
        }
    }

    private fun getEmail() {
        viewModelScope.launch {
            val email = userRepository.getEmail(uid)
            _email.value = (email ?: "Email desconocido").toString()
        }
    }
    fun logout() {
        Log.i("ProfileViewModel", "Logout function called")
        authRepository.logout()
        _navigationEvent.value = AppScreens.FirebaseComposeScreen.route // Navegar fuera de DogScreen
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    fun goSettingsScreen() {
        // Navegar a la pantalla de ajustes
    }
    fun goSharedScreen() {
        // Navegar a la pantalla de compartir
    }

    fun goToChatRoomScreen() {

    }

    fun changeProfileImage(context: Context, newImageUri: Uri?) {
        viewModelScope.launch {
            // Guarda la nueva imagen en Firebase Storage
            val imageUrl = uploadImageToFirebaseStorage(newImageUri)

            // Actualiza el URL de la imagen en el estado
            _profileImageUrl.value = imageUrl

            // Guarda la URL en el usuario en la base de datos
            userRepository.updateUserProfileImage(imageUrl,uid)
        }
    }

    private suspend fun uploadImageToFirebaseStorage(newImageUri: Uri?): String? {
        return try {
            val storageRef = storage.reference.child("profile_images/${UUID.randomUUID()}.jpg")
            if (newImageUri != null) {
                storageRef.putFile(newImageUri).await()
            } // Utiliza Kotlin Coroutines para esperar la subida

            // Obtener el URL de descarga
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Error uploading image", e)
            null
        }
    }

}
