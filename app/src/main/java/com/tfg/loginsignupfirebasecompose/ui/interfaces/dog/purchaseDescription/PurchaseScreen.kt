package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.purchaseDescription

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.compose.primaryContainerLight
import com.example.compose.primaryLightHighContrast
import com.example.compose.secondaryContainerLightHighContrast
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
    val navigationEvent by viewModel.navigationEvent.collectAsState()


    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { destination ->
            navController.navigate(destination)
        }
    }

    val dog by viewModel.dog.collectAsState()
    val owner by viewModel.owner.collectAsState()

    if (dog == null || owner == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen del perro
            Image(
                painter = rememberAsyncImagePainter(dog?.profileImageUrl),
                contentDescription = "Dog Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            // Contenido
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 190.dp)
            ) {
                ElevatedCard(
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp), // Solo esquinas superiores redondeadas
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 60.dp) // Elevar la tarjeta desde más abajo
                        .shadow(6.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)), // Sombra alrededor de la tarjeta
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = primaryContainerLight
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

                    // Información del dueño
                    ElevatedCard(
                        elevation = androidx.compose.material3.CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = primaryLightHighContrast
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Owner Information",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Text(
                                    text = owner?.name ?: "Unknown",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                                Text(
                                    text = owner?.phone ?: "Unknown",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                                Text(
                                    text = owner?.email ?: "Unknown",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                                Text(
                                    text = owner?.address ?: "Unknown",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                            }
                            Image(
                                painter = rememberAsyncImagePainter(owner?.profileImageUrl),
                                contentDescription = "Owner Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(RoundedCornerShape(60.dp))
                                    .align(Alignment.CenterVertically)
                                    .padding(16.dp)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.adoptOrBuy(dog!!, owner!!.userId, dogId)
                            navController.navigateUp()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp, top = 16.dp, start = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryLightHighContrast
                        )
                    ) {
                        Text(
                            text = when (dog?.status) {
                                "Adopt" -> "Adopt"
                                "Buy" -> "Buy"
                                else -> "Contact"
                            }
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.navigateToChat(dogId)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary // Color del texto
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)

                    ) {
                        Text(text = "Chat with me", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(200.dp)) // Espacio al final
                }
            }
        }
    }

}

