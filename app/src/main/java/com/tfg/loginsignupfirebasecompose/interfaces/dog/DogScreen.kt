package com.tfg.loginsignupfirebasecompose.interfaces.dog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compose.AppTheme

@Composable
fun DogScreen(navController: NavController, viewModel: DogViewModel = hiltViewModel()) {

    val navigationEvent by viewModel.navigationEvent.collectAsState()

    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { route ->
            navController.navigate(route)
            viewModel.clearNavigationEvent()
        }
    }

    AppTheme {
        // Contenido de la pantalla Dog
        Column (modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(text = "Dog Screen")
            Button(
                onClick = {viewModel.logout()},
                modifier = Modifier.padding(vertical = 8.dp),
            ) {
                Text(text = "Logout")
            }
        }
    }

}