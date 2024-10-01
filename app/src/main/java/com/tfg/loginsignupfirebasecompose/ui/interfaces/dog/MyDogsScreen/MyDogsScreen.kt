package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.MyDogsScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog


@Composable
fun MyDogsScreen(navController: NavHostController, viewModel: MyDogsViewModel = hiltViewModel()) {

    val myDogs by viewModel.myDogs.collectAsState()

    IconButton(
        onClick = { navController.popBackStack() },
        modifier = Modifier
            .padding(16.dp)
    ) {
        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
    }
    Text(text = "My Dogs Screen")
    LazyColumn {
        items(myDogs) { dog ->
            DogItemCard(dog = dog)
        }

    }
    Button(
        modifier = Modifier.padding(top = 100.dp),
        onClick = { navController.navigate("uploadDog") }) {
        Text(text = "Upload Dog")
    }
}

@Composable
fun DogItemCard(dog: Dog) {
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
                onClick = {},
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
