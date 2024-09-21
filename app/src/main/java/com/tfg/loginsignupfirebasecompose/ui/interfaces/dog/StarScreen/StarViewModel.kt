package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.StarScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarViewModel @Inject constructor (
    private val userRepository: UserRepository,
    private val dogRepository: DogRepository,
    private val authRepository: AuthRepository

): ViewModel(){
    fun toggleStarredDog(dogId: String) {
        TODO("Not yet implemented")
    }

    private val _starredDogs = MutableStateFlow<List<Dog>>(emptyList())
    val starredDogs: StateFlow<List<Dog>> = _starredDogs

    private val _savedDogs = MutableStateFlow<List<Dog>>(emptyList())
    val savedDogs: StateFlow<List<Dog>> = _savedDogs

    val dog = Dog(
        name = "Max",
        gender = "Male",
        breed = "Labrador Retriever",
        price = 250,
        age = 3,
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3d/Bernese_Mountain_Dog_Miedzynarodowa_wystawa_psow_rasowych_rybnik_kamien_pazdziernik_2011_1.jpg/1200px-Bernese_Mountain_Dog_Miedzynarodowa_wystawa_psow_rasowych_rybnik_kamien_pazdziernik_2011_1.jpg"

    )
    val listDog:List<Dog> = listOf(dog)

    init {
        fetchDog()
    }

    private fun fetchDog() {
        viewModelScope.launch {
            _savedDogs.value = listDog
            Log.d("HomeViewModel", "Dogs: ${_savedDogs.value}")

        }
    }
}