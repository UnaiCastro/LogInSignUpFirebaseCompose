package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Home

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.compose.onBackgroundLight
import com.example.compose.onSurfaceLight
import com.example.compose.primaryLight
import com.tfg.loginsignupfirebasecompose.R
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    innerNavController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var query by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedGender by remember { mutableStateOf("") }
    var selectedBreed by remember { mutableStateOf("") }

    val filteredDogs by viewModel.filteredDogs.collectAsState()

    val navigationEvent by viewModel.navigationEvent.collectAsState()

    val profileImageUrl by viewModel.profileImageUrl.collectAsState() // Cambiado a collectAsState
    val currentUser by viewModel.currentUser.collectAsState()

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

    LaunchedEffect(query, selectedBreed, selectedGender, selectedFilter) {
        updateFilters()
    }

    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { destination ->
            innerNavController.navigate(destination)
            viewModel.clearNavigationEvent()
        }
    }
    
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
                 // Fondo más claro y limpio
        ) {

            Image(
                painter = painterResource(id = R.drawable.background4),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize().alpha(0.6f).blur(4.dp)
            )

            // Column that contains all elements
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                // Header con imagen de perfil y bienvenida
                Row(modifier = Modifier.padding(vertical = 16.dp)) {
                    val painter = rememberAsyncImagePainter(profileImageUrl)
                    Image(
                        painter = painter,
                        contentDescription = "Default Profile Image",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(60.dp),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .align(Alignment.CenterVertically),
                        text = "Welcome, $currentUser",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black
                    )
                }

                Text(
                    text = "FIND YOUR DOG",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )

                SearchBar(
                    query = query,
                    onQueryChange = { newQuery -> query = newQuery },
                    onSearch = { /* Perform search action */ },
                    placeholder = { Text("Search...", color = onSurfaceLight) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = primaryLight  // Ícono gris claro
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { toggleDrawer() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_tune),
                                contentDescription = "Filter",
                                tint = primaryLight  // Ícono verde para consistencia
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    active = false,
                    onActiveChange = {},
                    content = {}
                )

                // Lista de perros
                LazyColumn {
                    items(filteredDogs) { dog ->
                        DogCard(
                            dog = dog,
                            isStarred = starredDogs.contains(dog.dogId),
                            isShared = sharedDogs.contains(dog.dogId),
                            onToggleStarred = { viewModel.toggleStarredDog(dog.dogId) },
                            onToggleShared = { viewModel.toggleSharedDog(dog.dogId) },
                            viewModel = viewModel,
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
    isShared: Boolean, // Ahora recibimos si el perro está compartido,
    viewModel: HomeViewModel,

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
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = if (isStarred) Color(0xFFFFF9C4) else Color(0xFFF5F5F5)
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
                    onClick = { onToggleStarred(dog.dogId) },
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
                        onClick = { onToggleShared(dog.dogId) },
                        modifier = Modifier.padding(end = 8.dp) // Espacio entre botones
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
                        onClick = { viewModel.navigateToPurchaseDescription(dog.dogId) },

                    ) {
                        Text(text = dog.status)
                    }
                }
            }
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

        // Sección de filtro por raza
        Text("Breed", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        FilterChipCage(
            text = "All",
            isSelected = selectedBreed == "All",
            onSelectionChange = { onBreedChange("All") }
        )
        FilterChipCage(
            text = "Golden Retriever",
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

        // Sección de filtro por género
        Text("Gender", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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
fun FilterChipCage(
    text: String,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit
) {
    FilterChip(
        modifier = Modifier.padding(horizontal = 4.dp),
        onClick = { onSelectionChange(!isSelected) },
        label = { Text(text)}, // Texto cambia de color si está seleccionado
        selected = isSelected,
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                )
            }
        } else {
            null
        },

    )
}
