package com.tfg.loginsignupfirebasecompose.data.collectionsData

data class User(
    val userId: String,
    val name: String,
    val email: String,
    val profileImageUrl: String,
    val type: String, // Tipo de usuario (particular o empresa)
    val adress:String,
    val phone:String,
    val coordinates: Map<String, Any>, // Coordenadas del usuario
    val likedEstablishments: List<String>, // Lista de identificadores de establecimientos dados me gusta
    val starredDogs: List<String>, // Lista de identificadores de perros marcados como favoritos
    val sharedDogs: List<String>, // Lista de identificadores de perros compartidos
    val chat_rooms: List<String>, // Lista de identificadores de salas de chat
    val dogs: List<Dog> // Lista de perros del usuario
) {

}
