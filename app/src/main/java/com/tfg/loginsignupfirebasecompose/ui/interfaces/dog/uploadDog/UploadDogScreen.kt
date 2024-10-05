package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.uploadDog

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDogScreen(
    navController: NavHostController,
    viewModel: UploadDogViewModel = hiltViewModel()
) {
    val context = LocalContext.current
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
        if (currentUser.type == "empresa") listOf("Adopt", "Buy") else listOf("Adopt")

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> viewModel.imageUri = uri }
    )
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            bitmap?.let {
                // Convertir el Bitmap a Uri y asignarlo al viewModel
                val uri = saveBitmapToFile(context, bitmap)
                viewModel.imageUri = uri
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                takePictureLauncher.launch()
            } else {
                Log.d("UploadDogScreen", "Camera permission denied")
            }
        }
    )

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 35.dp),
                    ) {
                        Text("Upload Dog")
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }

    ) { contentPadding ->

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                // Sheet content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Choose an option", style = MaterialTheme.typography.titleMedium)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Option to pick photo from gallery
                    Button(
                        onClick = {
                            scope.launch {
                                sheetState.hide() // Hide the BottomSheet
                                launcher.launch("image/*") // Open gallery
                            }
                            showBottomSheet = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Choose from Gallery")
                    }

                    Spacer(modifier = Modifier.height(8.dp))


                    Button(
                        onClick = {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                takePictureLauncher.launch()
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                            showBottomSheet = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Take a Photo")
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .clickable { scope.launch { showBottomSheet = true } },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add photo",
                        modifier = Modifier.size(50.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "Resume photo",
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                }
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

            Button(
                onClick = {
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


}

fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri? {
        return try {
            val file = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
            file.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

}
