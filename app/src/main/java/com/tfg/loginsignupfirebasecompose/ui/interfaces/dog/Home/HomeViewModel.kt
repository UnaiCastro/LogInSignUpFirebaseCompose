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
) : ViewModel()
{

    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    // Estado de los perros filtrados
    private val _filteredDogs = MutableStateFlow<List<Dog>>(emptyList())
    val filteredDogs: StateFlow<List<Dog>> = _filteredDogs

    // Estado de los perros guardados y compartidos
    private val _starredDogs = MutableStateFlow<List<String>>(emptyList())
    val starredDogs: StateFlow<List<String>> = _starredDogs

    private val _sharedDogs = MutableStateFlow<List<String>>(emptyList())
    val sharedDogs: StateFlow<List<String>> = _sharedDogs

    // Estado de la imagen de perfil y el usuario actual
    private val _profileImageUrl = MutableStateFlow<String>("")
    val profileImageUrl: StateFlow<String> = _profileImageUrl

    private val _currentUser = MutableStateFlow<String>("")
    val currentUser: StateFlow<String> = _currentUser

    private var allDogs = listOf<Dog>() // Almacena todos los perros no filtrados
    private var selectedBreed = ""
    private var selectedGender = ""
    private var searchQuery = ""

    init {
        loadUserData()
        loadStarredDogs()
        loadSharedDogs()
        loadDogs()
    }

    // Cargar información del usuario actual
    private fun loadUserData() = viewModelScope.launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
        _currentUser.value = userRepository.getUserName(uid) ?: ""
        _profileImageUrl.value = userRepository.getProfileImageUrl(uid) ?: ""
    }

    // Cargar los perros guardados (favoritos)
    private fun loadStarredDogs() = viewModelScope.launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
        _starredDogs.value = userRepository.getStarredDogs(uid)!!
    }

    // Cargar los perros compartidos
    private fun loadSharedDogs() = viewModelScope.launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
        _sharedDogs.value = userRepository.getSharedDogs(uid)
    }

    // Cargar todos los perros
    private fun loadDogs() = viewModelScope.launch {
        val allDogs = dogRepository.getDogs()
        val allDogsNotMine = mutableListOf<Dog>()

        for (dog in allDogs) {
            println("Dog: dog.name = ${dog.name}")
            if (dog.owner_id != uid) {
                allDogsNotMine.add(dog)
            }
        }

        _filteredDogs.value = allDogs // Mostrar solo perros que no son míos
    }

    // Actualizar la lista de perros guardados
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

    // Actualizar el estado del perro compartido
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

    // Función para actualizar los filtros seleccionados
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
        // Filtrar la lista según los criterios seleccionados
        var filteredList = allDogs

        // Filtro por nombre
        if (searchQuery.isNotBlank()) {
            filteredList = filteredList.filter { dog ->
                dog.name.contains(searchQuery, ignoreCase = true)
            }
        }

        // Filtro por raza (si hay una raza seleccionada)
        if (selectedBreed.isNotBlank() && selectedBreed != "All") {
            filteredList = filteredList.filter { dog ->
                dog.breed.equals(selectedBreed, ignoreCase = true)
            }
        }

        // Filtro por género (si hay un género seleccionado)
        if (selectedGender.isNotBlank() && selectedGender != "Todos") {
            filteredList = filteredList.filter { dog ->
                dog.gender.equals(selectedGender, ignoreCase = true)
            }
        }

        _filteredDogs.value = filteredList
    }

    fun navigateToPurchaseDescription(dogId: String) {
        _navigationEvent.value = "purchaseDescription/$dogId"
    }

    fun navigateToChat(dogId: String) {
        viewModelScope.launch {
            val userId= currentUser
            val dog = dogRepository.getDogById(dogId)
            val createdChat:String= chatRepository.isCreatedChat(userId,dogId, dog!!.owner_id)
            _navigationEvent.value = "chat/$createdChat"
        }

    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

}
