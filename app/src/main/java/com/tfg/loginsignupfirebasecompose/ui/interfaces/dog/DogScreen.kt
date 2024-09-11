package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tfg.loginsignupfirebasecompose.ui.theme.AppTheme

@Composable
fun DogScreen(navController: NavController, viewModel: DogViewModel = hiltViewModel()) {
    val currentUser by viewModel.currentUser.collectAsState()

    val navigationEvent by viewModel.navigationEvent.collectAsState()

    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { route ->
            navController.navigate(route)
            viewModel.clearNavigationEvent()
        }
    }

    AppTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(text = "Dog Screen")
            Text(text = "Bienvenido, $currentUser")  // Muestra el nombre del usuario cuando se obtiene

            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier.padding(vertical = 8.dp),
            ) {
                Text(text = "Logout")
            }
        }
    }

}