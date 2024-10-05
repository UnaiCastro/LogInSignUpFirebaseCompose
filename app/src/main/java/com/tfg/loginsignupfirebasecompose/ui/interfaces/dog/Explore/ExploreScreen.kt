package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Explore

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun ExploreScreen(navController: NavHostController, viewModel: ExploreViewModel = hiltViewModel()) {

    val establishments = viewModel.establishments // Lista de establecimientos

    val newYork = com.google.android.gms.maps.model.LatLng(40.7128, -74.0060)
    val tokyo = com.google.android.gms.maps.model.LatLng(35.6762, 139.6503)
    // Definir la posición inicial de la cámara (ejemplo: el centro del mapa)
    val initialPosition = com.google.android.gms.maps.model.LatLng(
        0.0,
        0.0
    )
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(initialPosition, 2f) // Zoom global
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        // Mapa de Google
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            establishments.forEach { establishment ->
                val latitude = establishment.coordinates["latitude"]
                val longitude = establishment.coordinates["longitude"]

                // Verificamos que latitud y longitud no sean nulas
                if (latitude != null && longitude != null) {
                    Marker(
                        state = MarkerState(position = com.google.android.gms.maps.model.LatLng(latitude, longitude)),
                        title = establishment.name,
                        onInfoWindowClick = {
                            navController.navigate("establishmentDescription/${establishment.establishmentId}")
                        }
                    )
                }
            }
        }
    }
}