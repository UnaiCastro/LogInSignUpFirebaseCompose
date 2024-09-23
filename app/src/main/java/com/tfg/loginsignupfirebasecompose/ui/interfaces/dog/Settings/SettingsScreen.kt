package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Settings

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.shape.CircleShape as CircleShape1

@Composable
fun SettingsScreen(navController: NavHostController, viewModel: SettingsViewModel = hiltViewModel()) {
    val name by viewModel.name.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val address by viewModel.address.collectAsState()
    val profileImageUrl by viewModel.profileImageUrl.collectAsState()
    val userType by viewModel.userType.collectAsState()


    val context = LocalContext.current

    // Launcher para tomar una foto con la cámara
    val launcherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            bitmap?.let {
                viewModel.uploadProfileImageFromCamera(it)
            }
        }
    )

    // Launcher para solicitar el permiso de la cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Si el permiso es concedido, abrir la cámara
                launcherCamera.launch()
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Launcher para seleccionar una imagen desde la galería
    val launcherGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModel.uploadProfileImage(it, context.contentResolver)
            }
        }
    )



    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
        }
        Text("Configuración", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen de perfil
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape1)
                .background(Color.LightGray)
                .clickable {
                    launcherGallery.launch("image/*") // Abre la galería
                },
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(profileImageUrl),
                    contentDescription = "Imagen de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(80.dp))
            }
        }

        Row {
            Button(onClick = { launcherGallery.launch("image/*") }) {
                Text("Seleccionar de la galería")
            }
            Spacer(modifier = Modifier.width(8.dp))
            /*Button(onClick = { launcherCamera.launch() }) {
                Text("Tomar foto")
            }*/
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de nombre
        TextField(
            value = name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de teléfono
        TextField(
            value = phone,
            onValueChange = { viewModel.updatePhone(it) },
            label = { Text("Número de teléfono") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de dirección
        TextField(
            value = address,
            onValueChange = { viewModel.updateAddress(it) },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Selección de tipo de usuario (Particular o Empresa)
        Row {
            Text("Tipo de usuario: ", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.width(8.dp))
            RadioButton(selected = userType == "Particular", onClick = { viewModel.updateUserType("Particular") })
            Text("Particular", modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = userType == "Empresa", onClick = { viewModel.updateUserType("Empresa") })
            Text("Empresa", modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}