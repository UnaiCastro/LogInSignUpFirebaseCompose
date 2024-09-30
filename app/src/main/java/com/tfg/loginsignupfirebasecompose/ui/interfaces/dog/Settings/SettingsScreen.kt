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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    // Estado para controlar la visibilidad del diálogo para el establecimiento
    var showBusinessDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Variables para almacenar temporalmente los datos del establecimiento
    var businessName by remember { mutableStateOf("") }
    var businessPhone by remember { mutableStateOf("") }
    var businessAddress by remember { mutableStateOf("") }

    // Función para mostrar el diálogo de establecimiento
    fun showBusinessDialog() {
        showBusinessDialog = true
    }

    // Función para mostrar el diálogo de confirmación
    fun showConfirmDialog() {
        showConfirmDialog = true
    }

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
        Box {
            if (profileImageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(profileImageUrl),
                    contentDescription = "Imagen de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp)
                )
            } else {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(80.dp))
            }
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
            RadioButton(selected = userType == "Particular", onClick = {
                if (userType == "Empresa") {
                    // Si cambia de Empresa a Particular, mostrar el diálogo de confirmación
                    showConfirmDialog()
                } else {
                    viewModel.updateUserType("Particular")
                }
            })
            Text("Particular", modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = userType == "Empresa", onClick = {
                if (userType == "Particular") {
                    // Si cambia de Particular a Empresa, mostrar el diálogo del establecimiento
                    showBusinessDialog()
                } else {
                    viewModel.updateUserType("Empresa")
                }
            })
            Text("Empresa", modifier = Modifier.align(Alignment.CenterVertically))
        }

        // Diálogo para el establecimiento (Particular a Empresa)
        if (showBusinessDialog) {
            AlertDialog(
                onDismissRequest = { showBusinessDialog = false },
                title = { Text("Información del establecimiento") },
                text = {
                    Column {
                        TextField(
                            value = businessName,
                            onValueChange = { businessName = it },
                            label = { Text("Nombre del establecimiento") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = businessAddress,
                            onValueChange = { businessAddress = it },
                            label = { Text("Dirección del establecimiento") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = businessPhone,
                            onValueChange = { businessPhone = it },
                            label = { Text("Teléfono del establecimiento") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.updateUserType("Empresa")
                            viewModel.saveBusinessInfo(businessName, businessAddress, businessPhone)
                            showBusinessDialog = false
                        }
                    ) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showBusinessDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // Diálogo de confirmación (Empresa a Particular)
        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text("Confirmación") },
                text = { Text("¿Está seguro que quiere cambiar a particular? Esto eliminará el establecimiento asociado.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteBusinessInfo()
                            viewModel.updateUserType("Particular")
                            showConfirmDialog = false
                        }
                    ) {
                        Text("Sí, cambiar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showConfirmDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}