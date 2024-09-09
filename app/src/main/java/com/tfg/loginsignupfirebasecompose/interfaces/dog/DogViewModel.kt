package com.tfg.loginsignupfirebasecompose.interfaces.dog

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.tfg.loginsignupfirebasecompose.data.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DogViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    fun logout() {
        auth.signOut()
        _navigationEvent.value = AppScreens.FirebaseComposeScreen.route
        Log.i("Logout", "LogoutScreen: success")
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }
}