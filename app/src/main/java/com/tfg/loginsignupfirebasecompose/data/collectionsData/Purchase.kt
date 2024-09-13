package com.tfg.loginsignupfirebasecompose.data.collectionsData

data class Purchase(
    val dog_id: String,
    val seller_id: String,
    val buyer_id: String,
    val date: String,
    val price: Double
)
