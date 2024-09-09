package com.tfg.loginsignupfirebasecompose.interfaces

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.compose.AppTheme

@Composable
fun LoginScreen(navController: NavController){
    AppTheme {
        Column {
            Text(text = "LoginScreen")
        }
    }

}