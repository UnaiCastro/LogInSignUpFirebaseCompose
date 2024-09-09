package com.tfg.loginsignupfirebasecompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tfg.loginsignupfirebasecompose.data.AppScreens
import com.tfg.loginsignupfirebasecompose.interfaces.FirebaseComposeScreen
import com.tfg.loginsignupfirebasecompose.interfaces.LoginScreen
import com.tfg.loginsignupfirebasecompose.interfaces.SignUpScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.FirebaseComposeScreen.route){
        composable(route = AppScreens.FirebaseComposeScreen.route){
            FirebaseComposeScreen(navController)
        }
        composable(route = AppScreens.LoginScreen.route){
            LoginScreen(navController)
        }
        composable(route = AppScreens.SignUpScreen.route){
            SignUpScreen(navController)

        }

    }
}