package com.tfg.loginsignupfirebasecompose.data.collectionsData

data class Establlishment(
    val name: String,
    val adress: String,
    val coordinates: Map<String, Double>,
    val phone: String,
    val owner_id: String,
    val liked_users: List<String> // Lista de identificadores de usuarios que han dado me gusta
)
