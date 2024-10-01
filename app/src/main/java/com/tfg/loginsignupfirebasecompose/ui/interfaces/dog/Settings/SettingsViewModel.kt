package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val storage: FirebaseStorage
) : ViewModel() {

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

    fun saveBusinessInfo(name: String, address: String, phone: String) = viewModelScope.launch {
        val userId = authRepository.getCurrentUser()?.uid ?: return@launch
        val user = userRepository.getUserDetailsById(userId)
        userRepository.saveBusinessInfo(userId, name, address, phone, user!!.coordinates)
    }

    // Función para eliminar la información del establecimiento
    fun deleteBusinessInfo() = viewModelScope.launch {

        val userId = authRepository.getCurrentUser()?.uid ?: return@launch
        userRepository.deleteBusinessInfo(userId)
    }
}