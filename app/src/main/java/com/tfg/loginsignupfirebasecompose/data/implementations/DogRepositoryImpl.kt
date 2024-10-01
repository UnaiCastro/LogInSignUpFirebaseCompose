package com.tfg.loginsignupfirebasecompose.data.implementations

import android.util.Log
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
                .await() // Espera el resultado de la llamada asíncrona
            querySnapshot.toObjects(Dog::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener los perros", e)
            emptyList() // Si hay un error, retornamos una lista vacía
        }
    }

    override suspend fun getDogs(): List<Dog> {
        return try {
            val querySnapshot = db.collection(FirestoreCollections.dogs)
                .get()
                .await() // Espera el resultado de la llamada asíncrona
            querySnapshot.documents.map { document ->
                val dog = document.toObject(Dog::class.java)
                dog?.apply {
                    // Aquí añadimos el ID del documento al objeto Dog
                    this.dogId = document.id
                } ?: throw IllegalStateException("Error al convertir el documento a Dog")
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener los perros", e)
            emptyList() // Si hay un error, retornamos una lista vacía
        }
    }

    override suspend fun updateSharedBy(dogId: String, uid: String, add: Boolean) {
        val dogRef = db.collection(FirestoreCollections.dogs).document(dogId)

        try {
            db.runTransaction { transaction ->
                val snapshot = transaction.get(dogRef)

                // Obtener la lista actual de 'sharedBy', si no existe, crear una lista vacía
                val sharedBy = snapshot.get("shared_dog_userId") as? MutableList<String> ?: mutableListOf()

                if (add) {
                    // Añadir el ID del usuario si no está ya en la lista
                    if (!sharedBy.contains(uid)) {
                        sharedBy.add(uid)
                    }
                } else {
                    // Eliminar el ID del usuario de la lista
                    sharedBy.remove(uid)
                }

                // Actualizar el campo 'sharedBy' en Firestore
                transaction.update(dogRef, "shared_dog_userId", sharedBy)
            }.await() // Asegúrate de manejar las coroutines
        } catch (e: Exception) {
            // Aquí puedes manejar el error, por ejemplo, registrarlo o mostrar un mensaje al usuario
            Log.e("DogRepository", "Error updating sharedBy field: ${e.message}")
        }
    }

    override suspend fun getDogById(dogId: String): Dog? {
        return try {
            // Referencia a la colección de usuarios en Firestore
            val document = db.collection(FirestoreCollections.dogs).document(dogId).get().await()

            // Si el documento existe, mapeamos los datos al objeto User
            if (document.exists()) {
                document.toObject(Dog::class.java) // Convierte el documento en un objeto User
            } else {
                null // En caso de que no exista el documento, devolvemos null
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error fetching user details for userId: $dogId", e)
            null // En caso de error, devolvemos null
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
                "profileImageUrl" to dog.imageUrl
            )
            db.collection(FirestoreCollections.dogs).add(dogData).await()
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

}