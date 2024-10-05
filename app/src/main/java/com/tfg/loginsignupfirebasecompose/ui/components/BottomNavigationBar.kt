package com.tfg.loginsignupfirebasecompose.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.compose.primaryContainerDark
import com.example.compose.primaryLightHighContrast
import com.tfg.loginsignupfirebasecompose.navigation.BottomNavItem


@Composable
fun BottomNavigationBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    val items = listOf(
        BottomNavItem.Inicio,
        BottomNavItem.Guardados,
        BottomNavItem.Explora,
        BottomNavItem.Perfil
    )

    NavigationBar (containerColor = primaryLightHighContrast

    ){

        items.forEach { item ->
            NavigationBarItem(
                selected = selectedItem.route == item.route,
                icon = {
                    Icon(
                        imageVector = if (selectedItem.route == item.route) item.iconSelected else item.unSelectedIcon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                onClick = { onItemSelected(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White,
                    indicatorColor = primaryContainerDark,

                )
            )
        }
    }
}