package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.StarScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.compose.backgroundLight
import com.example.compose.onBackgroundLight
import com.example.compose.primaryLight
import com.example.compose.surfaceContainerLight
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Home.HomeViewModel

@Composable
fun StarredScreen(navController: NavController, viewModel: StarViewModel = hiltViewModel()) {
    val starredDogs by viewModel.starredDogs.collectAsState() // Lista de perros guardados (solo IDs)
    val savedDogs by viewModel.savedDogs.collectAsState() // Lista completa de perros guardados por el usuario

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

            if (savedDogs.isEmpty()) {
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
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .shadow(10.dp, RoundedCornerShape(20.dp)), // Sombra suave
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = surfaceContainerLight  // Color de fondo blanco ligeramente translúcido
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Nombre del perro y edad
                            Column {
                                Text(
                                    text = savedDogs.first().name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(text = "${savedDogs.first().age} years", style = MaterialTheme.typography.bodyLarge)
                            }

                            // Imagen del perro
                            Image(
                                painter = rememberAsyncImagePainter(savedDogs.first().imageUrl),
                                contentDescription = "Dog Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.White, CircleShape)
                            )
                        }

                        // Botón de estrella para marcar como favorito
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth()
                        ) {
                            IconButton(onClick = { /*onToggleStarred(dog.dogId) */}) {
                                Icon(
                                    imageVector = /*if (isStarred) Icons.Filled.Star else */Icons.Outlined.Star,
                                    contentDescription = "Star",
                                    tint = /*if (isStarred) Color.Yellow else */primaryLight
                                )
                            }
                            Text(
                                text = /*if (isStarred) "Guardado" else */"Guardar",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Spacer(modifier = Modifier.weight(1f)) // Espacio flexible para empujar el siguiente botón a la derecha

                            // Botón de compartir con icono dinámico
                            IconButton(onClick = { /*onToggleShared(dog.dogId) */}) {
                                Icon(
                                    imageVector = /*if (isShared) Icons.Filled.Check else */Icons.Filled.Share,
                                    contentDescription = /*if (isShared) "Shared" else */"Share",
                                    tint = /*if (isShared) Color.Green else */Color.DarkGray
                                )
                            }
                        }

                        // Precio y raza del perro
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text(text = savedDogs.first().breed, style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "·",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            Text(text = "${savedDogs.first().price} USD", style = MaterialTheme.typography.bodyLarge)
                        }

                        // Botones de "Comprar" y "Contactar"
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = { /* Acción de compra */ }) {
                                Text(text = "Comprar")
                            }
                            TextButton(onClick = { /* Contactar */ }) {
                                Text(text = "Chat with me")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DogCardStar(dog: Int, isStarred: Boolean, onToggleStarred: () -> Unit) {

}
