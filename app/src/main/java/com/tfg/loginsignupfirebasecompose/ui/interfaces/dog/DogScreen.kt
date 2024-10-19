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
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.EstablishmentDescripction.EstablishmentDescriptionScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Explore.ExploreScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Home.HomeScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.LikesScreen.LikesScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.MyDogsScreen.MyDogsScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.ProfileScreen.ProfileScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Settings.SettingsScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.SharedScreen.SharedScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.StarScreen.StarredScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.chat.ChatScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.purchaseDescription.PurchaseScreen
import com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.uploadDog.UploadDogScreen

@Composable
fun DogScreen(navController: NavController, viewModel: DogViewModel = hiltViewModel()) {
    val currentUser by viewModel.currentUser.collectAsState()
    val selectedNavItem by viewModel.selectedNavItem.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedNavItem,
                onItemSelected = { viewModel.onNavItemSelected(it) }
            )
        }
    ) { padding ->
        val innerNavController = rememberNavController()

        NavHost(
            navController = innerNavController,
            startDestination = selectedNavItem.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Inicio.route) {
                HomeScreen(navController, innerNavController)
            }
            composable(BottomNavItem.Guardados.route) {
                StarredScreen(innerNavController)
            }
            composable(BottomNavItem.Explora.route) {
                ExploreScreen(innerNavController)
            }
            composable(BottomNavItem.Perfil.route) {
                ProfileScreen(navController, innerNavController)
            }
            composable("chatroom") {
                ChatRoomScreen(innerNavController)
            }
            composable("establishmentDescription/{chatId}") {
                val chatId = it.arguments?.getString("chatId")
                if (chatId != null) {
                    EstablishmentDescriptionScreen(chatId, innerNavController)
                }
            }
            composable("purchaseDescription/{dogId}") {
                val dogId = it.arguments?.getString("dogId")
                if (dogId != null) {
                    PurchaseScreen(dogId, innerNavController)
                }
            }
            composable("chat/{chatId}") {
                val chatId = it.arguments?.getString("chatId")
                if (chatId != null) {
                    ChatScreen(chatId, innerNavController)
                }

            }
            composable("settings") {
                SettingsScreen(innerNavController)
            }
            composable("shared") {
                SharedScreen(innerNavController)
            }
            composable("likes") {
                LikesScreen(innerNavController)
            }
            composable("mydogs") {
                MyDogsScreen(innerNavController)
            }
            composable("uploadDog") {
                UploadDogScreen(innerNavController)
            }

        }

        LaunchedEffect(selectedNavItem) {
            if (innerNavController.currentDestination?.route != selectedNavItem.route) {
                innerNavController.navigate(selectedNavItem.route) {
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





