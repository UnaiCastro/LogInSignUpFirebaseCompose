package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.StarScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.compose.backgroundLight
import com.example.compose.onBackgroundLight
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog


@Composable
fun StarredScreen(navController: NavHostController, viewModel: StarViewModel = hiltViewModel()) {
    val starredDogs by viewModel.starredDogs.collectAsState()

    // Fondo decorativo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight), // Gradiente suave

    ) {
        // Encabezado decorativo
        Column {
            Text(
                text = "Tus Perros Guardados 🐾",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (starredDogs.isEmpty()) {
                // Texto decorativo si no hay perros guardados
                Text(
                    text = "No tienes perros guardados todavía.",
                    style = MaterialTheme.typography.bodyLarge.copy(color = onBackgroundLight),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            } else {
                // LazyColumn para mostrar los perros guardados
                LazyColumn(

                ) {
                    items(starredDogs) { dog ->
                        DogCardStar(
                            dog = dog,
                            viewModel=viewModel
                            )
                    }
                }
            }
        }
    }
}

@Composable
fun DogCardStar(dog: Dog, viewModel: StarViewModel) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ){
        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = dog.name)
            Text(text = dog.breed)
            Text(text = dog.age.toString())
            Text(text = dog.gender)
            Text(text = dog.description)
            Image(
                painter = rememberAsyncImagePainter(dog.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = {viewModel.toggleStarredDog(dog)},
            ){
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Compartir",
                    tint = Color.Black
                )
            }
        }
    }
}
