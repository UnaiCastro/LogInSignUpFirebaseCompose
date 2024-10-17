package com.tfg.loginsignupfirebasecompose.domain.repositories

import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog

interface DogRepository {
    suspend fun getDogsByOwner(ownerId: String): List<Dog>
    suspend fun getDogs(): List<Dog>
    suspend fun updateSharedBy(dogId: String, uid: String, add: Boolean)
    suspend fun getDogById(dogId: String): Dog?
    suspend fun uploadDog(dog:Dog)
    suspend fun adoptOrBuy(dogId: String, uid: String, status: String)
    suspend fun deleteDog(dogId: String)
    suspend fun getDogsByIds(sharedDogIds: List<String>): List<Dog>
}