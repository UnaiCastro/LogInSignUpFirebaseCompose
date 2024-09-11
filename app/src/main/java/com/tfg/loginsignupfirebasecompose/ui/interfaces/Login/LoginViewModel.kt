package com.tfg.loginsignupfirebasecompose.ui.interfaces.Login

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.tfg.loginsignupfirebasecompose.data.AppScreens
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/*@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    var email: String by mutableStateOf("")
    var password: String by mutableStateOf("")
    var passwordHidden by mutableStateOf(true)

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login() {
        Log.i("Login", "LoginScreen: $email")
        Log.i("Login", "LoginScreen: $password")
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _navigationEvent.value = AppScreens.DogScreen.route
                }
            }
            .addOnFailureListener {
                _errorMessage.value = "No coinciden la contraseña o el correo"
            }
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun onSignUpClick() {
        _navigationEvent.value = AppScreens.SignUpScreen.route
    }

}*/

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email: String by mutableStateOf("")
    var password: String by mutableStateOf("")
    var passwordHidden by mutableStateOf(true)

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login() {
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            result.fold(
                onSuccess = { _navigationEvent.value = AppScreens.DogScreen.route },
                onFailure = { _errorMessage.value = "No coinciden la contraseña o el correo" }
            )
        }
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun onSignUpClick() {
        _navigationEvent.value = AppScreens.SignUpScreen.route
    }
}




