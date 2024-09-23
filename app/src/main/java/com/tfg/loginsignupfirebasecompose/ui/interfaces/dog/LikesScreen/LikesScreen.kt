package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.LikesScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun LikesScreen(navController: NavHostController, viewModel: LikesViewModel = hiltViewModel()) {
    IconButton(
        onClick = { navController.popBackStack() },
        modifier = Modifier
            .padding(16.dp)
    ) {
        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
    }
    Text(text = "Likes Screen")
}