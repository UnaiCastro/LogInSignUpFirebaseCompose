package com.tfg.loginsignupfirebasecompose.interfaces.FirebaseCompose

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.data.AppScreens
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
