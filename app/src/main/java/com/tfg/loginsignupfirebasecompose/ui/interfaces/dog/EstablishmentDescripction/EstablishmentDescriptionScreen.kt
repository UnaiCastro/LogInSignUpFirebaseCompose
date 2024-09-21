package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.EstablishmentDescripction

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.tfg.loginsignupfirebasecompose.R
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Establishment

@Composable
fun EstablishmentDescriptionScreen(
    establishmentId: String,
    navController: NavHostController,
    viewModel: EstablishmentDescriptionViewModel = hiltViewModel()
){
    // Estados para almacenar los datos del establecimiento y los perros
    var establishment by remember { mutableStateOf<Establishment?>(null) }
    var dogs by remember { mutableStateOf<List<Dog>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) } // Estado de carga

    // Ejecutamos la corutina dentro de LaunchedEffect para cargar los datos
    LaunchedEffect(establishmentId) {
        isLoading = true
        // Llamar a la función suspendida para obtener los detalles del establecimiento
        establishment = viewModel.getEstablishmentDetails(establishmentId)
        establishment?.let {
            // Si se obtiene el establecimiento, buscar los perros asociados
            dogs = viewModel.getDogsByOwner(it.owner_id)
        }
        isLoading = false
    }

    // Mostrar un indicador de carga mientras se obtienen los datos
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Mostrar la interfaz solo si ya no estamos cargando
        establishment?.let {
            Box(modifier = Modifier.fillMaxSize()) {
                // Imagen de fondo
                Image(
                    painter = painterResource(id = R.drawable.goldenretrieverbaby), // reemplaza con tu imagen
                    contentDescription = "Establishment Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Ocupa la parte superior (200.dp alto ajustable)
                )

                // Botón de retroceso en la esquina superior izquierda
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                // Card con información del establecimiento
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = 120.dp) // Colocarlo debajo de la imagen
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nombre: ${it.name}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Dirección: ${it.adress}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Teléfono: ${it.phone}",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = "Likes")
                            Text(text = " ${it.liked_users.size}", style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                }

                // Espacio entre la Card y el LazyColumn
                /*Spacer(modifier = Modifier.height(16.dp))*/


                Text(
                    modifier = Modifier.fillMaxWidth().padding(top = 290.dp, start = 16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Start,
                    style = MaterialTheme.typography.titleLarge,
                    text = "Perros:",
                )
                // LazyColumn con los perros asociados al dueño del establecimiento
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 320.dp) // Ajusta para que empiece debajo de la Card
                ) {
                    items(dogs) { dog ->
                        DogCard(dog = dog) // Muestra cada perro en una tarjeta
                    }
                }
            }
        }
    }
}

@Composable
fun DogCard(dog: Dog) {
    ElevatedCard (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(color = Color.Yellow, width = 2.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )

    ) {
        Row(modifier = Modifier.padding(16.dp)) {

            Column(modifier = Modifier.weight(1f).fillMaxHeight().padding(end = 16.dp)) {
                Text(
                    text = dog.name,
                    style = MaterialTheme.typography.titleMedium ,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = dog.breed,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "·",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 2.dp).align(Alignment.CenterVertically)
                    )
                    Text(
                        text = dog.age.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 0.dp)
                    )
                }

                Text(
                    text = dog.price.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()) {
                    IconButton(
                        onClick = { /* Handle button click */ },
                        modifier = Modifier.align(Alignment.CenterVertically),
                    ) {
                        Icon(imageVector = Icons.Filled.Star, contentDescription = "Star")
                    }
                    Text(
                        text = " 100",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 1.dp).align(Alignment.CenterVertically)
                    )
                }
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Button(
                        onClick = { /* Handle button click */ },
                        modifier = Modifier.padding(end = 2.dp),
                    ) {
                        Text(text = "Buy")
                    }
                    TextButton(
                        onClick = { /* Handle button click */ },
                        modifier = Modifier.padding(end = 2.dp),
                    ) {
                        Text(text = "Contactar")
                    }
                    IconButton(
                        onClick = { /* Handle button click */ },
                        modifier = Modifier.padding(end = 2.dp).size(20.dp).align(Alignment.CenterVertically),
                    ) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = "Share",tint = Color.DarkGray)
                    }


                }
            }
            val painter = rememberAsyncImagePainter(dog.imageUrl)
            Image(
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = "Default Profile Image",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically),
            )
        }
        
    }
}
