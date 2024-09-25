package com.tfg.loginsignupfirebasecompose.ui.interfaces.SignUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.compose.primaryLight

import com.tfg.loginsignupfirebasecompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController, viewModel: SignUpViewModel = hiltViewModel()) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var userType by remember { mutableStateOf("Particular") }
    var phone by remember { mutableStateOf("") }
    var selectedRegion by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var coordinates by remember { mutableStateOf(Pair("", "")) } // Latitude, Longitude
    val errorMessage by viewModel.errorMessage.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()

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
                Button(
                    onClick = { viewModel.clearErrorMessage() }
                ) {
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

            // Hacer que el contenido sea desplazable
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
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                )

                Text("Selecciona el tipo de usuario:")
                DropdownMenuUserType(userType, onUserTypeChange = { userType = it })

                TextField(
                    value = viewModel.name,
                    onValueChange = { viewModel.name = it },
                    label = { Text("Name") },
                    placeholder = { Text("Astrolopitecus") },
                    singleLine = true,
                    /*colors = textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    ),*/
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                TextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("Email") },
                    placeholder = { Text("example@gmail.com") },
                    singleLine = true,
                    /*colors = textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    ),*/
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Number phone") },
                    /*colors = textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    ),*/
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                DropdownMenuRegions(regions, selectedRegion, onRegionSelected = { selectedRegion = it })

                if (userType == "Empresa") {
                    // Campo adicional para nombre de la empresa
                    TextField(
                        value = companyName,
                        onValueChange = { companyName = it },
                        label = { Text("Nombre de la empresa") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    // Coordenadas de la empresa
                    CoordinatesInput(coordinates) { lat, lon ->
                        coordinates = Pair(lat, lon)
                    }
                }

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
                    /*colors = textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    ),*/
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )

                FilledTonalButton(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    onClick = {
                        if (userType == "Particular") {
                            viewModel.signUp()
                        } else {
                            // SignUp con empresa
                            // Aquí puedes añadir la lógica para manejar el registro de empresa
                        }
                    }
                ) {
                    Text("Sign up")
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuUserType(selectedType: String, onUserTypeChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val types = listOf("Particular", "Empresa")

    Box(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        TextField(
            value = selectedType,
            onValueChange = {},
            label = { Text("Tipo de Usuario") },
            readOnly = true,
            /*colors = textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),*/
            modifier = Modifier.clickable { expanded = true }.fillMaxWidth()
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            types.forEach { type ->
                DropdownMenuItem(
                    text = { Text(text = type) },
                    onClick = {
                        onUserTypeChange(type)
                        expanded = false
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuRegions(regions: List<String>, selectedRegion: String, onRegionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        TextField(
            value = selectedRegion,
            onValueChange = {},
            label = { Text("Comunidad Autónoma") },
            readOnly = true,
            /*colors = textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),*/
            modifier = Modifier.clickable { expanded = true }.fillMaxWidth()
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            regions.forEach { region ->
                DropdownMenuItem(
                    text = { Text(text = region)},
                    onClick = {
                        onRegionSelected(region)
                        expanded = false
                })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoordinatesInput(
    coordinates: Pair<String, String>,
    onCoordinatesChange: (String, String) -> Unit
) {
    Column(modifier = Modifier.padding(top = 8.dp)) {
        TextField(
            value = coordinates.first,
            onValueChange = { lat -> onCoordinatesChange(lat, coordinates.second) },
            label = { Text("Latitud") },
            /*colors = textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),*/
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        TextField(
            value = coordinates.second,
            onValueChange = { lon -> onCoordinatesChange(coordinates.first, lon) },
            label = { Text("Longitud") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
    }
}
