package com.tfg.loginsignupfirebasecompose.data.collectionsData

data class Establishment(
    val establishmentId: String,
    val name: String,
    val adress: String,
    val coordinates: Map<String, Double>,
    val phone: Long,
    val owner_id: String,
    val liked_users: List<String> // Lista de identificadores de usuarios que han dado me gusta
)
