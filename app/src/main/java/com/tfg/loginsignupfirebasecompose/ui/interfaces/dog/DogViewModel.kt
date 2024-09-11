package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.data.AppScreens
import com.tfg.loginsignupfirebasecompose.data.FirestoreCollections
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/*@HiltViewModel
class DogViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    val uid: String by mutableStateOf(auth.currentUser?.uid ?: "")

    // StateFlow para el nombre del usuario
    private val _currentUser = MutableStateFlow<String>("")
    val currentUser: StateFlow<String> = _currentUser

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    init {
        if (uid.isNotEmpty()) {
            // Cargar el nombre del usuario desde Firestore cuando se inicializa el ViewModel
            getUserName()
        }
    }

    fun logout() {
        auth.signOut()
        _navigationEvent.value = AppScreens.FirebaseComposeScreen.route
        Log.i("Logout", "LogoutScreen: success")
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    // Función suspendida para obtener el nombre del usuario desde Firestore
    private fun getUserName() {
        viewModelScope.launch {
            val name = checkNameOfUser()
            _currentUser.value = name ?: "Usuario desconocido"
        }
    }

    // Función suspendida para obtener el nombre del usuario desde Firestore
    private suspend fun checkNameOfUser(): String? {
        return try {
            val document = db.collection(FirestoreCollections.users).document(uid).get().await()
            if (document.exists()) {
                document.getString("name").toString()
                Log.i("Firestore", "Nombre del usuario: ${document.getString("name")}")
                return document.getString("name").toString()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener el nombre del usuario", e)
            null
        }
    }
}*/
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
            _currentUser.value = (name ?: "Usuario desconocido").toString()
        }
    }
}

