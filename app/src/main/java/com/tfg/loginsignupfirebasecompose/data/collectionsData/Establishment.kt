package com.tfg.loginsignupfirebasecompose.data.collectionsData

data class Establishment(
    val establishmentId: String,
    val name: String,
    val adress: String,
    val establishmentImage: String,
    val coordinates: Map<String, Double>,
    val phone: String,
    val owner_id: String,
    val liked_users: List<String>
)
