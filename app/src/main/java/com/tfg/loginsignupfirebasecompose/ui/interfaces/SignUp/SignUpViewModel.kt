package com.tfg.loginsignupfirebasecompose.ui.interfaces.SignUp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.loginsignupfirebasecompose.data.Firebase.AppScreens
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    // Campos comunes
    var name: String by mutableStateOf("")
    var email: String by mutableStateOf("")
    var password: String by mutableStateOf("")
    var passwordHidden by mutableStateOf(true)

    // Campos específicos para empresas
    var userType: String by mutableStateOf("Particular") // "Particular" o "Empresa"
    var companyName: String by mutableStateOf("")
    var phone: String by mutableStateOf("")
    var selectedRegion: String by mutableStateOf("")
    var coordinates: Pair<String, String> by mutableStateOf(Pair("", "")) // Latitud, Longitud

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    /*fun signUp() {
        viewModelScope.launch {
            val result = authRepository.signUp(email, password)
            result.fold(
                onSuccess = { firebaseUser ->
                    val uid = firebaseUser.uid
                    val user = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "dogs" to arrayListOf<String>()
                    )
                    val saveResult = userRepository.saveUser(uid, user)
                    saveResult.fold(
                        onSuccess = { navigateToLogin() },
                        onFailure = { _errorMessage.value = "Cannot sign up" }
                    )
                },
                onFailure = { _errorMessage.value = "Error al crear el usuario" }
            )
        }
    }*/

    fun signUp() {
        viewModelScope.launch {
            val result = authRepository.signUp(email, password)
            result.fold(
                onSuccess = { firebaseUser ->
                    val uid = firebaseUser.uid
                    val user = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "dogs" to arrayListOf<String>()
                    )
                    val saveResult = userRepository.saveUser(uid, user)
                    saveResult.fold(
                        onSuccess = {
                            if (userType == "Empresa") {
                                // Si es una empresa, guarda la información adicional
                                val establishment = hashMapOf(
                                    "name" to companyName,
                                    "address" to selectedRegion,
                                    "coordinates" to coordinates,
                                    "phone" to phone,
                                    "userId" to uid,
                                    "likes" to 0 // Inicializa los likes en 0
                                )
                                val establishmentResult = userRepository.saveEstablishment(uid, establishment)
                                establishmentResult.fold(
                                    onSuccess = { navigateToLogin() },
                                    onFailure = { _errorMessage.value = "Cannot create establishment" }
                                )
                            } else {
                                navigateToLogin()
                            }
                        },
                        onFailure = { _errorMessage.value = "Cannot sign up" }
                    )
                },
                onFailure = { _errorMessage.value = "Error al crear el usuario" }
            )
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun navigateToLogin() {
        _navigationEvent.value = AppScreens.LoginScreen.route
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }
}

