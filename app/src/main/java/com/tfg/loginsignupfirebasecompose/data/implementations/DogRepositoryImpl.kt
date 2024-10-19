package com.tfg.loginsignupfirebasecompose.data.implementations

import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.data.Firebase.FirestoreCollections
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class DogRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : DogRepository
{
    override suspend fun getDogsByOwner(ownerId: String): List<Dog> {
        return try {
            val querySnapshot = db.collection(FirestoreCollections.dogs)
                .whereEqualTo("owner_id", ownerId)
                .get()
                .await()
            querySnapshot.documents.map { document ->
                val dog = document.toObject(Dog::class.java)
                dog?.dogId = document.id
                dog
            }.filterNotNull()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener los perros", e)
            emptyList()
        }
    }

    override suspend fun getDogs(): List<Dog> {
        return try {
            val querySnapshot = db.collection(FirestoreCollections.dogs)
                .get()
                .await()
            querySnapshot.documents.map { document ->
                val dog = document.toObject(Dog::class.java)
                dog?.apply {
                    this.dogId = document.id
                } ?: throw IllegalStateException("Error al convertir el documento a Dog")
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener los perros", e)
            emptyList()
        }
    }

    override suspend fun updateSharedBy(dogId: String, uid: String, add: Boolean) {
        val dogRef = db.collection(FirestoreCollections.dogs).document(dogId)

        try {
            db.runTransaction { transaction ->
                val snapshot = transaction.get(dogRef)

                val sharedBy = snapshot.get("shared_dog_userId") as? MutableList<String> ?: mutableListOf()

                if (add) {
                    if (!sharedBy.contains(uid)) {
                        sharedBy.add(uid)
                    }
                } else {
                    sharedBy.remove(uid)
                }

                transaction.update(dogRef, "shared_dog_userId", sharedBy)
            }.await()
        } catch (e: Exception) {
            Log.e("DogRepository", "Error updating sharedBy field: ${e.message}")
        }
    }

    override suspend fun getDogById(dogId: String): Dog? {
        return try {
            val document = db.collection(FirestoreCollections.dogs).document(dogId).get().await()

            if (document.exists()) {
                document.toObject(Dog::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error fetching user details for userId: $dogId", e)
            null
        }
    }

    override suspend fun uploadDog(
        dog:Dog
    ) {
        try {
            val dogData = mapOf(
                "name" to dog.name,
                "gender" to dog.gender,
                "breed" to dog.breed,
                "description" to dog.description,
                "age" to dog.age,
                "price" to dog.price,
                "status" to dog.status,
                "owner_id" to dog.owner_id,
                "shared_dog_userId" to dog.shared_dog_userId,
                "profileImageUrl" to dog.profileImageUrl
            )
            db.collection(FirestoreCollections.dogs).add(dogData).await()
            Log.d("DogRepository", "Dog uploaded successfully")
        } catch (e: Exception) {
            Log.e("DogRepository", "Error uploading dog: ${e.message}")
        }
    }

    override suspend fun adoptOrBuy(dogId: String, uid: String, status: String) {
        Log.d("DogRepository", "Adopting or buying dog with ID: $dogId, UID: $uid, Status: $status")
        try {
            db.collection(FirestoreCollections.dogs).document(dogId).update("owner_id", uid).await()
            if (status == "Adopt") {
                db.collection(FirestoreCollections.dogs).document(dogId).update("status", "Adopted").await()

            }
            if (status == "Buy") {
                db.collection(FirestoreCollections.dogs).document(dogId).update("status", "Bought").await()
            }
        } catch (e: Exception) {
            Log.e("DogRepository", "Error adopting or buying dog: ${e.message}")
        }
    }

    override suspend fun deleteDog(dogId: String) {
        try {
            db.collection(FirestoreCollections.dogs).document(dogId).delete().await()
        } catch (e: Exception) {
            Log.e("DogRepository", "Error deleting dog: ${e.message}")
        }
    }

    override suspend fun getDogsByIds(sharedDogIds: List<String>): List<Dog> {
        return try {
            val dogs = mutableListOf<Dog>()
            val snapshot = db.collection(FirestoreCollections.dogs)
                .whereIn(FieldPath.documentId(), sharedDogIds)
                .get()
                .await()

            for (document in snapshot.documents) {
                val dog = document.toObject(Dog::class.java)
                dog?.let {
                    it.dogId = document.id
                    dogs.add(it)
                }
            }
            dogs
        } catch (e: Exception) {
            Log.e("DogRepository", "Error al obtener perros por ID", e)
            emptyList()
        }
    }

    override suspend fun getDogDetailsById(dogId: String): Dog? {
        return try {
            val document = db.collection(FirestoreCollections.dogs).document(dogId).get().await()
            if (document.exists()) {
                document.toObject(Dog::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error fetching dog details for dogId: $dogId", e)
            null
        }
    }

}