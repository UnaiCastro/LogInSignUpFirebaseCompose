package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.ProfileScreen

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.compose.onErrorContainerLight
import com.example.compose.primaryContainerLightHighContrast
import com.tfg.loginsignupfirebasecompose.R
import com.tfg.loginsignupfirebasecompose.data.Firebase.AppScreens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    innerNavController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val currentUser by viewModel.currentUser.collectAsState()
    val email by viewModel.email.collectAsState()
    val adress by viewModel.adress.collectAsState()
    val profileImageUrl by remember { viewModel.profileImageUrl }
    val navigationEvent by viewModel.navigationEvent.collectAsState()

    val scrollState = rememberScrollState()

    var photoUri by remember { mutableStateOf<Uri?>(null) }



    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { route ->
            // Navegación interna o externa según el evento
            navController.navigate(route) {
                popUpTo(AppScreens.DogScreen.route) { inclusive = true }
            }
            viewModel.clearNavigationEvent()
        }
    }

    val context = LocalContext.current
    val changeImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                viewModel.changeProfileImage(context, it)
            }
        }
    )

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success: Boolean ->
            if (success) {
                photoUri?.let { uri ->
                    viewModel.changeProfileImage(context, uri)
                }
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 32.dp, paddingValues.calculateTopPadding()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Imagen de perfil
            Box(
                modifier = Modifier
                    .size(150.dp)
            ) {
                // Imagen de perfil
                profileImageUrl?.let { url ->
                    Image(
                        painter = rememberImagePainter(data = url),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } ?: run {
                    Image(
                        painter = painterResource(id = R.drawable.perrosonriente),
                        contentDescription = "Default Profile Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                // Ícono de lápiz para cambiar la imagen
                IconButton(
                    onClick = {
                        // Aquí puedes llamar a la función para cambiar la imagen
                        showImageSelectionDialog(
                            context,
                            changeImageLauncher,
                            takePictureLauncher,
                            photoUri
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(36.dp)
                        .padding(4.dp), // Ajusta el padding según lo necesites
                    content = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile Image",
                            tint = primaryContainerLightHighContrast

                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Welcome $currentUser",
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp) // Tamaño del icono
                        .clip(CircleShape), // Espacio entre el icono y el texto
                    tint = Color.Unspecified
                )

                // Texto justo a la derecha del icono
                Text(
                    text = adress, // Cambia `adress` por `address` si ese es el nombre correcto
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 8.dp) // Espacio entre el icono y el texto
                )
            }



            Spacer(modifier = Modifier.height(32.dp))

            // Fila para Settings
            ProfileRowItem(
                iconResId = R.drawable.ic_settings,
                text = "Settings",
                onClick = { innerNavController.navigate("settings") }
            )

            ProfileRowItem(
                iconResId = R.drawable.ic_messages,
                text = "Chats",
                onClick = { innerNavController.navigate("chatroom") }
            )

            // Fila para Shared
            ProfileRowItem(
                iconResId = R.drawable.ic_compartir,
                text = "Shared",
                onClick = { innerNavController.navigate("shared") }
            )

            // Fila para Establishments Likes
            ProfileRowItem(
                iconResId = R.drawable.ic_megusta,
                text = "Establishments Likes",
                onClick = { innerNavController.navigate("likes") }
            )

            // Fila para My Dogs
            ProfileRowItem(
                iconResId = R.drawable.ic_mydogs,
                text = "My dogs",
                onClick = { innerNavController.navigate("mydogs") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.logout()

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = onErrorContainerLight, // Color de fondo inicial del botón
                    contentColor = Color.White, // Color de las letras en el estado inicial
                ),
                border = BorderStroke(
                    1.dp,
                    onErrorContainerLight
                ), // Borde con el mismo rojo que el fondo
                modifier = Modifier.padding(vertical = 8.dp),
            ) {
                Text(
                    text = "Log Out",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

private fun showImageSelectionDialog(
    context: Context,
    changeImageLauncher: ManagedActivityResultLauncher<String, Uri?>,
    takePictureLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    photoUri: Uri?
) {
    val options = arrayOf("Tomar Foto", "Seleccionar de Galería")
    AlertDialog.Builder(context).apply {
        setTitle("Cambiar imagen de perfil")
        setItems(options) { _, which ->
            when (which) {
                0 -> {
                    // Tomar foto
                    if (photoUri != null) {
                        takePictureLauncher.launch(photoUri)
                    } else {
                        Toast.makeText(
                            context,
                            "Error al crear el archivo para la imagen",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                1 -> {
                    // Seleccionar de galería
                    changeImageLauncher.launch("image/*")
                }
            }
        }
        create().show()
    }
}

@Composable
fun ProfileRowItem(
    iconResId: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                tint = Color.Unspecified

            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text, style = MaterialTheme.typography.titleLarge)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null
        )
    }
}

