package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.uploadDog

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDogScreen(
    navController: NavHostController,
    viewModel: UploadDogViewModel = hiltViewModel()
) {

    val currentUser by viewModel.currentUser.collectAsState()

    val breedOptions = listOf(
        "Beagle", "Bichon Frise", "Border Collie", "Boxer", "Bulldog",
        "Bullmastiff", "Cavalier King Charles Spaniel", "Chihuahua",
        "Cocker Spaniel", "Dachshund", "Dalmatian", "Doberman", "English Setter",
        "French Bulldog", "German Shepherd", "Golden Retriever", "Great Dane",
        "Greyhound", "Husky", "Jack Russell Terrier", "Labrador Retriever",
        "Maltese", "Mastiff", "Miniature Schnauzer", "Poodle", "Pomeranian",
        "Pug", "Rottweiler", "Shih Tzu", "Staffordshire Bull Terrier", "Yorkshire Terrier"
    ).sorted()

    val genderOptions = listOf("Male", "Female")
    val statusOptions =
        if (currentUser.type == "empresa") listOf("Adoptar", "Vender") else listOf("Adoptar")

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> viewModel.imageUri = uri }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Text(
            text = "Subir Perro",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )


        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current


        OutlinedTextField(
            value = viewModel.name,
            onValueChange = { viewModel.name = it },
            label = { Text("Nombre") },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            placeholder = { Text("Nombre del perro") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )

        var expandedGender by remember { mutableStateOf(false) }

        // ExposedDropdownMenuBox para el género
        ExposedDropdownMenuBox(
            expanded = expandedGender,
            onExpandedChange = {
                expandedGender = !expandedGender
            }
        ) {
            OutlinedTextField(
                value = viewModel.gender,
                onValueChange = { viewModel.gender = it },
                label = { Text("Género") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),  // Necesario para que el dropdown se alinee correctamente
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender)
                }
            )

            ExposedDropdownMenu(
                expanded = expandedGender,
                onDismissRequest = { expandedGender = false },
                /*modifier = Modifier.fillMaxWidth()*/
            ) {
                genderOptions.forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender) },
                        onClick = {
                            viewModel.gender = gender
                            expandedGender = false
                        }
                    )
                }
            }
        }

        var expandedBreed by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expandedBreed,
            onExpandedChange = { expandedBreed = !expandedBreed }
        ) {
            OutlinedTextField(
                value = viewModel.breed,
                onValueChange = { viewModel.breed = it },
                label = { Text("Raza") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedBreed)
                }
            )
            ExposedDropdownMenu(
                expanded = expandedBreed,
                onDismissRequest = { expandedBreed = false }
            ) {
                breedOptions.forEach { breed ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = breed
                            )
                        },
                        onClick = {
                            viewModel.breed = breed
                            expandedBreed = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = viewModel.description,
            onValueChange = { viewModel.description = it },
            label = { Text("Descripcion") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Descripcion del perro") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )

        OutlinedTextField(
            value = viewModel.age,
            maxLines = 1,
            onValueChange = { viewModel.age = it },
            label = { Text("Edad") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Edad del perro") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )

        )

        OutlinedTextField(
            maxLines = 1,
            value = viewModel.price,
            onValueChange = { viewModel.price = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.clearFocus()
                }
            ),
            placeholder = { Text("Precio del perro") }

        )

        /*if (currentUser.type == "empresa") {
            OutlinedTextField(
                value = viewModel.status,
                maxLines = 1,
                readOnly = true,
                onValueChange = { viewModel.status = it },
                label = { Text("Status") },
                modifier = Modifier.fillMaxWidth()
                    .clickable { expandedStatus = true },
                placeholder = { Text("Seleccione el status") },
                trailingIcon = {
                    Icon(
                        if (expandedStatus) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Toggle Dropdown",
                        Modifier.clickable { expandedStatus = !expandedStatus }
                    )
                }
            )
            DropdownMenu(
                expanded = expandedStatus,
                onDismissRequest = { expandedStatus = false }
            ) {
                statusOptions.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(text = status) },
                        onClick = {
                            viewModel.status = status
                            expandedStatus = false
                        }
                    )
                }
            }
        }*/

        // Subir foto
        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text(text = if (viewModel.imageUri == null) "Subir Foto" else "Cambiar Foto")
        }

        viewModel.imageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = "Dog Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // Botón para subir perro
        Button(
            onClick = {
                // Lógica para subir los datos del perro
                viewModel.uploadDog(
                    name = viewModel.name,
                    gender = viewModel.gender,
                    breed = viewModel.breed,
                    description = viewModel.description,
                    age = viewModel.age.toInt(),
                    price = viewModel.price.toInt(),
                    status = viewModel.status,
                    imageUri = viewModel.imageUri
                )
                navController.navigateUp()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Subir Perro")
        }
    }
}
