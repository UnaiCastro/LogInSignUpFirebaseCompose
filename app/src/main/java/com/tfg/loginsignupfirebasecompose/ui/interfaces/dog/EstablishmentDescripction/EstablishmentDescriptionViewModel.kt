package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.EstablishmentDescripction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Establishment
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
    private val establishmentRepository: EstablishmentRepository,
    private val dogRepository: DogRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _establishment = MutableStateFlow<Establishment?>(null)
    val establishment: StateFlow<Establishment?> = _establishment.asStateFlow()

    private val _dogs = MutableStateFlow<List<Dog>>(emptyList())
    val dogs: StateFlow<List<Dog>> = _dogs.asStateFlow()

    private val _userStarredDogs = MutableStateFlow<List<String>>(emptyList())
    val userStarredDogs: StateFlow<List<String>> = _userStarredDogs.asStateFlow()

    private val _userSharedDogs = MutableStateFlow<List<String>>(emptyList())
    val userSharedDogs: StateFlow<List<String>> = _userSharedDogs.asStateFlow()

    private val _establishmentLikes = MutableStateFlow<List<String>>(emptyList())
    val establishmentLikes: StateFlow<List<String>> = _establishmentLikes.asStateFlow()

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    fun loadEstablishmentAndDogs(establishmentId: String) {
        viewModelScope.launch {
            val establishment = establishmentRepository.getEstablishmentById(establishmentId)
            _establishment.value = establishment

            establishment?.let {
                val dogs = dogRepository.getDogsByOwner(it.owner_id)
                _dogs.value = dogs

                userId?.let { uid ->
                    val user = userRepository.getUserDetailsById(uid)
                    _userStarredDogs.value = user?.starred_dogs ?: emptyList()
                    _userSharedDogs.value = user?.sharedDogs ?: emptyList()
                }
            }
        }
    }

    fun toggleStarredDog(dogId: String) {
        viewModelScope.launch {
            val isStarred = _userStarredDogs.value.contains(dogId)
            if (isStarred) {
                userRepository.removeStarredDog(userId!!, dogId)
                _userStarredDogs.value -= dogId
            } else {
                userRepository.addToStarredDogs(userId!!, dogId)
                _userStarredDogs.value += dogId
            }
        }
    }

    fun toggleSharedDog(dogId: String) {
        viewModelScope.launch {
            val isShared = _userSharedDogs.value.contains(dogId)
            if (isShared) {
                userRepository.removeSharedDog(userId!!, dogId)
                dogRepository.updateSharedBy(dogId, userId, add = false)
                _userSharedDogs.value -= dogId
            } else {
                userRepository.addSharedDog(userId!!, dogId)
                dogRepository.updateSharedBy(dogId, userId, add = true)
                _userSharedDogs.value += dogId
            }
        }
    }

    fun loadUserEstablishmentLikes() {
        viewModelScope.launch {
            userId?.let {
                val user = userRepository.getUserDetailsById(it)
                _establishmentLikes.value = user?.likedEstablishments ?: emptyList()
            }
        }
    }

    fun toggleEstablishmentLike(establishmentId: String) {
        viewModelScope.launch {
            val isLiked = _establishmentLikes.value.contains(establishmentId)
            if (isLiked) {
                Log.i(
                    "EstablishmentDescriptionViewModel",
                    "Toggling like for establishment with ID: $establishmentId"
                )
                userRepository.removeFromLikedEstablishments(userId!!, establishmentId)
                establishmentRepository.removeFromLikes(establishmentId, userId)
                _establishmentLikes.value -= establishmentId
                val updatedEstablishment =
                    establishmentRepository.getEstablishmentById(establishmentId)
                if (updatedEstablishment != null) {
                    _establishment.value = updatedEstablishment
                }

            } else {
                Log.i(
                    "EstablishmentDescriptionViewModel",
                    "Toggling like for establishment with ID: $establishmentId"
                )
                userRepository.addToLikedEstablishments(userId!!, establishmentId)
                establishmentRepository.addToLikes(establishmentId, userId)
                _establishmentLikes.value += establishmentId
                val updatedEstablishment =
                    establishmentRepository.getEstablishmentById(establishmentId)
                if (updatedEstablishment != null) {
                    _establishment.value = updatedEstablishment
                }
            }
        }
    }
}

