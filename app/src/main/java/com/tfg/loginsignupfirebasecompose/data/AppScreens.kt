package com.tfg.loginsignupfirebasecompose.data

sealed class AppScreens(val route: String ) {
    object FirebaseComposeScreen: AppScreens("firebase_compose_screen")
    object LoginScreen: AppScreens("login_screen")
    object SignUpScreen: AppScreens("sign_up_screen")
    object DogScreen: AppScreens("dog_screen")

}