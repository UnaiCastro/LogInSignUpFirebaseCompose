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
    private val userRepository: UserRepository
) : ViewModel() {
    var name: String by mutableStateOf("")
    var email: String by mutableStateOf("")
    var password: String by mutableStateOf("")
    var userType: String by mutableStateOf("")

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
                    if (!response.isSuccessful) throw IOException("Error en la solicitud: $response")

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
        viewModelScope.launch {
            val result = authRepository.signUp(email, password)
            result.fold(
                onSuccess = { firebaseUser ->
                    val uid = firebaseUser.uid
                    val profileImageUrl = getRandomDogImage()
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
                                    "phone" to phone,
                                    "owner_id" to uid,
                                    "likes" to emptyList<String>(),
                                    "establishmentImage" to "https://static.fundacion-affinity.org/cdn/farfuture/wA2M2WIdWb-muHHNOHC_YxBPkEhxd0F7uoyJs8MGjuE/mtime:1593587079/sites/default/files/impacto-del-confinamiento-protectoras-y-adopcion-de-animales.jpg",
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
        val lat = latitud.toDoubleOrNull() ?: 0.0
        val lon = longitud.toDoubleOrNull() ?: 0.0
        coordinates = Pair(lat, lon)
    }
}

