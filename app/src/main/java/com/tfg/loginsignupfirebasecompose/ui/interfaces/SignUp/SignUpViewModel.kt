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
    var name: String by mutableStateOf("")
    var email: String by mutableStateOf("")
    var password: String by mutableStateOf("")
    var passwordHidden by mutableStateOf(true)
    var userType: String by mutableStateOf("")

    // Nuevos campos para dirección y teléfono para todos los usuarios
    var address: String by mutableStateOf("")
    var phone: String by mutableStateOf("")

    // Nuevos campos para la empresa
    var companyName: String by mutableStateOf("")
    var companyAddress: String by mutableStateOf("")
    var companyPhone: String by mutableStateOf("")

    var selectedRegion: String by mutableStateOf("")
    var latitud: String by mutableStateOf("")
    var longitud: String by mutableStateOf("")
    var coordinates: Pair<Double, Double> by mutableStateOf(Pair(0.0, 0.0))


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    fun signUp() {
        viewModelScope.launch {
            val result = authRepository.signUp(email, password)
            result.fold(
                onSuccess = { firebaseUser ->
                    val uid = firebaseUser.uid
                    val user = hashMapOf(
                        "name" to name,
                        "address" to address,
                        "phone" to phone,
                        "type" to userType,
                        "region" to selectedRegion,
                        "likedEstablishments" to arrayListOf<String>(),
                        "starred_dogs" to arrayListOf<String>(),
                        "sharedDogs" to arrayListOf<String>(),
                        "chat_rooms" to arrayListOf<String>(),
                        "profileImageUrl" to "",
                        "email" to email,
                        "dogs" to arrayListOf<String>()
                    )
                    val saveResult = userRepository.saveUser(uid, user)
                    saveResult.fold(
                        onSuccess = {
                            if (userType == "Enterprise") {
                                updateCoordinates()
                                val establishment = hashMapOf(
                                    "name" to companyName,
                                    "address" to companyAddress,
                                    "coordinates" to mapOf( // Guarda las coordenadas como un mapa
                                        "latitude" to coordinates.first,
                                        "longitude" to coordinates.second
                                    ),
                                    "phone" to phone,
                                    "owner_id" to uid,
                                    "likes" to 0 // Inicializa los likes en 0
                                )
                                val establishmentResult = userRepository.saveEstablishment(establishment)
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
    private fun updateCoordinates() {
        val lat = latitud.toDoubleOrNull() ?: 0.0 // Convierte a Double, si falla pone 0.0
        val lon = longitud.toDoubleOrNull() ?: 0.0
        coordinates = Pair(lat, lon)
    }
}

