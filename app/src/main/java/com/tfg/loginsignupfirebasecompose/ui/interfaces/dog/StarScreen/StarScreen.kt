package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.StarScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.compose.backgroundLight
import com.example.compose.onBackgroundLight
import com.example.compose.onSurfaceLight
import com.tfg.loginsignupfirebasecompose.R
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog


@Composable
fun StarredScreen(navController: NavHostController, viewModel: StarViewModel = hiltViewModel()) {
    val starredDogs by viewModel.starredDogs.collectAsState()

    // Fondo decorativo
    Box(
        modifier = Modifier
            .fillMaxSize(),

    ) {

        Image(
            painter = painterResource(id = R.drawable.background4),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize().alpha(0.6f).blur(4.dp)
        )

        // Encabezado decorativo
        Column (modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tus Perros Guardados 🐾",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (starredDogs.isEmpty()) {
                Text(
                    text = "No tienes perros guardados todavía.",
                    style = MaterialTheme.typography.bodyLarge.copy(color = onBackgroundLight),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            } else {
                LazyColumn(

                ) {
                    items(starredDogs) { dog ->
                        DogCardStar(
                            dog = dog,
                            viewModel=viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DogCardStar(dog: Dog, viewModel: StarViewModel, navController: NavHostController) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .border(
                width = 2.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(16.dp) // Más redondeada para suavizar visualmente
            ),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color(0xFFFFF9C4) // Amarillo suave
        ),
        elevation = CardDefaults.cardElevation(8.dp) // Elevación para dar efecto de profundidad
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del perro
            Box (modifier = Modifier.size(110.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(dog.profileImageUrl),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Dog Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, CircleShape)
                )

                IconButton(
                    onClick = { viewModel.toggleStarredDog(dog) },
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Desmarcar como guardado",
                        tint = Color(0xFFFFF9C4)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información del perro
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = dog.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = onBackgroundLight,
                    maxLines = 1
                )

                // Información de la raza y edad
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dog.breed,
                        style = MaterialTheme.typography.bodyLarge,
                        color = onSurfaceLight
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "·",
                        style = MaterialTheme.typography.bodyLarge,
                        color = onSurfaceLight
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${dog.age} años",
                        style = MaterialTheme.typography.bodyLarge,
                        color = onSurfaceLight
                    )
                }

                // Descripción
                Text(
                    text = dog.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = onBackgroundLight
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate("purchaseDescription/${dog.dogId}") },
                    modifier = Modifier.align(Alignment.End)
                    ) {
                    Text(text = dog.status)
                }
            }
        }
    }
}


