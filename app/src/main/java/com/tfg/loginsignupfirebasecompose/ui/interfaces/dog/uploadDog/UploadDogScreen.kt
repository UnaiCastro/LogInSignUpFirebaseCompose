package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.uploadDog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tfg.loginsignupfirebasecompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDogScreen(
    navController: NavHostController,
    viewModel: UploadDogViewModel = hiltViewModel()
) {
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

    Scaffold(
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }

    ) { contentPadding ->

        Image(
            painter = painterResource(id = R.drawable.background4),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.6f)
                .blur(4.dp)
        )

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
            TextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = { Text("Name") },
                maxLines = 1,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = { Text("Dog name") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            var expandedGender by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expandedGender,
                onExpandedChange = {
                    expandedGender = !expandedGender
                }
            ) {
                TextField(
                    value = viewModel.gender,
                    onValueChange = { viewModel.gender = it },
                    label = { Text("Gender") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender)
                    }
                )

                ExposedDropdownMenu(
                    expanded = expandedGender,
                    onDismissRequest = { expandedGender = false },
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
                TextField(
                    value = viewModel.breed,
                    onValueChange = { viewModel.breed = it },
                    label = { Text("Raza") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    ),
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

            TextField(
                value = viewModel.description,
                onValueChange = { viewModel.description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Dog description") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            TextField(
                value = viewModel.age,
                maxLines = 1,
                onValueChange = { viewModel.age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Dog age") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )

            )

            TextField(
                maxLines = 1,
                value = viewModel.price,
                onValueChange = { viewModel.price = it },
                label = { Text("Price") },
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
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                ),
                placeholder = { Text("Dog price") }

            )

            var expandedStatus by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedStatus,
                onExpandedChange = { expandedStatus = !expandedStatus }
            ) {
                TextField(
                    value = viewModel.status,
                    onValueChange = { viewModel.status = it },
                    label = { Text("Status") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus)
                    }
                )
                ExposedDropdownMenu(
                    expanded = expandedStatus,
                    onDismissRequest = { expandedStatus = false }
                ) {
                    val statusOptions = listOf("Adopt", "Buy")
                    statusOptions.forEach { status ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = status
                                )
                            },
                            onClick = {
                                viewModel.status = status
                                expandedStatus = false
                            }
                        )
                    }
                }
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
                    )
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upload Dog")
            }
        }

    }


}
