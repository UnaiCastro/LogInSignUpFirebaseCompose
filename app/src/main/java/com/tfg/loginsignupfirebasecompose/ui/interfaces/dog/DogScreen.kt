package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tfg.loginsignupfirebasecompose.navigation.BottomNavItem
import com.tfg.loginsignupfirebasecompose.ui.components.BottomNavigationBar
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.ChatRoom.ChatRoomScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Explore.ExploreScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Home.HomeScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.ProfileScreen.ProfileScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.StarScreen.StarredScreen

@Composable
fun DogScreen(navController: NavController, viewModel: DogViewModel = hiltViewModel()) {
    val currentUser by viewModel.currentUser.collectAsState()
    val selectedNavItem by viewModel.selectedNavItem.collectAsState()
    /*val navigationEvent by viewModel.navigationEvent.collectAsState()

    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { route ->
            navController.navigate(route)  // Usa el navController global aquí
            viewModel.clearNavigationEvent()
        }
    }*/


    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedNavItem,
                onItemSelected = { viewModel.onNavItemSelected(it) }
            )
        }
    ) { padding ->
        // Definimos un controlador de navegación interno
        val innerNavController = rememberNavController()

        // NavHost interno para cambiar entre las pantallas del BottomNavItem
        NavHost(
            navController = innerNavController,
            startDestination = selectedNavItem.route, // La pantalla inicial es la seleccionada
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Inicio.route) {
                HomeScreen(innerNavController)
            }
            composable(BottomNavItem.Guardados.route) {
                StarredScreen(innerNavController)
            }
            composable(BottomNavItem.Explora.route) {
                ExploreScreen(innerNavController)
            }
            composable(BottomNavItem.Perfil.route) {
                ProfileScreen(navController,innerNavController)
            }
            composable("chatroom") {
                ChatRoomScreen(innerNavController)
            }
        }

        // Cambia la pantalla cuando se selecciona un nuevo item en la barra de navegación
        LaunchedEffect(selectedNavItem) {
            if (innerNavController.currentDestination?.route != selectedNavItem.route) {
                innerNavController.navigate(selectedNavItem.route) {
                    // Opcional: Evitar agregar múltiples veces la misma pantalla al backstack
                    popUpTo(innerNavController.graph.startDestinationId) {
                        saveState = true
                    }
                    restoreState = true
                    launchSingleTop = true
                }
            }
        }
    }

}





