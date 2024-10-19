package com.tfg.loginsignupfirebasecompose.data.collectionsData

data class User(
    var userId: String = "",
    val name: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val type: String = "",
    val address: String = "",
    val phone: String = "",
    val region: String = "",
    val likedEstablishments: List<String> = emptyList(),
    val starred_dogs: List<String> = emptyList(),
    val sharedDogs: List<String> = emptyList(),
    val chat_rooms: List<String> = emptyList(),
    val dogs: List<String> = emptyList()
)

