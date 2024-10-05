package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Settings

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.compose.primaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val address by viewModel.address.collectAsState()
    val profileImageUrl by viewModel.profileImageUrl.collectAsState()
    val userType by viewModel.userType.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var showBusinessDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    var businessName by remember { mutableStateOf("") }
    var businessPhone by remember { mutableStateOf("") }
    var businessAddress by remember { mutableStateOf("") }

    fun showBusinessDialog() {
        showBusinessDialog = true
    }

    fun showConfirmDialog() {
        showConfirmDialog = true
    }

    val autonomousCommunities = listOf(
        "Andalucía" to Pair(37.3891, -5.9845), // Sevilla
        "Aragón" to Pair(41.6488, -0.8891), // Zaragoza
        "Asturias" to Pair(43.3619, -5.8494), // Oviedo
        "Baleares" to Pair(39.5696, 2.6502), // Palma de Mallorca
        "Canarias" to Pair(28.1235, -15.4363), // Las Palmas de Gran Canaria
        "Cantabria" to Pair(43.4623, -3.8099), // Santander
        "Castilla-La Mancha" to Pair(39.8628, -4.0273), // Toledo
        "Castilla y León" to Pair(41.6529, -4.7286), // Valladolid
        "Cataluña" to Pair(41.3851, 2.1734), // Barcelona
        "Comunidad Valenciana" to Pair(39.4699, -0.3763), // Valencia
        "Extremadura" to Pair(39.4750, -6.3725), // Mérida
        "Galicia" to Pair(42.8804, -8.5457), // Santiago de Compostela
        "Madrid" to Pair(40.4168, -3.7038), // Madrid
        "Murcia" to Pair(37.9922, -1.1307), // Murcia
        "Navarra" to Pair(42.8125, -1.6458), // Pamplona
        "País Vasco" to Pair(43.2630, -2.9350), // Bilbao
        "La Rioja" to Pair(42.4650, -2.4487), // Logroño
        "Ceuta" to Pair(35.8894, -5.3198), // Ceuta
        "Melilla" to Pair(35.2923, -2.9381) // Melilla
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 35.dp), // Compensamos el espacio del icono de la izquierda

                        contentAlignment = Alignment.Center
                    ) {
                        Text("Settings")
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },

                        ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Aquí agregamos el padding
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally, // Centra el contenido horizontalmente

        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.size(120.dp), // Tamaño de la imagen
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(profileImageUrl),
                        contentDescription = "Imagen de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de nombre
            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Nombre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedPlaceholderColor = Color.Transparent,
                    unfocusedPlaceholderColor = Color.Transparent,
                    cursorColor = primaryLight
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de teléfono
            OutlinedTextField(
                value = phone,
                onValueChange = { viewModel.updatePhone(it) },
                label = { Text("Número de teléfono") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Phone
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                modifier = Modifier.fillMaxWidth(),

                )

            Spacer(modifier = Modifier.height(8.dp))

            var expanded by remember { mutableStateOf(false) }
            var selectedCommunity by remember { mutableStateOf("") }
            var coordinates by remember { mutableStateOf<Pair<Double, Double>?>(null) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    value = selectedCommunity,
                    onValueChange = { },
                    label = { Text("Comunidad Autónoma") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    autonomousCommunities.forEach { (community, coord) -> // Usamos desestructuración
                        DropdownMenuItem(
                            text = { Text(community) },
                            onClick = {
                                selectedCommunity = community
                                coordinates =
                                    coord // Guardar las coordenadas de la comunidad
                                expanded = false

                                // Guardar automáticamente en el ViewModel
                                viewModel.updateCommunityInfo(
                                    selectedCommunity,
                                    coordinates
                                )
                            }
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = address,
                onValueChange = { viewModel.updateAddress(it) },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.clearFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Selección de tipo de usuario (Particular o Empresa)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tipo de usuario: ", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(8.dp))
                RadioButton(selected = userType == "Particular", onClick = {
                    if (userType == "Empresa") {
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
                                viewModel.saveBusinessInfo(
                                    businessName,
                                    businessAddress,
                                    businessPhone
                                )
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
}
