package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.SharedScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.tfg.loginsignupfirebasecompose.R
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedScreen(navController: NavHostController, viewModel: SharedViewModel = hiltViewModel()) {
    val dogsShared by viewModel.dogsShared.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Shared Dogs") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                )
            )
        }
    ) { padding ->
        Image(
            painter = painterResource(id = R.drawable.background4),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.6f)
                .blur(4.dp)
        )
        if (dogsShared.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                items(dogsShared) { dog ->
                    DogItem(
                        dog = dog,
                        onRemoveDog = { viewModel.removeDogFromShared(dog.dogId) }
                    )
                }
            }
        } else {

        }
    }
}

@Composable
fun DogItem(dog: Dog, onRemoveDog: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            dog.profileImageUrl?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Image of ${dog.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()) {
                Text(text = dog.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Breed: ${dog.breed}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Age: ${dog.age} years", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(6.dp))

                IconButton(onClick = onRemoveDog, modifier = Modifier.align(Alignment.End)) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Remove from Shared",
                        tint = Color.Green
                    )
                }
            }
        }
    }
}