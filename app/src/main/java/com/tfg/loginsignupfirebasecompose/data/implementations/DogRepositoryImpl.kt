package com.tfg.loginsignupfirebasecompose.data.implementations

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.data.FirestoreCollections
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

}