package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.uploadDog

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.data.collectionsData.User
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadDogViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dogRepository: DogRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var name: String by mutableStateOf("")
    var gender by mutableStateOf("")
    var breed by mutableStateOf("")
    var description by mutableStateOf("")
    var age by mutableStateOf("")
    var price by mutableStateOf("")
    var status by mutableStateOf("")

    val uid: String by mutableStateOf(authRepository.getCurrentUser()?.uid ?: "")
    private val _currentUser = MutableStateFlow<User>(User())
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()


    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = userRepository.getUserDetailsById(uid) ?: User()
        }
    }

    private fun getDogImageByBreed(breed: String): String {
        return when (breed) {
            "Beagle" -> "https://plus.unsplash.com/premium_photo-1663127048434-84db6f90f08d?q=80&w=1914&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Bichon Frise" -> "https://images.unsplash.com/photo-1728154638526-5b795e1bb017?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Border Collie" -> "https://images.unsplash.com/photo-1708787786119-9bcade6ffa21?q=80&w=2060&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Boxer" -> "https://images.unsplash.com/photo-1633949831162-5a03f2c7a59a?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Bulldog" -> "https://plus.unsplash.com/premium_photo-1722859221349-26353eae4744?q=80&w=1936&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Bullmastiff" -> "https://images.unsplash.com/photo-1677899997050-950369b117b2?q=80&w=1791&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Cavalier King Charles Spaniel" -> "https://images.unsplash.com/photo-1556392210-ed91a8ad0cf6?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Chihuahua" -> "https://plus.unsplash.com/premium_photo-1661861069475-fea636a949e6?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Cocker Spaniel" -> "https://images.unsplash.com/photo-1515597849219-88a19d5f13f9?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Dachshund" -> "https://images.unsplash.com/photo-1504502080150-7ac132968c9e?q=80&w=2060&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Dalmatian" -> "https://plus.unsplash.com/premium_photo-1664303507729-8e7589fccf5c?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Doberman" -> "https://images.unsplash.com/photo-1536677075446-821da10993a6?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "English Setter" -> "https://images.unsplash.com/photo-1605293311220-e753718cb345?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "French Bulldog" -> "https://images.unsplash.com/photo-1531842477197-54acf89bff98?q=80&w=1965&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "German Shepherd" -> "https://images.unsplash.com/photo-1649923625148-1e13d9431053?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Golden Retriever" -> "https://plus.unsplash.com/premium_photo-1694819488591-a43907d1c5cc?q=80&w=1914&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Great Dane" -> "https://images.unsplash.com/photo-1658829157646-6549202f68bf?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8Z3JlYXQlMjBkYW5lfGVufDB8fDB8fHww"
            "Greyhound" -> "https://plus.unsplash.com/premium_photo-1664303510081-a7c56cd8f25e?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Husky" -> "https://images.unsplash.com/photo-1522971901479-aa43436c3929?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Jack Russell Terrier" -> "https://images.unsplash.com/photo-1601375461501-8489ed54b141?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Labrador Retriever" -> "https://images.unsplash.com/photo-1537204696486-967f1b7198c8?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            "Maltese" -> "https://unsplash.com/s/photos/maltese-dog"
            "Mastiff" -> "https://unsplash.com/s/photos/mastiff"
            "Miniature Schnauzer" -> "https://unsplash.com/s/photos/miniature-schnauzer"
            "Poodle" -> "https://unsplash.com/s/photos/poodle"
            "Pomeranian" -> "https://unsplash.com/s/photos/pomeranian"
            "Pug" -> "https://unsplash.com/s/photos/pug"
            "Rottweiler" -> "https://unsplash.com/s/photos/rottweiler"
            "Shih Tzu" -> "https://unsplash.com/s/photos/shih-tzu"
            "Staffordshire Bull Terrier" -> "https://unsplash.com/s/photos/staffordshire-bull-terrier"
            "Yorkshire Terrier" -> "https://unsplash.com/s/photos/yorkshire-terrier"
            else -> "https://unsplash.com/s/photos/dog"
        }
    }


    fun uploadDog(
        name: String,
        gender: String,
        breed: String,
        description: String,
        age: Int,
        price: Int,
        status: String,
    ) {
        viewModelScope.launch {
            val imageUrl = getDogImageByBreed(breed)

            val dog = Dog(
                name = name,
                gender = gender,
                breed = breed,
                description = description,
                age = age,
                price = price,
                status = status,
                profileImageUrl = imageUrl,
                owner_id = uid
            )

            dogRepository.uploadDog(dog = dog)
            Log.d("UploadDogViewModel", "Dog uploaded successfully")

        }
    }


}