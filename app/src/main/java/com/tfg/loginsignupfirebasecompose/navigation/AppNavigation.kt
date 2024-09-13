package com.tfg.loginsignupfirebasecompose.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.tfg.loginsignupfirebasecompose.data.AppScreens
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Explore.ExploreScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.FirebaseCompose.FirebaseComposeScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.Login.LoginScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.ProfileScreen.ProfileScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.SignUp.SignUpScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.StarScreen.StarredScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.DogScreen

@Composable
fun AppNavigation(){
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val navController = rememberNavController()


    LaunchedEffect(currentUser) {
        Log.i("Navigation", "Current User: ${currentUser}")
        if (currentUser != null) {
            navController.navigate(AppScreens.DogScreen.route) {
                popUpTo(AppScreens.FirebaseComposeScreen.route) { inclusive = true }
            }
        } else {
            navController.navigate(AppScreens.FirebaseComposeScreen.route)
        }
    }

    NavHost(navController = navController, startDestination = AppScreens.FirebaseComposeScreen.route) {
        composable(route = AppScreens.FirebaseComposeScreen.route) {
            FirebaseComposeScreen(navController)
        }
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = AppScreens.SignUpScreen.route) {
            SignUpScreen(navController)
        }
        composable(route = AppScreens.DogScreen.route) {
            DogScreen(navController)
        }

    }
}