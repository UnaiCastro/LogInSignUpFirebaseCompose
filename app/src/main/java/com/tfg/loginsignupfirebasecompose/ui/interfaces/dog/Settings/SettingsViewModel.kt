package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Settings
import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val storage: FirebaseStorage
) : ViewModel() {

    // Subir imagen desde la galería
    fun uploadProfileImage(imageUri: Uri, contentResolver: ContentResolver) = viewModelScope.launch {
        val userId = authRepository.getCurrentUser() ?: return@launch
        val currentUser= userId.uid

        try {
            // Subir imagen a Firebase Storage
            val storageRef = storage.reference.child("profileImages/$currentUser.jpg")
            val uploadTask = storageRef.putFile(imageUri)
            uploadTask.await()

            // Obtener URL pública de la imagen
            val downloadUrl = storageRef.downloadUrl.await()

            // Actualizar URL de la imagen en Firestore
            userRepository.updateProfileImage(currentUser, downloadUrl.toString())

        } catch (e: Exception) {
            Log.e("UploadImage", "Error al subir la imagen", e)
        }
    }

    // Subir imagen desde la cámara (Bitmap)
    fun uploadProfileImageFromCamera(bitmap: Bitmap) = viewModelScope.launch {
        val userId = authRepository.getCurrentUser() ?: return@launch
        val currentUser= userId.uid

        try {
            // Convertir Bitmap a ByteArray para subir a Firebase Storage
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageData = baos.toByteArray()

            // Subir imagen a Firebase Storage
            val storageRef = storage.reference.child("profileImages/$userId.jpg")
            val uploadTask = storageRef.putBytes(imageData)
            uploadTask.await()

            // Obtener URL pública de la imagen
            val downloadUrl = storageRef.downloadUrl.await()

            // Actualizar URL de la imagen en Firestore
            userRepository.updateProfileImage(currentUser, downloadUrl.toString())

        } catch (e: Exception) {
            Log.e("UploadImage", "Error al subir la imagen", e)
        }
    }

    val currentUser = authRepository.getCurrentUser()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _profileImageUrl = MutableStateFlow("")
    val profileImageUrl: StateFlow<String> = _profileImageUrl

    private val _userType = MutableStateFlow("")
    val userType: StateFlow<String> = _userType

    init {
        loadUserData()
    }

    private fun loadUserData() = viewModelScope.launch {
        val uid = authRepository.getCurrentUser() ?: return@launch
        val user = userRepository.getUserDetailsById(currentUser!!.uid)
        _name.value = user?.name ?: ""
        _phone.value = user?.phone ?: ""
        _address.value = user?.address ?: ""
        _profileImageUrl.value = user?.profileImageUrl ?: ""
        _userType.value = user?.type ?: "Particular"
    }

    fun updateName(newName: String) = viewModelScope.launch {
        _name.value = newName
        userRepository.updateName(currentUser!!.uid, newName)
    }

    fun updatePhone(newPhone: String) = viewModelScope.launch {
        _phone.value = newPhone
        userRepository.updatePhone(currentUser!!.uid, newPhone)
    }

    fun updateAddress(newAddress: String) = viewModelScope.launch {
        _address.value = newAddress
        userRepository.updateAddress(currentUser!!.uid, newAddress)
    }

    fun updateUserType(newType: String) = viewModelScope.launch {
        _userType.value = newType
        userRepository.updateUserType(currentUser!!.uid, newType)
    }

    fun updateProfileImage(newImageUrl: String) = viewModelScope.launch {
        _profileImageUrl.value = newImageUrl
        userRepository.updateProfileImage(currentUser!!.uid, newImageUrl)
    }
}