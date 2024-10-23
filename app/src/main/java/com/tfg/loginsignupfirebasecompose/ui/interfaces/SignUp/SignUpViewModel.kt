package com.tfg.loginsignupfirebasecompose.ui.interfaces.SignUp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.loginsignupfirebasecompose.data.Firebase.AppScreens
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.EstablishmentRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val esta: EstablishmentRepository
) : ViewModel() {
    var name: String by mutableStateOf("")
    var email: String by mutableStateOf("")
    var password: String by mutableStateOf("")
    var userType: String by mutableStateOf("")

    var address: String by mutableStateOf("")
    var phone: String by mutableStateOf("")

    var companyName: String by mutableStateOf("")
    var companyAddress: String by mutableStateOf("")
    var companyPhone: String by mutableStateOf("")

    var selectedRegion: String by mutableStateOf("")
    var latitud: String by mutableStateOf("")
    var longitud: String by mutableStateOf("")
    var coordinates: Pair<Double, Double> by mutableStateOf(Pair(0.0, 0.0))

    var nameError: Boolean by mutableStateOf(false)
    var emailError: Boolean by mutableStateOf(false)
    var passwordError: Boolean by mutableStateOf(false)
    var addressError: Boolean by mutableStateOf(false)
    var phoneError: Boolean by mutableStateOf(false)
    var generalError: String? by mutableStateOf(null)

    private val client = OkHttpClient()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    private suspend fun getRandomDogImage(): String {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url("https://random.dog/woof.json")
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Error in requesting dog image: $response")

                    val responseBody = response.body?.string()
                    val jsonObject = JSONObject(responseBody)
                    jsonObject.getString("url")
                }
            } catch (e: Exception) {
                "https://example.com/default-dog-image.jpg"
            }
        }
    }

    fun signUp() {
        if (!validateForm()) {
            _errorMessage.value = generalError
            return
        }

        viewModelScope.launch {
            name = capitalizeName(name)

            val authResult = authRepository.signUp(email, password)
            authResult.fold(
                onSuccess = { firebaseUser ->
                    val uid = firebaseUser.uid
                    val profileImageUrl = getRandomDogImage()
                    val user = hashMapOf(
                        "name" to name,
                        "address" to address,
                        "phone" to phone,
                        "type" to userType,
                        "region" to selectedRegion,
                        "profileImageUrl" to profileImageUrl,
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
                                    "coordinates" to mapOf(
                                        "latitude" to coordinates.first,
                                        "longitude" to coordinates.second
                                    ),
                                    "phone" to companyPhone,
                                    "owner_id" to uid,
                                    "establishmentImage" to "https://img.freepik.com/vector-premium/tienda-mascotas-perro-gato-dibujos-animados-vector-icono-ilustracion_480044-815.jpg"
                                )
                                esta.saveEstablishment(establishment)
                            }
                            navigateToHome()
                        },
                        onFailure = {
                            _errorMessage.value = "Error saving user data"
                        }
                    )
                },
                onFailure = {
                    _errorMessage.value = "Error during sign-up. Email is used"
                }
            )
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun navigateToHome() {
        _navigationEvent.value = AppScreens.DogScreen.route
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    private fun updateCoordinates() {
        val lat = latitud.toDoubleOrNull() ?: 0.0
        val lon = longitud.toDoubleOrNull() ?: 0.0
        coordinates = Pair(lat, lon)
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Z]).{8,}$"
        return password.matches(passwordPattern.toRegex())
    }

    private fun capitalizeName(name: String): String {
        return name.lowercase().replaceFirstChar { it.uppercase() }
    }

    private fun validateForm(): Boolean {
        nameError = name.isBlank()
        emailError = !isEmailValid(email)
        passwordError = !isPasswordValid(password)
        addressError = address.isBlank()
        phoneError = phone.isBlank()

        generalError = when {
            nameError -> "Name is required"
            emailError -> "Invalid email format"
            passwordError -> "Password must be 8 characters long and contain a capital letter"
            addressError -> "Address is required"
            phoneError -> "Phone is required"
            else -> null
        }

        return !nameError && !emailError && !passwordError && !addressError && !phoneError
    }
}

