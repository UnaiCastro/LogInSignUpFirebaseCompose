package com.tfg.loginsignupfirebasecompose.interfaces

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.tfg.loginsignupfirebasecompose.Greeting
import com.tfg.loginsignupfirebasecompose.R
import com.tfg.loginsignupfirebasecompose.data.AppScreens
import com.tfg.loginsignupfirebasecompose.navigation.AppNavigation
import java.lang.RuntimeException

@Composable
fun FirebaseComposeScreen(navController: NavController){
    AppTheme {
        Column(modifier = Modifier.padding(horizontal = 32.dp)) {
            Text(
                modifier = Modifier.padding(vertical = 64.dp),
                text = "Hola",
                fontSize = 32.sp
            )
            OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = { navController.navigate(route = AppScreens.SignUpScreen.route) }) {
                Text(
                    text = "Sign up for free"
                )
            }
            OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = { navController.navigate(route = AppScreens.LoginScreen.route) }) {
                Text(
                    text = "Log In"
                )
            }
        }

    }
}