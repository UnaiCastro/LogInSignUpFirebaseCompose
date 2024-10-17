package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.EstablishmentDescripction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Establishment
import com.tfg.loginsignupfirebasecompose.data.collectionsData.User
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.EstablishmentRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstablishmentDescriptionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val establishmentRepository: EstablishmentRepository,
    private val dogRepository: DogRepository
) : ViewModel() {

    val uid = FirebaseAuth.getInstance().currentUser?.uid

    private val _dogs = MutableStateFlow<List<Dog>>(emptyList())
    val dogs: StateFlow<List<Dog>> = _dogs.asStateFlow()


    suspend fun getEstablishmentDetails(establishmentId: String): Establishment? {
        Log.d("EstablishmentDetails", "Obteniendo detalles del establecimiento con ID: $establishmentId")
        return try {
            val establishment = establishmentRepository.getEstablishmentById(establishmentId)
            Log.d("EstablishmentDetails", "Detalles del establecimiento obtenidos: $establishment")
            establishment
        } catch (e: Exception) {
            Log.e("EstablishmentDetails", "Error al obtener los detalles del establecimiento", e)
            null
        }
    }

    suspend fun getDogsByOwner(ownerId: String) {
        val dogList = try {
            dogRepository.getDogsByOwner(ownerId)
        } catch (e: Exception) {
            Log.e("DogFetchError", "Error al obtener los perros del dueño", e)
            emptyList()
        }
        _dogs.value = dogList
    }

    suspend fun isEstablishmentLiked(establishmentId: String): Boolean {
        val currentUser = userRepository.getUserDetailsById(uid.toString())
        return currentUser?.likedEstablishments?.contains(establishmentId) ?: false
    }

    // Método para añadir a likedEstablishments
    fun likeEstablishment(establishmentId: String) {
        viewModelScope.launch {
            userRepository.addToLikedEstablishments(uid.toString(),establishmentId)
            establishmentRepository.addToLikes(establishmentId,uid.toString())
        }
    }

    // Método para eliminar de likedEstablishments
     fun unlikeEstablishment(establishmentId: String) {
        viewModelScope.launch {
            userRepository.removeFromLikedEstablishments(uid.toString(),establishmentId)
            establishmentRepository.removeFromLikes(establishmentId,uid.toString())
        }
    }

    suspend fun isDogStarred(dogId: String): Boolean {
        val currentUser = userRepository.getUserDetailsById(uid.toString())
        return currentUser?.starred_dogs?.contains(dogId) ?: false
    }

    suspend fun isDogShared(dogId: String): Boolean {
        val currentUser = userRepository.getUserDetailsById(uid.toString())
        return currentUser?.sharedDogs?.contains(dogId) ?: false
    }



    fun onToggleStarred(dogId: String) {
        viewModelScope.launch {
            val currentUser = userRepository.getUserDetailsById(uid.toString())
            val newDogsList = _dogs.value.toMutableList()

            if (currentUser?.starred_dogs?.contains(dogId) == true) {
                userRepository.removeStarredDog(uid.toString(), dogId)
                newDogsList.find { it.dogId == dogId }?.let { dog ->
/*
                    newDogsList[newDogsList.indexOf(dog)] = dog.copy(starred_dogs = dog.shared_dog_userId.filter { it != uid.toString() })
*/
                }
            } else {
                userRepository.addToStarredDogs(uid.toString(), dogId)
                newDogsList.find { it.dogId == dogId }?.let { dog ->
/*
                    newDogsList[newDogsList.indexOf(dog)] = dog.copy(starred_dogs = dog.shared_dog_userId + uid.toString())
*/
                }
            }

            // Actualiza el StateFlow
            _dogs.value = newDogsList
        }
    }

    fun onToggleShared(dogId: String) {
        viewModelScope.launch {
            val currentUser = userRepository.getUserDetailsById(uid.toString())
            val newDogsList = _dogs.value.toMutableList()

            if (currentUser?.sharedDogs?.contains(dogId) == true) {
                userRepository.removeSharedDog(uid.toString(), dogId)
                newDogsList.find { it.dogId == dogId }?.let { dog ->
                    newDogsList[newDogsList.indexOf(dog)] = dog.copy(shared_dog_userId = dog.shared_dog_userId.filter { it != uid.toString() })
                }
            } else {
                userRepository.addSharedDog(uid.toString(), dogId)
                newDogsList.find { it.dogId == dogId }?.let { dog ->
                    newDogsList[newDogsList.indexOf(dog)] = dog.copy(shared_dog_userId = dog.shared_dog_userId + uid.toString())
                }
            }

            // Actualiza el StateFlow
            _dogs.value = newDogsList
        }
    }

}
