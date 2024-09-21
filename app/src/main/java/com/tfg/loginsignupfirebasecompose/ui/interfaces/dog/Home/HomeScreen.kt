package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Home

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.compose.primaryContainerDark
import com.example.compose.tertiaryContainerLight
import com.example.compose.tertiaryLight
import com.tfg.loginsignupfirebasecompose.R
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    var query by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedGender by remember { mutableStateOf("") }
    var selectedBreed by remember { mutableStateOf("") }

    val filteredDogs by viewModel.filteredDogs.collectAsState()

    val profileImageUrl by viewModel.profileImageUrl.collectAsState() // Cambiado a collectAsState
    val currentUser by viewModel.currentUser.collectAsState()
/*
    val dogs by viewModel.dogs.collectAsState()
*/

    val starredDogs by viewModel.starredDogs.collectAsState()  // Asegúrate de recolectar esto si es necesario
    val sharedDogs by viewModel.sharedDogs.collectAsState()


    // Drawer state
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun updateFilters() {
        viewModel.updateQuery(query)
        viewModel.updateSelectedBreed(selectedBreed)
        viewModel.updateSelectedGender(selectedGender)

    }

    // Llamar a la función cada vez que cambie algo
    LaunchedEffect(query, selectedBreed, selectedGender, selectedFilter) {
        updateFilters()
    }

    // Function to toggle drawer

    fun toggleDrawer() {
        scope.launch {
            if (drawerState.isClosed) {
                drawerState.open()
            } else {
                drawerState.close()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    selectedFilter = selectedFilter,
                    onFilterChange = { selectedFilter = it },
                    selectedBreed = selectedBreed,
                    onBreedChange = { selectedBreed = it },
                    selectedGender = selectedGender,
                    onGenderChange = { selectedGender = it }
                )
            }
        }
    ) {
        // Main layout with background
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Column that contains all elements
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row (modifier = Modifier.padding(vertical = 16.dp)){
                    val painter = rememberAsyncImagePainter(profileImageUrl)
                    Image(
                        painter = painter,
                        contentDescription = "Default Profile Image",
                        modifier = Modifier
                            .clip(CircleShape)
                            .width(60.dp)
                            .height(60.dp),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        modifier = Modifier.padding(start = 8.dp).align(Alignment.CenterVertically),
                        text = "Welcome, $currentUser",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black
                    )



                }
                Text(
                    text = "FIND YOUR DOG",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,

                )

                // Search Bar placed after "Dogs" text
                SearchBar(
                    query = query,
                    onQueryChange = { newQuery -> query = newQuery },
                    onSearch = { /* Perform search action */ },
                    placeholder = { Text("Search...") },
                    leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { toggleDrawer() }) {
                            Icon(painter = painterResource(id = R.drawable.ic_tune), contentDescription = "Filter")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    active = false,
                    onActiveChange = {},
                    content = {}
                )

                LazyColumn {
                    items(filteredDogs) { dog ->
                        DogCard(
                            dog = dog,
                            isStarred = starredDogs.contains(dog.dogId),
                            isShared = sharedDogs.contains(dog.dogId),
                            onToggleStarred = { viewModel.toggleStarredDog(dog.dogId) },
                            onToggleShared = { viewModel.toggleSharedDog(dog.dogId) }
                        )
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
    onToggleStarred: (String) -> Unit,
    onToggleShared: (String) -> Unit,
    isShared: Boolean  // Ahora recibimos si el perro está compartido
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .border(
                width = 2.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = if (isStarred) Color.Yellow else tertiaryContainerLight
        )

    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = dog.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(text = dog.breed, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "·",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                    Text(text = "${dog.age} years", style = MaterialTheme.typography.bodyLarge)
                }
                Text(text = "${dog.price} USD", style = MaterialTheme.typography.bodyLarge)

                // Botón de estrella para marcar como favorito
                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                ) {
                    IconButton(onClick = { onToggleStarred(dog.dogId) }) {
                        Icon(
                            imageVector = if (isStarred) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Star",
                            tint = if (isStarred) Color.Yellow else Color.Gray
                        )
                    }
                    Text(
                        text = if (isStarred) "Guardado" else "Guardar",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                // Botón de compartir con icono dinámico
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Button(onClick = { /* Comprar */ }) {
                        Text(text = dog.status)
                    }
                    TextButton(onClick = { /* Contactar */ }) {
                        Text(text = "Chat with me")
                    }

                    // Botón de compartir
                    IconButton(onClick = { onToggleShared(dog.dogId) }) {
                        Icon(
                            imageVector = if (isShared) Icons.Filled.Check else Icons.Filled.Share,  // Si está compartido, icono de check
                            contentDescription = if (isShared) "Shared" else "Share",
                            tint = if (isShared) Color.Green else Color.DarkGray  // Verde si está compartido
                        )
                    }
                }
            }

            // Imagen del perro
            Image(
                painter = rememberAsyncImagePainter(dog.imageUrl),
                contentScale = ContentScale.Crop,
                contentDescription = "Dog Image",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically),
            )
        }
    }
}




@Composable
fun DrawerContent(
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    selectedBreed: String,
    onBreedChange: (String) -> Unit,
    selectedGender: String,
    onGenderChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        /*Text("Map", style = MaterialTheme.typography.titleLarge,fontWeight = FontWeight.Bold,)
        FilterChipCage(
            text = "All",
            isSelected = selectedFilter == "All",
            onSelectionChange = { onFilterChange("All") }
        )
        FilterChipCage(
            text = "Around me",
            isSelected = selectedFilter == "Around me",
            onSelectionChange = { onFilterChange("Around me") }
        )*/

        /*Spacer(modifier = Modifier.height(16.dp))*/

        Text("Breed", style = MaterialTheme.typography.titleLarge,fontWeight = FontWeight.Bold,)
        FilterChipCage(
            text = "All",
            isSelected = selectedBreed == "All",
            onSelectionChange = { onBreedChange("All") }
        )
        FilterChipCage(
            text = "GoldenRetriever",
            isSelected = selectedBreed == "Golden Retriever",
            onSelectionChange = { onBreedChange("Golden Retriever") }
        )
        FilterChipCage(
            text = "Border Collie",
            isSelected = selectedBreed == "Border Collie",
            onSelectionChange = { onBreedChange("Border Collie") }
        )
        FilterChipCage(
            text = "Samoyed",
            isSelected = selectedBreed == "Samoyed",
            onSelectionChange = { onBreedChange("Samoyed") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Gender", style = MaterialTheme.typography.titleLarge,fontWeight = FontWeight.Bold,)
        FilterChipCage(
            text = "Male",
            isSelected = selectedGender == "Male",
            onSelectionChange = { onGenderChange("Male") }
        )
        FilterChipCage(
            text = "Female",
            isSelected = selectedGender == "Female",
            onSelectionChange = { onGenderChange("Female") }
        )
    }
}



@Composable
private fun FilterChipCage(
    text: String,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit
) {
    FilterChip(
        modifier = Modifier.padding(horizontal = 4.dp),
        onClick = { onSelectionChange(!isSelected) },
        label = { Text(text) },
        selected = isSelected,
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        }
    )
}
