package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.LikesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Establishment
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.EstablishmentRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LikesViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val establishmentRepository: EstablishmentRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser

    init {
        viewModelScope.launch {
            _currentUser.value = authRepository.getCurrentUser()?.uid
            getLikedEstablishments(_currentUser.value!!)
        }
    }

    private val _likedEstablishments = MutableStateFlow<List<Establishment>>(emptyList())
    val likedEstablishments: StateFlow<List<Establishment>> = _likedEstablishments

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun getLikedEstablishments(userId: String) {
        viewModelScope.launch {
            val user = userRepository.getUserDetailsById(userId)
            val likedIds = user?.likedEstablishments ?: emptyList()
            _likedEstablishments.value = establishmentRepository.getEstablishmentsByIds(likedIds)
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
