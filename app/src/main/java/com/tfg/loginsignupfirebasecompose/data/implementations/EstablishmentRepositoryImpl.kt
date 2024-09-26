package com.tfg.loginsignupfirebasecompose.data.implementations

import android.util.Log
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
            val result = db.collection(FirestoreCollections.establishments).get().await() // Esperamos la respuesta usando await()

            result.documents.mapNotNull { document ->
                val data = document.data
                val name = data?.get("name") as? String ?: "Unknown"
                val coordinates = data?.get("coordinates") as? Map<String, Double> ?: emptyMap()
                val phone = data?.get("phone") as? Long ?: 0
                val adress = data?.get("adress") as? String ?: "Unknown"
                val owner_id = data?.get("owner_id") as? String ?: "Unknown"
                val liked_users = data?.get("likes") as? List<String> ?: emptyList()


                val latitude = coordinates["latitude"]
                val longitude = coordinates["longitude"]

                if (latitude != null && longitude != null) {
                    Establishment(
                        name = name,
                        coordinates = coordinates,
                        phone = phone,
                        adress = adress,
                        owner_id = owner_id,
                        liked_users = liked_users,
                        establishmentId = document.id,

                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener los establecimientos", e)
            emptyList() // Devuelve una lista vacía en caso de error
        }
    }

    override suspend fun getEstablishmentById(establishmentId: String): Establishment? {
        Log.d("EstablishmentRepositoryImpl", "Fetching establishment details for ID: $establishmentId")

        return try {
            val document = db.collection(FirestoreCollections.establishments)
                .document(establishmentId)
                .get()
                .await() // await() para esperar el resultado de la llamada asíncrona

            if (document.exists()) {
                val data = document.data
                val name = data?.get("name") as? String ?: "Unknown"
                val coordinates = data?.get("coordinates") as? Map<String, Double> ?: emptyMap()
                val phone = data?.get("phone") as? Long ?: 0
                val address = data?.get("adress") as? String ?: "Unknown"
                val ownerId = data?.get("owner_id") as? String ?: "Unknown"
                val liked_users = data?.get("likes") as? List<String> ?: emptyList()

                Establishment(
                    name = name,
                    coordinates = coordinates,
                    phone = phone,
                    adress = address,
                    owner_id = ownerId,
                    liked_users = liked_users,
                    establishmentId = document.id
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("EstablishmentRepository", "Error fetching establishment by ID", e)
            null
        }
    }
}

