package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.uploadDog

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter

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

    val genderOptions = listOf("Macho", "Hembra")
    val statusOptions =
        if (currentUser.type == "empresa") listOf("Adoptar", "Vender") else listOf("Adoptar")

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> viewModel.imageUri = uri }
    )
    val focusManager = LocalFocusManager.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .clickable { focusManager.clearFocus() },
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Button(
            onClick = { navController.navigate("pruebamenu") },
            modifier = Modifier
                .padding(16.dp)
        ) {

        }
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Text(
            text = "Subir Perro",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )


        // Nombre
        OutlinedTextField(
            value = viewModel.name,
            onValueChange = { viewModel.name = it },
            label = { Text("Nombre") },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Nombre del perro") },

            )

        OutlinedTextField(
            value = viewModel.gender,
            onValueChange = { viewModel.gender = it },
            label = { Text("Genero") },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Genero del perro") }
        )
        OutlinedTextField(
            value = viewModel.breed,
            onValueChange = { viewModel.breed = it },
            label = { Text("Raza") },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Raza del perro") }
        )
        OutlinedTextField(
            value = viewModel.description,
            onValueChange = { viewModel.description = it },
            label = { Text("Descripcion") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Descripcion del perro") }
        )

        OutlinedTextField(
            value = viewModel.age,
            maxLines = 1,
            onValueChange = { viewModel.age = it },
            label = { Text("Edad") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text("Edad del perro") }
        )

        OutlinedTextField(
            maxLines = 1,
            value = viewModel.price,
            onValueChange = { viewModel.price = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text("Precio del perro") }
        )

        OutlinedTextField(
            value = viewModel.status,
            maxLines = 1,
            onValueChange = { viewModel.status = it },
            label = { Text("Status") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Status del perro") }
        )

        // Subir foto
        Button(onClick = { launcher.launch("image/*") }) {
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
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Subir Perro")
        }
    }
}
