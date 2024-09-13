package com.tfg.loginsignupfirebasecompose.data.collectionsData

data class Dog(
    val name: String,
    val breed: String,
    val age: Int,
    val gender: String,
    val description: String,
    val imageUrl: String,
    val owner_id:String,
    val status:String,
    val price:Double,
    val shared_dog_userIdi:List<String> // Lista de identificadores de usuarios que compartieron el perro
)
