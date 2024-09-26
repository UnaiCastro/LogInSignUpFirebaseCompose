package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.ProfileScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.compose.errorLightHighContrast
import com.tfg.loginsignupfirebasecompose.R
import com.tfg.loginsignupfirebasecompose.data.Firebase.AppScreens


@Composable
fun ProfileScreen(navController: NavController, innerNavController: NavHostController, viewModel: ProfileViewModel = hiltViewModel()) {

    val currentUser by viewModel.currentUser.collectAsState()
    val email by viewModel.email.collectAsState()
    val profileImageUrl by remember { viewModel.profileImageUrl }
    val navigationEvent by viewModel.navigationEvent.collectAsState()


    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { route ->
            // Navegación interna o externa según el evento
            navController.navigate(route) {
                popUpTo(AppScreens.DogScreen.route) { inclusive = true }
            }
            viewModel.clearNavigationEvent()
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                modifier = Modifier.padding(end = 16.dp),
                onClick = { innerNavController.navigate("chatroom") })
            {
                Icon(
                    painter = painterResource(id = R.drawable.ic_message),
                    contentDescription = "Chat Icon"
                )
                Text(text = "Chat")

            }
        }
        profileImageUrl?.let { url ->
            Image(
                painter = rememberImagePainter(data = url),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .clip(CircleShape)
                    .width(150.dp)
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
        } ?: run {
            // Imagen de reserva si la URL es nula
            Image(
                painter = painterResource(id = R.drawable.perrosonriente),
                contentDescription = "Default Profile Image",
                modifier = Modifier
                    .clip(CircleShape)
                    .width(150.dp)
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Welcome, $currentUser",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = email,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Primera Card
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            onClick = { innerNavController.navigate("settings")}
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Settings Icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                )
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Segunda Card
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            onClick = { innerNavController.navigate("shared") }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Shared Icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                )
                Text(
                    text = "Shared",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

        }
        Spacer(modifier = Modifier.height(8.dp))
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            onClick = { innerNavController.navigate("likes") }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Shared Icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                )
                Text(
                    text = "Establishments Likes",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

        }
        Spacer(modifier = Modifier.height(8.dp))
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            onClick = { innerNavController.navigate("mydogs") }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Shared Icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                )
                Text(
                    text = "My dogs",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

        }
        TextButton(onClick = { viewModel.logout() }) {
            Text(
                modifier = Modifier
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center,
                text = "Log Out",
                style = MaterialTheme.typography.bodyMedium,
                /*onTextLayout = { viewModel.logout()},*/
                color = errorLightHighContrast,
            )
        }

    }
    // Mostrar la imagen de perfil


}

