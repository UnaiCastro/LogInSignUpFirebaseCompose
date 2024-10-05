package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Explore

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Establishment
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.EstablishmentRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val establishmentRepository: EstablishmentRepository
) : ViewModel() {

    var establishments by mutableStateOf<List<Establishment>>(emptyList())
        private set

    init {
        fetchEstablishments()
    }

    private fun fetchEstablishments() {
        viewModelScope.launch {
            try {
                establishments = establishmentRepository.getEstablishments()
            } catch (e: Exception) {
                Log.e("ExploreViewModel", "Error fetching establishments", e)
            }
        }
    }

}