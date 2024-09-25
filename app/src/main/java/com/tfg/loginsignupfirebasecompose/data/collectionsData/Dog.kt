package com.tfg.loginsignupfirebasecompose.data.collectionsData

data class Dog(
    var dogId: String = "",
    val name: String = "",
    val breed: String = "",
    val age: Int = 0,
    val gender: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val owner_id: String = "",
    val status: String = "",
    val price: Int = 0,
    val shared_dog_userId: List<String> = emptyList() // Lista vacía por defecto
)

