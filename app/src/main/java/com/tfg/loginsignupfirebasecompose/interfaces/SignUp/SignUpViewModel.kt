package com.tfg.loginsignupfirebasecompose.interfaces.SignUp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.data.AppScreens
import com.tfg.loginsignupfirebasecompose.data.FirestoreCollections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore

) : ViewModel() {

    var name: String by mutableStateOf("")
    var email: String by mutableStateOf("")
    var password: String by mutableStateOf("")
    var passwordHidden by mutableStateOf(true)

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent



    fun signUp() {

        Log.i("SignUp", "SignUpScreen: $email")
        Log.i("SignUp", "SignUpScreen: $password")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                Log.i("SignUp", "SignUpScreen: success")
                Log.i("SignUp", "SignUpScreen: $email and $password")

                val uid = authResult.user?.uid

                val user = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "dogs" to arrayListOf<String>()
                )

                if (uid != null) {
                    firestore.collection(FirestoreCollections.users).document(uid).set(user)
                        .addOnSuccessListener {
                            Log.i("SignUp", "SignUpScreen: sucess information added")
                            Log.i("SignUp", "SignUpScreen: $email and $password and $name")
                            navigateToLogin()
                        }
                        .addOnFailureListener {
                            _errorMessage.value = "Cannot sign up"

                        }
                }
            }
            .addOnFailureListener {
                Log.i("SignUp", "SignUpScreen: failure")
            }

            .addOnCompleteListener {
                Log.i("SignUp", "SignUpScreen: complete")
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
