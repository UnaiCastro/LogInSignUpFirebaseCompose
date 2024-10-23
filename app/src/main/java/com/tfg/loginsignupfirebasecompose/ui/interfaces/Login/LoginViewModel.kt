package com.tfg.loginsignupfirebasecompose.ui.interfaces.Login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.loginsignupfirebasecompose.data.Firebase.AppScreens
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel()
{

    var email: String by mutableStateOf("")
    var password: String by mutableStateOf("")
    var passwordHidden by mutableStateOf(true)

    var emailError: Boolean by mutableStateOf(false)
    var passwordError: Boolean by mutableStateOf(false)
    var generalError: String? by mutableStateOf(null)

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login() {
        if (!validateForm()) {
            _errorMessage.value = generalError
            return
        }

        viewModelScope.launch {
            val result = authRepository.login(email, password)
            result.fold(
                onSuccess = { _navigationEvent.value = AppScreens.DogScreen.route },
                onFailure = { _errorMessage.value = "Incorrect email or password" }
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

    fun onForgotPasswordClick() {
        _navigationEvent.value = AppScreens.ResetPasswordScreen.route
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun validateForm(): Boolean {
        emailError = email.isBlank() || !isEmailValid(email)
        passwordError = password.isBlank()

        generalError = when {
            emailError -> "Email format is invalid"
            passwordError -> "Password cannot be empty"
            else -> null
        }

        return !emailError && !passwordError
    }
}




