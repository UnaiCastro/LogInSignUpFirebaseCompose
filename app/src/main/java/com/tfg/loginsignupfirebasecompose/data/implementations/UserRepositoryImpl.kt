package com.tfg.loginsignupfirebasecompose.data.implementations

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.data.FirestoreCollections
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : UserRepository {

    override suspend fun getUserName(uid: String): String? {
        return try {
            val document = db.collection(FirestoreCollections.users).document(uid).get().await()
            document.getString("name")
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener el nombre del usuario", e)
            null
        }
    }

    override suspend fun saveUser(uid: String, user: Map<String, Any>): Result<Unit> {
        return try {
            db.collection(FirestoreCollections.users).document(uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}