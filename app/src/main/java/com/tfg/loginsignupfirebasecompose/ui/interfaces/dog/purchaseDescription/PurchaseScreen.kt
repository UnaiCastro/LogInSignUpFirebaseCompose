package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.purchaseDescription

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.wear.compose.material3.Button
import coil.compose.rememberAsyncImagePainter
import com.example.compose.surfaceContainerLight

@Composable
fun PurchaseScreen(
    dogId: String,
    navController: NavHostController,
    viewModel: PurchaseViewModel = hiltViewModel()
) {

    LaunchedEffect(dogId) {
        viewModel.getDogById(dogId)
    }

    // Obteniendo los valores de dog y owner de los flujos de estado
    val dog by viewModel.dog.collectAsState()
    val owner by viewModel.owner.collectAsState()

    // Mostramos un indicador de carga si el perro o el dueño no están cargados aún
    if (dog == null || owner == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Si los datos están cargados, renderizamos la pantalla normalmente
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Imagen de fondo del perro
            Image(
                painter = rememberAsyncImagePainter(dog?.imageUrl),
                contentDescription = "Dog Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)  // Menos de la mitad de la pantalla
            )
            // Botón de retroceso en la esquina superior izquierda
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }

            // Detalles del perro
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 260.dp)  // Justo debajo de la imagen
            ) {
                // Tarjeta con la información del perro
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .shadow(10.dp, RoundedCornerShape(20.dp)),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = surfaceContainerLight
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Nombre del perro y precio
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = dog?.name ?: "Unknown",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${dog?.price ?: "0"} USD",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Género
                        Text(
                            text = "Gender: ${dog?.gender ?: "Unknown"}",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Descripción del perro
                        Text(
                            text = if (dog?.gender == "Male") "About him" else "About her",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = dog?.description ?: "No description available",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Raza, edad y estado
                        Text(text = "Breed: ${dog?.breed ?: "Unknown"}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Age: ${dog?.age ?: "Unknown"} years", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Status: ${dog?.status ?: "Unknown"}", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Información del dueño
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = surfaceContainerLight
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Owner: ${owner?.name ?: "Unknown"}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Phone: ${owner?.phone ?: "Unknown"}", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para adoptar o comprar
                Button(
                    onClick = { /* Acción para adoptar o comprar */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = when (dog?.status) {
                            "Available" -> "Adopt"
                            "For Sale" -> "Buy"
                            else -> "Contact"
                        }
                    )
                }
            }
        }
    }
}

