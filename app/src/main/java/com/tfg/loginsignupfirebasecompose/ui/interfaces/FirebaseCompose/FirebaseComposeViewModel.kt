package com.tfg.loginsignupfirebasecompose.ui.interfaces.FirebaseCompose

import androidx.lifecycle.ViewModel
import com.tfg.loginsignupfirebasecompose.data.Firebase.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FirebaseComposeViewModel @Inject constructor() : ViewModel() {

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    fun onSignUpClick() {
        _navigationEvent.value = AppScreens.SignUpScreen.route
    }

    fun onLoginClick() {
        _navigationEvent.value = AppScreens.LoginScreen.route
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }
}
