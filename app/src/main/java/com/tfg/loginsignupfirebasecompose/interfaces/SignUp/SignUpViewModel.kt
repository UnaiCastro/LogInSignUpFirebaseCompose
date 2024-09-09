package com.tfg.loginsignupfirebasecompose.interfaces.SignUp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    var email: String by mutableStateOf("")
    var password: String by mutableStateOf("")


    fun signUp() {
        // Lógica para registrarse
        Log.i("SignUp", "SignUpScreen: $email")
        Log.i("SignUp", "SignUpScreen: $password")
        // auth.createUserWithEmailAndPassword(...) o cualquier otra lógica de registro
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.i("SignUp", "SignUpScreen: success")
                Log.i("SignUp", "SignUpScreen: $email and $password")
            }
            .addOnFailureListener {
                Log.i("SignUp", "SignUpScreen: failure")
                throw RuntimeException("Error")
            }
            .addOnCompleteListener {
                Log.i("SignUp", "SignUpScreen: complete")
            }
    }
}
