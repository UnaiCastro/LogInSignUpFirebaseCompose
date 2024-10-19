package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.loginsignupfirebasecompose.data.Firebase.AppScreens
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import com.tfg.loginsignupfirebasecompose.navigation.BottomNavItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val uid: String by mutableStateOf(authRepository.getCurrentUser()?.uid ?: "")
    private val _currentUser = MutableStateFlow<String>("")
    val currentUser: StateFlow<String> = _currentUser

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    private val _selectedNavItem = MutableStateFlow<BottomNavItem>(BottomNavItem.Inicio)
    val selectedNavItem: StateFlow<BottomNavItem> = _selectedNavItem

    init {
        if (uid.isNotEmpty()) {
            getUserName()
        }
    }

    fun logout() {
        authRepository.logout()
        _navigationEvent.value = AppScreens.FirebaseComposeScreen.route
        Log.i("Logout", "LogoutScreen: success")
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    private fun getUserName() {
        viewModelScope.launch {
            val name = userRepository.getUserName(uid)
            _currentUser.value = (name ?: "Unknown user").toString()
        }
    }

    fun onNavItemSelected(item: BottomNavItem) {
        _selectedNavItem.value = item
    }
}

