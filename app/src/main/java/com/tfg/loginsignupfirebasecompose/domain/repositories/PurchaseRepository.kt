package com.tfg.loginsignupfirebasecompose.domain.repositories

interface PurchaseRepository {
    suspend fun newPurchase(dogId: String, price: Int, uid: String, userId: String)
}