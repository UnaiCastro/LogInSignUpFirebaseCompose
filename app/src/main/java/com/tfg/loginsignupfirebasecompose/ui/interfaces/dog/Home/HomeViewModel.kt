package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val dogRepository: DogRepository
) : ViewModel()
{

    // Variables para filtros
    private val _query = MutableStateFlow<String>("")
    private val _selectedGender = MutableStateFlow<String>("")
    private val _selectedBreed = MutableStateFlow<String>("")
*/
/*
    private val _selectedFilter = MutableStateFlow<String>("All")
*//*


    val uid: String by mutableStateOf(authRepository.getCurrentUser()?.uid ?: "")

    private val _currentUser = MutableStateFlow<String>("")
    val currentUser: StateFlow<String> = _currentUser

    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl: StateFlow<String?> = _profileImageUrl

    private val _dogs = MutableStateFlow<List<Dog>>(emptyList())
    val dogs: StateFlow<List<Dog>> = _dogs

    private val _starredDogs = MutableStateFlow<List<String>>(emptyList())
    val starredDogs: StateFlow<List<String>> = _starredDogs

    private val _sharedDogs = MutableStateFlow<List<String>>(emptyList()) // Estado de perros compartidos
    val sharedDogs: StateFlow<List<String>> = _sharedDogs

    private val _filteredDogs = MutableStateFlow<List<Dog>>(emptyList()) // Estado para perros filtrados
    val filteredDogs: StateFlow<List<Dog>> = _filteredDogs.asStateFlow()


    init {
        if (uid.isNotEmpty()) {
            getUserName()
            fetchProfileImage()
            fetchDogs()
            fetchStarredDogs() // Cargar perros favoritos
            fetchSharedDogs()
        }
    }

    private fun fetchStarredDogs() {
        viewModelScope.launch {
            val starred = userRepository.getStarredDogs(uid) // Obtener la lista de IDs de perros favoritos
            _starredDogs.value = starred ?: emptyList()
            Log.d("HomeViewModel", "Starred dogs: ${_starredDogs.value}")
            applyFilters()
        }
    }
    private fun fetchSharedDogs() {
        viewModelScope.launch {
            val userSharedDogs = userRepository.getSharedDogs(uid)
            _sharedDogs.value = userSharedDogs
            Log.d("HomeViewModel", "Shared dogs: ${_sharedDogs.value}")
            applyFilters()
        }
    }


    private fun fetchDogs() {
        viewModelScope.launch {
            _dogs.value=dogRepository.getDogs()
            applyFilters()
            Log.d("HomeViewModel", "Dogs: ${_dogs.value}")
        }
    }

    private fun fetchProfileImage() {
        viewModelScope.launch {
            val imageUrl = userRepository.getProfileImageUrl(uid)
            _profileImageUrl.value = imageUrl
            Log.d("HomeViewModel", "Profile image URL: $imageUrl")
        }
    }

    private fun getUserName() {
        viewModelScope.launch {
            val name = userRepository.getUserName(uid)
            _currentUser.value = (name ?: "Usuario desconocido").toString()
            Log.d("HomeViewModel", "User name: ${_currentUser.value}")
        }
    }


    fun toggleStarredDog(dogId: String) {
        viewModelScope.launch {
            val currentStarred = _starredDogs.value.toMutableList()
            if (currentStarred.contains(dogId)) {
                currentStarred.remove(dogId)
            } else {
                currentStarred.add(dogId)
            }
            userRepository.updateStarredDogs(uid, currentStarred)
            _starredDogs.value = currentStarred.toList() // Forzamos la recomposición
            updateDogStatus(dogId, isStarred = true)
            Log.d("HomeViewModel", "Toggled starred dog: ${_starredDogs.value}")
            applyFilters()
        }
    }


    fun isDogStarred(dogId: String): Boolean {
        return _starredDogs.value.contains(dogId)
        Log.d("HomeViewModel", "Is dog starred: ${_starredDogs.value.contains(dogId)}")
    }

    fun toggleSharedDog(dogId: String) {
        viewModelScope.launch {
            val currentShared = _sharedDogs.value.toMutableList()
            if (currentShared.contains(dogId)) {
                currentShared.remove(dogId)
            } else {
                currentShared.add(dogId)
            }

            // Crea una nueva lista para asegurar que Compose detecte el cambio
            _sharedDogs.value = currentShared.toList()

            Log.d("HomeViewModel", "Toggled shared dog: ${_sharedDogs.value}")

            // Actualizar en Firestore
            if (_sharedDogs.value.contains(dogId)) {
                userRepository.addSharedDog(uid, dogId)
                dogRepository.updateSharedBy(dogId, uid, add = true)
            } else {
                userRepository.removeSharedDog(uid, dogId)
                dogRepository.updateSharedBy(dogId, uid, add = false)
            }
            updateDogStatus(dogId, isShared = true)
            Log.d("HomeViewModel", "Toggled shared dog: ${_sharedDogs.value}")
            applyFilters()

        }
    }

    fun isDogShared(dogId: String): Boolean {
        return _sharedDogs.value.contains(dogId)
        Log.d("HomeViewModel", "Is dog shared: ${_sharedDogs.value.contains(dogId)}")
    }

    private fun updateDogStatus(dogId: String, isStarred: Boolean = false, isShared: Boolean = false) {
        val updatedDogs = _dogs.value.map { dog ->
            if (dog.dogId == dogId) {
                dog.copy(
                    // Aquí puedes agregar más lógica si necesitas actualizar más estados, por ejemplo:
                    // si deseas cambiar algún campo del perro cuando se guarde o comparta.
                    shared_dog_userId = if (isShared) {
                        dog.shared_dog_userId + uid // Agregar usuario que ha compartido el perro
                    } else {
                        dog.shared_dog_userId.filterNot { it == uid } // Eliminar si ya no se comparte
                    }
                )
            } else dog
        }
        _dogs.value = updatedDogs
    }


    fun applyFilters() {
        viewModelScope.launch {
            val filteredList = _dogs.value.filter { dog ->
                (_query.value.isEmpty() || dog.name.contains(_query.value, ignoreCase = true)) &&
                        (_selectedGender.value == "All" || _selectedGender.value.isEmpty() || dog.gender == _selectedGender.value) && // Manejo del filtro "All" para género
                        (_selectedBreed.value == "All" || _selectedBreed.value.isEmpty() || dog.breed == _selectedBreed.value) // Manejo del filtro "All" para raza
            }
            _filteredDogs.value = filteredList
            Log.d("HomeViewModel", "Filtered dogs: ${_filteredDogs.value}")
        }
    }

    fun updateQuery(query: String) {
        _query.value = query
        applyFilters()
        Log.d("HomeViewModel", "Updated query: ${_query.value}")
    }

    fun updateSelectedBreed(breed: String) {
        _selectedBreed.value = breed
        applyFilters()
        Log.d("HomeViewModel", "Updated selected breed: ${_selectedBreed.value}")
    }

    fun updateSelectedGender(gender: String) {
        _selectedGender.value = gender
        applyFilters()
        Log.d("HomeViewModel", "Updated selected gender: ${_selectedGender.value}")
    }

}*/


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dogRepository: DogRepository,
    private val authRepository: AuthRepository, // Supongo que usas Firebase Auth para identificar al usuario
) : ViewModel()
{

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
        viewModelScope.launch {
            allDogs = dogRepository.getDogs()
            _filteredDogs.value = allDogs // Mostrar todos los perros por defecto
        }
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
        _filteredDogs.value = dogRepository.getDogs()
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
}
