package com.tfg.loginsignupfirebasecompose.data.collectionsData

data class User(
    val uid: String,
    val name: String,
    val email: String,
    val dogs: List<Dog>
) {

}
