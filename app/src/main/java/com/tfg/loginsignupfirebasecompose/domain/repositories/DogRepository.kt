package com.tfg.loginsignupfirebasecompose.domain.repositories

import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog

interface DogRepository {
    suspend fun getDogsByOwner(ownerId: String): List<Dog>
    suspend fun getDogs(): List<Dog>
    suspend fun updateSharedBy(dogId: String, uid: String, add: Boolean)
}