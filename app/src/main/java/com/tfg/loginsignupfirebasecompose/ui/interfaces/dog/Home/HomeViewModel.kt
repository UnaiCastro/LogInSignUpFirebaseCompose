package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.ChatRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dogRepository: DogRepository,
    private val authRepository: AuthRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    private val _filteredDogs = MutableStateFlow<List<Dog>>(emptyList())
    val filteredDogs: StateFlow<List<Dog>> = _filteredDogs

    private val _starredDogs = MutableStateFlow<List<String>>(emptyList())
    val starredDogs: StateFlow<List<String>> = _starredDogs

    private val _sharedDogs = MutableStateFlow<List<String>>(emptyList())
    val sharedDogs: StateFlow<List<String>> = _sharedDogs

    private val _profileImageUrl = MutableStateFlow<String>("")
    val profileImageUrl: StateFlow<String> = _profileImageUrl

    private val _currentUser = MutableStateFlow<String>("")
    val currentUser: StateFlow<String> = _currentUser

    private var allDogs = listOf<Dog>()
    private var selectedBreed = ""
    private var selectedGender = ""
    private var searchQuery = ""

    init {
        loadUserData()
        loadStarredDogs()
        loadSharedDogs()
        loadDogs()
    }

    private fun loadUserData() = viewModelScope.launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
        _currentUser.value = userRepository.getUserName(uid) ?: ""
        _profileImageUrl.value = userRepository.getProfileImageUrl(uid) ?: ""
    }

    private fun loadStarredDogs() = viewModelScope.launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
        _starredDogs.value = userRepository.getStarredDogs(uid)!!
    }

    private fun loadSharedDogs() = viewModelScope.launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
        _sharedDogs.value = userRepository.getSharedDogs(uid)
    }

    private fun loadDogs() = viewModelScope.launch {
        val allDogsList = dogRepository.getDogs()
        val allDogsNotMine = allDogsList.filter { dog ->
            dog.owner_id != uid && (dog.status == "Adopt" || dog.status == "Buy")
        }

        allDogs = allDogsNotMine
        _filteredDogs.value = allDogs
    }

    fun toggleStarredDog(dogId: String) = viewModelScope.launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
        val updatedStarredDogs = _starredDogs.value.toMutableList()

        if (updatedStarredDogs.contains(dogId)) {
            updatedStarredDogs.remove(dogId)
        } else {
            updatedStarredDogs.add(dogId)
        }

        _starredDogs.value = updatedStarredDogs
        userRepository.updateStarredDogs(uid, updatedStarredDogs)
    }

    fun toggleSharedDog(dogId: String) = viewModelScope.launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
        val updatedSharedDogs = _sharedDogs.value.toMutableList()

        if (updatedSharedDogs.contains(dogId)) {
            updatedSharedDogs.remove(dogId)
            userRepository.removeSharedDog(uid, dogId)
            dogRepository.updateSharedBy(dogId, uid, add = false)
        } else {
            updatedSharedDogs.add(dogId)
            userRepository.addSharedDog(uid, dogId)
            dogRepository.updateSharedBy(dogId, uid, add = true)
        }

        _sharedDogs.value = updatedSharedDogs
    }

    fun updateQuery(query: String) {
        searchQuery = query
        applyFilters()
    }

    fun updateSelectedBreed(breed: String) {
        selectedBreed = breed
        applyFilters()
    }

    fun updateSelectedGender(gender: String) {
        selectedGender = gender
        applyFilters()
    }

    private fun applyFilters() {
        var filteredList = allDogs

        if (searchQuery.isNotBlank()) {
            filteredList = filteredList.filter { dog ->
                dog.name.contains(searchQuery, ignoreCase = true)
            }
        }

        if (selectedBreed.isNotBlank() && selectedBreed != "All") {
            filteredList = filteredList.filter { dog ->
                dog.breed.equals(selectedBreed, ignoreCase = true)
            }
        }

        if (selectedGender.isNotBlank() && selectedGender != "All") {
            filteredList = filteredList.filter { dog ->
                dog.gender.equals(selectedGender, ignoreCase = true)
            }
        }

        _filteredDogs.value = filteredList
    }

    fun navigateToPurchaseDescription(dogId: String) {
        _navigationEvent.value = "purchaseDescription/$dogId"
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

}
