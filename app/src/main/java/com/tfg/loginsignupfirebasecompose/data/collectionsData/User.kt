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
    val likedEstablishments: List<String> = emptyList(), // Lista de identificadores de establecimientos dados me gusta
    val starred_dogs: List<String> = emptyList(), // Lista de identificadores de perros marcados como favoritos
    val sharedDogs: List<String> = emptyList(), // Lista de identificadores de perros compartidos
    val chat_rooms: List<String> = emptyList(), // Lista de identificadores de salas de chat
    val dogs: List<String> = emptyList() // Lista de perros del usuario
)

