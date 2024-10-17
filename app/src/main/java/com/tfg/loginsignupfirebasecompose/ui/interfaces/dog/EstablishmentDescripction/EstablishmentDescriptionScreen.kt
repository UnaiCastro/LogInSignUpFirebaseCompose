package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.EstablishmentDescripction

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.compose.onBackgroundLight
import com.example.compose.onSurfaceLight
import com.example.compose.primaryLight
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Establishment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstablishmentDescriptionScreen(
    establishmentId: String,
    navController: NavHostController,
    viewModel: EstablishmentDescriptionViewModel = hiltViewModel()
) {


    var isLiked by remember { mutableStateOf(false) }
    var establishment by remember { mutableStateOf<Establishment?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var likedUsersCount by remember { mutableStateOf(0) }

    val dogs by viewModel.dogs.collectAsState()




    LaunchedEffect(establishmentId) {
        isLoading = true
        establishment = viewModel.getEstablishmentDetails(establishmentId)
        establishment?.let {
            viewModel.getDogsByOwner(it.owner_id)
            isLiked = viewModel.isEstablishmentLiked(establishmentId)
            likedUsersCount = it.liked_users.size
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,

                    ),
                actions = {
                    IconButton(onClick = {
                        isLiked = if (isLiked) {
                            viewModel.unlikeEstablishment(establishmentId)
                            likedUsersCount--
                            false // Cambia el estado a no guardado
                        } else {
                            viewModel.likeEstablishment(establishmentId)
                            likedUsersCount++
                            true // Cambia el estado a guardado
                        }
                    }) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) Color.Blue else Color.Black
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {

            establishment?.let { est ->
                val painter = rememberAsyncImagePainter(est.establishmentImage)
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painter,
                        contentDescription = "Establishment Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .padding(paddingValues)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 0.dp)
                    ) {
                        Spacer(modifier = Modifier.height(220.dp))

                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .offset(y = 40.dp),
                            elevation = CardDefaults.elevatedCardElevation(8.dp),
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp
                            ) // Curvatura para que la tarjeta se vea elegante
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Nombre: ${est.name}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = est.adress,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = est.phone,
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Favorite,
                                        contentDescription = "Likes",
                                        tint = Color.Blue
                                    )
                                    Text(
                                        text = " $likedUsersCount",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Dogs Owned:",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.align(Alignment.Start)
                                )

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp)
                                ) {
                                    items(dogs) { dog ->
                                        var isStarred by remember { mutableStateOf(false) }
                                        var isShared by remember { mutableStateOf(false) }

                                        LaunchedEffect(dog.dogId) {
                                            isStarred = viewModel.isDogStarred(dog.dogId)
                                            isShared = viewModel.isDogShared(dog.dogId)
                                        }
                                        DogCard(
                                            dog = dog,
                                            isStarred = isStarred,
                                            isShared = isShared,
                                            viewModel = viewModel
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DogCard(
    dog: Dog,
    isStarred: Boolean,
    isShared: Boolean,
    viewModel: EstablishmentDescriptionViewModel
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .border(
                width = 2.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isStarred) Color(0xFFFFF9C4) else Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(110.dp)) {
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
                    onClick = { viewModel.onToggleStarred(dog.dogId) },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = if (isStarred) Icons.Filled.Star else Icons.Filled.Star,
                        contentDescription = if (isStarred) "Guardado" else "Guardar",
                        tint = if (isStarred) Color(0xFFFFF9C4) else Color.Black, // Amarillo si está guardado
                        modifier = Modifier
                            .size(25.dp)
                            .background(primaryLight.copy(alpha = 0.7f), CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información del perro y botones
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

                // Precio
                Text(
                    text = "${dog.price} USD",
                    style = MaterialTheme.typography.bodyLarge,
                    color = onBackgroundLight
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(
                        onClick = { viewModel.onToggleShared(dog.dogId) },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        if (isShared) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Compartido",
                                tint = Color.Green,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        } else {
                            Text(text = "Share")
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { /*viewModel.navigateToPurchaseDescription(dog.dogId)*/ },
                        ) {
                        Text(text = dog.status)
                    }
                }
            }
        }
    }
}
