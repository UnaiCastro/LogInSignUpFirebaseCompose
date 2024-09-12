package com.tfg.loginsignupfirebasecompose.data

import com.tfg.loginsignupfirebasecompose.R

sealed class AppScreens(val route: String) {
    object FirebaseComposeScreen : AppScreens("firebase_compose")
    object LoginScreen : AppScreens("login")
    object SignUpScreen : AppScreens("signup")
    object DogScreen : AppScreens("dog")
    object ExploreScreen : AppScreens("explore")
    object StarredScreen : AppScreens("starred")
    object ProfileScreen : AppScreens("profile")
}

