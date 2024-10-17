package com.tfg.loginsignupfirebasecompose.data.implementations

import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.data.Firebase.FirestoreCollections
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Establishment
import com.tfg.loginsignupfirebasecompose.domain.repositories.EstablishmentRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EstablishmentRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : EstablishmentRepository {

    override suspend fun getEstablishments(): List<Establishment> {
        return try {
            val result = db.collection(FirestoreCollections.establishments).get().await()

            result.documents.mapNotNull { document ->
                val data = document.data
                val name = data?.get("name") as? String ?: "Unknown"
                val coordinates = data?.get("coordinates") as? Map<String, Double> ?: emptyMap()
                val phone = data?.get("phone") as? String ?: ""
                val establishmentImage = data?.get("establishmentImage") as? String ?: ""
                val address = data?.get("adress") as? String ?: "Unknown"
                val owner_id = data?.get("owner_id") as? String ?: "Unknown"
                val liked_users = data?.get("likes") as? List<String> ?: emptyList()


                val latitude = coordinates["latitude"]
                val longitude = coordinates["longitude"]

                if (latitude != null && longitude != null) {
                    Establishment(
                        establishmentId = document.id,
                        name = name,
                        adress = address,
                        establishmentImage = establishmentImage,
                        coordinates = coordinates,
                        phone = phone,
                        owner_id = owner_id,
                        liked_users = liked_users,

                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener los establecimientos", e)
            emptyList()
        }
    }

    override suspend fun getEstablishmentById(establishmentId: String): Establishment? {
        Log.d("EstablishmentRepositoryImpl", "Fetching establishment details for ID: $establishmentId")

        return try {
            val querySnapshot = db.collection(FirestoreCollections.establishments)
                .document(establishmentId)
                .get()
                .await()

            if (querySnapshot.exists()) {
                val data = querySnapshot.data
                val name = data?.get("name") as? String ?: "Unknown"
                val coordinates = data?.get("coordinates") as? Map<String, Double> ?: emptyMap()
                val phone = data?.get("phone") as? String ?: "0"
                val establishmentImage = data?.get("establishmentImage") as? String ?: ""
                val address = data?.get("address") as? String ?: "Unknown"
                val ownerId = data?.get("owner_id") as? String ?: "Unknown"
                val liked_users = data?.get("likes") as? List<String> ?: emptyList()

                Establishment(
                    establishmentId = querySnapshot.id,
                    name = name,
                    adress = address,
                    establishmentImage = establishmentImage,
                    coordinates = coordinates,
                    phone = phone,
                    owner_id = ownerId,
                    liked_users = liked_users
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("EstablishmentRepository", "Error fetching establishment by ID", e)
            null
        }
    }


    override suspend fun updateEstablishment(
        userId: String,
        value: String,
        value1: String,
        value2: String,
        first: Double?,
        second: Double?
    ) {
        try {
            // Primero, obtenemos el documento del establecimiento con el userId
            val querySnapshot = db.collection(FirestoreCollections.establishments)
                .whereEqualTo("owner_id", userId)
                .get()
                .await()

            // Si se encuentra un documento, lo actualizamos
            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0] // Tomamos el primer documento si hay coincidencias

                // Creamos el mapa con los valores a actualizar
                val updates = mapOf(
                    "name" to value,
                    "address" to value1,
                    "phone" to value2,
                    "coordinates" to mapOf(
                        "latitude" to first,
                        "longitude" to second
                    )
                )

                // Actualizamos el documento
                db.collection(FirestoreCollections.establishments)
                    .document(document.id)
                    .update(updates)
                    .await()
            } else {
                Log.e("FirestoreError", "No se encontró ningún establecimiento para el usuario: $userId")
            }

        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al actualizar el establecimiento", e)
        }
    }

    override suspend fun removeFromLikes(establishmentId: String, userId: String) {
        try {
            db.collection(FirestoreCollections.establishments).document(establishmentId).update("likes", FieldValue.arrayRemove(userId)).await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al eliminar de likes", e)
        }
    }

    override suspend fun addToLikes(establishmentId: String, userId: String) {
        try {
            db.collection(FirestoreCollections.establishments).document(establishmentId).update("likes", FieldValue.arrayUnion(userId)).await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al eliminar de likes", e)
        }
    }

    override suspend fun getEstablishmentsByIds(likedIds: List<String>): List<Establishment> {
        return try {
            if (likedIds.isEmpty()) {
                return emptyList()
            }

            val result = db.collection(FirestoreCollections.establishments)
                .whereIn(FieldPath.documentId(), likedIds)
                .get()
                .await()

            result.documents.mapNotNull { document ->
                val data = document.data
                val name = data?.get("name") as? String ?: "Unknown"
                val coordinates = data?.get("coordinates") as? Map<String, Double> ?: emptyMap()
                val phone = data?.get("phone") as? String ?: ""
                val establishmentImage = data?.get("establishmentImage") as? String ?: ""
                val address = data?.get("address") as? String ?: "Unknown"
                val ownerId = data?.get("owner_id") as? String ?: "Unknown"
                val liked_users = data?.get("likes") as? List<String> ?: emptyList()

                val latitude = coordinates["latitude"]
                val longitude = coordinates["longitude"]

                if (latitude != null && longitude != null) {
                    Establishment(
                        establishmentId = document.id,
                        name = name,
                        adress = address,
                        establishmentImage = establishmentImage,
                        coordinates = coordinates,
                        phone = phone,
                        owner_id = ownerId,
                        liked_users = liked_users
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("EstablishmentRepositoryImpl", "Error al obtener establecimientos por IDs", e)
            emptyList() // En caso de error, devolvemos una lista vacía
        }
    }

}

