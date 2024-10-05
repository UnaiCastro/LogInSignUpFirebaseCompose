package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.EstablishmentDescripction

import android.util.Log
import androidx.lifecycle.ViewModel
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Establishment
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.EstablishmentRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EstablishmentDescriptionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val establishmentRepository: EstablishmentRepository,
    private val dogRepository: DogRepository
) : ViewModel() {



    suspend fun getEstablishmentDetails(establishmentId: String): Establishment? {
        Log.d("EstablishmentDetails", "Obteniendo detalles del establecimiento con ID: $establishmentId")
        return try {
            establishmentRepository.getEstablishmentById(establishmentId)
        } catch (e: Exception) {
            Log.e("EstablishmentDetails", "Error al obtener los detalles del establecimiento", e)
            null
        }
    }

    suspend fun getDogsByOwner(ownerId: String): List<Dog> {
        return try {
            dogRepository.getDogsByOwner(ownerId)
        } catch (e: Exception) {
            Log.e("DogFetchError", "Error al obtener los perros del dueño", e)
            emptyList()
        }
    }
}
