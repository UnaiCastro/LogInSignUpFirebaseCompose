package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.ProfileScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val uid: String by mutableStateOf(authRepository.getCurrentUser()?.uid ?: "")
    private val _currentUser = MutableStateFlow<String>("")
    val currentUser: StateFlow<String> = _currentUser

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _profileImageUrl = mutableStateOf<String?>(null)
    val profileImageUrl: State<String?> = _profileImageUrl

    init {
        if (uid.isNotEmpty()) {
            getUserName()
            getEmail()
            fetchProfileImage()
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
        authRepository.logout()
    }
    fun goSettingsScreen() {
        // Navegar a la pantalla de ajustes
    }
    fun goSharedScreen() {
        // Navegar a la pantalla de compartir
    }
}
