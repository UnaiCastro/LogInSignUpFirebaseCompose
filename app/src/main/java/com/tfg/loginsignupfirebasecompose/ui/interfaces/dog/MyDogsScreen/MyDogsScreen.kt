package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.MyDogsScreen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
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
import com.example.compose.onBackgroundLight
import com.example.compose.onSurfaceLight
import com.example.compose.primaryLight
import com.tfg.loginsignupfirebasecompose.R
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDogsScreen(navController: NavHostController, viewModel: MyDogsViewModel = hiltViewModel()) {

    val myDogs by viewModel.myDogs.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchMyDogs() // Esto actualizará la lista de perros
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 35.dp),

                        contentAlignment = Alignment.Center
                    ) {
                        Text("My Dogs")
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },

                        ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                )
            )
        }
    ){

        Image(
            painter = painterResource(id = R.drawable.background4),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize().alpha(0.6f).blur(4.dp)
        )

       Column(
           modifier = Modifier
               .fillMaxSize()
               .padding(it)
               .padding(16.dp),

       ) {
           Box(
               modifier = Modifier.fillMaxWidth(),
               contentAlignment = Alignment.TopEnd // Alinea el contenido dentro de Box a la derecha
           ) {
               TextButton(
                   onClick = { navController.navigate("uploadDog") },
                   modifier = Modifier
                       .padding(vertical = 8.dp)
               ) {
                   Row(
                       verticalAlignment = Alignment.CenterVertically
                   ) {
                       Icon(
                           imageVector = Icons.Default.Add, // Usar el ícono de más
                           contentDescription = "Add Dog",
                       )
                       Text(text = "Upload Dog")
                   }
               }
           }

           LazyColumn {
               items(myDogs) { dog ->
                   DogItemCard(
                       dog = dog,
                       viewModel = viewModel
                   )
               }

           }
       }
    }
}

@Composable
fun DogItemCard(
    dog: Dog,
    viewModel: MyDogsViewModel,

    ) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .border(
                width = 2.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(16.dp) // Más redondeada para suavizar visualmente
            ),
        elevation = CardDefaults.cardElevation(8.dp)
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
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

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

                Text(
                    text = dog.description,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyLarge,
                    color = onBackgroundLight
                )

                Spacer(modifier = Modifier.height(8.dp))

                IconButton(
                    onClick = { viewModel.deleteDog(dog.dogId) },
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Check",
                        tint = primaryLight,
                        modifier = Modifier
                            .size(25.dp)
                    )
                }


            }
        }
    }
}
