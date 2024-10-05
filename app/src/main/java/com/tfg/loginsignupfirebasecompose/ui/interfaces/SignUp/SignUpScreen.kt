package com.tfg.loginsignupfirebasecompose.ui.interfaces.SignUp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.tfg.loginsignupfirebasecompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController, viewModel: SignUpViewModel = hiltViewModel()) {
    var userType by remember { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    val errorMessage by viewModel.errorMessage.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    val types = listOf("Particular", "Enterprise")

    var expandedCities by remember { mutableStateOf(false) }
    val regions = listOf(
        "Andalucía", "Cataluña", "Madrid", "Valencia", "Galicia",
        "Asturias", "Murcia", "Navarra", "Cantabria", "País Vasco",
        "La Rioja", "Ceuta", "Melilla", "Aragón", "Castilla y León",
        "Castilla la Mancha", "Extremadura"
    )

    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { route ->
            navController.navigate(route)
            viewModel.clearNavigationEvent()
        }
    }

    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearErrorMessage() },
            title = { Text("Error") },
            text = { Text(errorMessage ?: "") },
            confirmButton = {
                Button(onClick = { viewModel.clearErrorMessage() }) {
                    Text("OK")
                }
            }
        )
    }

    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // Ajusta el fondo si es necesario
        ) {
            Image(
                painter = painterResource(id = R.drawable.backgroundwelcome),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .width(320.dp)
                        .height(240.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "Create an account in few minutes",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .fillMaxWidth()
                )

                Text(
                    text = "Selecciona el tipo de usuario:",
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = Color.White,
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = viewModel.userType,
                        onValueChange = { viewModel.userType = it },
                        label = { Text("Type of person") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        types.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(text = type) },
                                onClick = {
                                    viewModel.userType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Condición para mostrar la sección de información del usuario
                if (viewModel.userType.isNotEmpty()) {
                    HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(top = 16.dp))

                    Text(
                        text = "User Information",
                        modifier = Modifier.padding(vertical = 16.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    TextField(
                        value = viewModel.name,
                        onValueChange = { viewModel.name = it },
                        label = { Text("Name") },
                        placeholder = { Text("Astrolopitecus") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    // Email
                    TextField(
                        value = viewModel.email,
                        onValueChange = { viewModel.email = it },
                        label = { Text("Email") },
                        placeholder = { Text("example@gmail.com") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    // Teléfono
                    TextField(
                        value = viewModel.phone,
                        onValueChange = { viewModel.phone = it },
                        label = { Text("Number phone") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    // Región
                    ExposedDropdownMenuBox(
                        expanded = expandedCities,
                        onExpandedChange = { expandedCities = !expandedCities }
                    ) {
                        TextField(
                            value = viewModel.selectedRegion,
                            onValueChange = { viewModel.selectedRegion = it },
                            label = { Text("Region") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .padding(top = 8.dp),
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCities)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedCities,
                            onDismissRequest = { expandedCities = false }
                        ) {
                            regions.forEach { region ->
                                DropdownMenuItem(
                                    text = { Text(text = region) },
                                    onClick = {
                                        viewModel.selectedRegion = region
                                        expandedCities = false
                                    }
                                )
                            }
                        }
                    }

                    TextField(
                        value = viewModel.address,
                        onValueChange = { viewModel.address = it },
                        label = { Text("Your Address") },
                        placeholder = { Text("Calle falsa 123") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    TextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.password = it },
                        singleLine = true,
                        label = { Text("Password") },
                        placeholder = { Text("") },
                        visualTransformation =
                        if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordHidden = !passwordHidden
                            }) {
                                val visibilityIcon =
                                    if (passwordHidden) Icons.Filled.Lock else Icons.Filled.Lock
                                val description =
                                    if (passwordHidden) "Show password" else "Hide password"
                                Icon(imageVector = visibilityIcon, contentDescription = description)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )

                    // Condición para mostrar la sección de información de la empresa
                    if (viewModel.userType == "Enterprise") {
                        HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(top = 16.dp))

                        Text(
                            text = "Company Information",
                            modifier = Modifier.padding(vertical = 16.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        // Nombre de la compañía
                        TextField(
                            value = viewModel.companyName,
                            onValueChange = { viewModel.companyName = it },
                            label = { Text("Nombre de la empresa") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                        )

                        TextField(
                            value = viewModel.companyPhone,
                            onValueChange = { viewModel.companyPhone = it },
                            label = { Text("Phone") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )

                        TextField(
                            value = viewModel.companyAddress,
                            onValueChange = { viewModel.companyAddress = it },
                            label = { Text("Company Address") },
                            placeholder = { Text("Company address") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                        )

                        TextField(
                            value = viewModel.latitud,
                            onValueChange = { viewModel.latitud = it },
                            label = { Text("Latitude") },
                            placeholder = { Text("Latitude") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                        )

                        TextField(
                            value = viewModel.longitud,
                            onValueChange = { viewModel.longitud = it },
                            label = { Text("Longitude") },
                            placeholder = { Text("Longitude") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                        )
                    }

                    // Botón de Sign Up
                    FilledTonalButton(
                        modifier = Modifier
                            .padding(top = 26.dp)
                            .fillMaxWidth(),
                        onClick = {
                            viewModel.signUp()
                        }
                    ) {
                        Text("Sign up")
                    }
                }

                HorizontalDivider(thickness = 2.dp)

                TextButton(
                    onClick = {
                        viewModel.navigateToLogin()
                    },
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .padding(top = 8.dp)
                ) {
                    Text(
                        "¿Don´t have an account? Sign in",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}


private fun openGoogleMaps(context: Context) {
    val gmmIntentUri = Uri.parse("geo:0,0?q=") // Abre Google Maps en una ubicación general
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")

    // Verifica que haya una aplicación que pueda manejar el Intent
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    }
}



