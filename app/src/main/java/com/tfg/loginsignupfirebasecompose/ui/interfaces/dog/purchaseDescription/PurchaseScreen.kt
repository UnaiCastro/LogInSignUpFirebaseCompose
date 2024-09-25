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
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
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

    val dog by viewModel.dog.collectAsState()
    val owner by viewModel.owner.collectAsState()

    if (dog == null || owner == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = rememberAsyncImagePainter(dog?.imageUrl),
                contentDescription = "Dog Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxWidth()
                    .height(300.dp)

            )
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
                    .padding(top = 240.dp)  // Justo debajo de la imagen
            ) {
                // Tarjeta con la información del perro
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .shadow(6.dp, RoundedCornerShape(20.dp)),
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
                                fontSize = 28.sp,
                                text = dog?.name ?: "Unknown",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${dog?.price ?: "0"} USD",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }


                        // Género
                        Text(
                            text = "${dog?.gender ?: "Unknown"}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Descripción del perro
                        Text(
                            text = if (dog?.gender == "Male") "About him" else "About her",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                        Text(
                            text = dog?.description ?: "No description available",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Raza, edad y estado
                        Text(
                            text = "   · ${dog?.breed ?: "Unknown"}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "   · ${dog?.age ?: "Unknown"} years",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "   · ${dog?.status ?: "Unknown"}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                ElevatedCard(
                    elevation = androidx.compose.material3.CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = surfaceContainerLight
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Owner Information",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${owner?.name ?: "Unknown"}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "${owner?.phone ?: "Unknown"}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Image(
                            painter = rememberAsyncImagePainter(owner?.profileImageUrl),
                            contentDescription = "Owner Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .align(Alignment.CenterVertically)
                        )
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para adoptar o comprar
                Button(
                    onClick = { viewModel.adoptOrBuy(dog!!, owner!!.userId,dogId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = when (dog?.status) {
                            "Adopt" -> "Adopt"
                            "Buy" -> "Buy"
                            else -> "Contact"
                        }
                    )
                }
            }
        }
    }
}

