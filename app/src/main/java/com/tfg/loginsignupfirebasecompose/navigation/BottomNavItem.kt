package com.tfg.loginsignupfirebasecompose.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val label: String, val iconSelected: ImageVector,val unSelectedIcon:ImageVector, val route: String) {
    object Inicio : BottomNavItem("Home", Icons.Filled.Home,Icons.Outlined.Home,"home")
    object Guardados : BottomNavItem("Starred", Icons.Filled.Star,Icons.Outlined.Star, "starred")
    object Explora : BottomNavItem("Explore", Icons.Filled.Search,Icons.Outlined.Search, "explore")
    object Perfil : BottomNavItem("Profile", Icons.Filled.Person, Icons.Outlined.Person, "profile")
}

