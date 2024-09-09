package com.tfg.loginsignupfirebasecompose.interfaces.Login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tfg.loginsignupfirebasecompose.data.AppScreens

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    var email: String by mutableStateOf("")
    var password: String by mutableStateOf("")
    var passwordHidden by mutableStateOf(true) // Añadir esta línea


    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    fun login() {
        Log.i("Login", "LoginScreen: $email")
        Log.i("Login", "LoginScreen: $password")
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _navigationEvent.value = AppScreens.DogScreen.route
                Log.i("Login", "LoginScreen: success")
            } else {
                Log.i("Login", "LoginScreen: failure")
            }
        }
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }
}


