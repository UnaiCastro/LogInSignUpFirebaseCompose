package com.tfg.loginsignupfirebasecompose.data.implementations

import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.data.FirestoreCollections
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Chat
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.data.collectionsData.User
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import kotlinx.coroutines.tasks.await
import java.io.Serializable
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : UserRepository
{

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

    override suspend fun getEmail(uid: String): String? {
        return try {
            val document = db.collection(FirestoreCollections.users).document(uid).get().await()
            document.getString("email")
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener el email del usuario", e)
            null
        }
    }

    override suspend fun getProfileImageUrl(uid: String): String? {
        return try {
            val document = db.collection(FirestoreCollections.users).document(uid).get().await()
            document.getString("profileImageUrl")
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener la URL de la imagen de perfil", e)
            null
        }
    }

    override suspend fun getChatsByIds(chatRoomIds: List<String>): List<Chat> {
        return try {

            val snapshot = db.collection("chats")
                .whereIn(FieldPath.documentId(), chatRoomIds)
                .get()
                .await()

            snapshot.documents.map { document ->
                val timestamp = document.getTimestamp("created_at")?.toDate()?.time
                    ?: System.currentTimeMillis()
                Chat(

                    user1id = document.getString("user1id") ?: "",
                    user2id = document.getString("user2id") ?: "",
                    lastMessage = document.getString("last_message") ?: "",
                    created_at = timestamp
                )
            }


        } catch (e: Exception) {
            Log.e("ChatRepository", "Error fetching chats", e)
            emptyList()
        }
    }

    override suspend fun getUserChatRooms(userId: String): List<String> {
        return try {
            val document = db.collection(FirestoreCollections.users)
                .document(userId)
                .get()
                .await()

            document.get("chat_rooms") as? List<String> ?: emptyList()
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error fetching chat rooms", e)
            emptyList()
        }
    }

    override suspend fun getUserDetailsById(userId: String): User? {
        return try {
            // Referencia a la colección de usuarios en Firestore
            val document = db.collection("users").document(userId).get().await()

            // Si el documento existe, mapeamos los datos al objeto User
            if (document.exists()) {
                val user = document.toObject(User::class.java) // Convierte el documento en un objeto User
                user?.let {
                    it.userId = document.id // Asignamos el id del documento al objeto User
                }
                user
            } else {
                null // En caso de que no exista el documento, devolvemos null
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error fetching user details for userId: $userId", e)
            null // En caso de error, devolvemos null
        }
    }


    override suspend fun getDogDetailsById(dogId: String): Dog? {
        return try {
            // Referencia a la colección de perros en Firestore
            val document = db.collection("dogs").document(dogId).get().await()

            // Si el documento existe, mapeamos los datos al objeto Dog
            if (document.exists()) {
                document.toObject(Dog::class.java) // Convierte el documento en un objeto Dog
            } else {
                null // En caso de que no exista el documento, devolvemos null
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error fetching dog details for dogId: $dogId", e)
            null // En caso de error, devolvemos null
        }
    }

    override suspend fun saveEstablishment(
        uid: String,
        establishment: HashMap<String, Serializable>
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getStarredDogs(uid: String): List<String> {
        return try {
            val document = db.collection(FirestoreCollections.users).document(uid).get().await()
            document.get("starred_dogs") as? List<String>
                ?: emptyList()  // Retorna una lista vacía si el valor es null o no es del tipo esperado
        } catch (e: Exception) {
            Log.e(
                "FirestoreError",
                "Error al obtener los perros favoritos para el usuario: $uid",
                e
            )
            emptyList()  // Retorna una lista vacía en caso de error
        }
    }

    override suspend fun updateStarredDogs(uid: String, currentStarred: MutableList<String>) {
        try {
            val documentRef = db.collection("users").document(uid)
            val documentSnapshot = documentRef.get().await()

            if (documentSnapshot.exists()) {
                documentRef.update("starred_dogs", currentStarred).await()
            } else {
                // Handle the case where the document does not exist
                // For example, you might want to create a new document or log an error
                Log.w("Firestore", "Document with UID $uid does not exist.")
            }
        } catch (e: Exception) {
            // Log the exception or handle it as needed
            Log.e("Firestore", "Error updating starred dogs", e)
        }
    }

    override suspend fun getSharedDogs(uid: String): List<String> {
        return try {
            val userSnapshot = db.collection(FirestoreCollections.users).document(uid).get().await()
            userSnapshot.get("sharedDogs") as? List<String> ?: emptyList()
        } catch (e: Exception) {
            Log.e(
                "FirestoreError",
                "Error al obtener los perros compartidos para el usuario: $uid",
                e
            )
            return emptyList()
        }

    }

    override suspend fun removeSharedDog(uid: String, dogId: String) {
        try {
            db
                .collection(FirestoreCollections.users)
                .document(uid)
                .update(
                    "sharedDogs",
                    FieldValue.arrayRemove(dogId)
                )
        } catch (e: Exception) {
            Log.e(
                "FirestoreError",
                "Error al eliminar el perro compartidos para el usuario: $uid",
                e
            )
        }
    }

    override suspend fun addSharedDog(uid: String, dogId: String) {
        try {
            db
                .collection(FirestoreCollections.users)
                .document(uid)
                .update(
                    "sharedDogs",
                    FieldValue.arrayUnion(dogId)
                )
        } catch (e: Exception) {
            Log.e(
                "FirestoreError",
                "Error al eliminar el perro compartidos para el usuario: $uid",
                e
            )
        }
    }

    override suspend fun updateName(userId: String, newName: String) {
        updateUserField(userId, "name", newName)
    }

    override suspend fun updatePhone(userId: String, newPhone: String) {
        updateUserField(userId, "phone", newPhone)
    }

    override suspend fun updateAddress(userId: String, newAddress: String) {
        updateUserField(userId, "address", newAddress)
    }

    override suspend fun updateUserType(userId: String, newType: String) {
        updateUserField(userId, "type", newType)
    }

    override suspend fun updateProfileImage(userId: String, newImageUrl: String) {
        updateUserField(userId, "profileImageUrl", newImageUrl)
    }

    override suspend fun getSharedDogsObject(currentUser: String): List<Dog> {
        val userRepository = UserRepositoryImpl(db)
        return try {
            // Recupera al usuario actual desde la base de datos
            val user = userRepository.getUserDetailsById(currentUser)

            // Si el usuario tiene una lista de perros compartidos
            val sharedDogIds = user!!.sharedDogs ?: emptyList()

            // Obtener el objeto de cada perro usando su ID
            sharedDogIds.mapNotNull { dogId ->
                userRepository.getDogDetailsById(dogId) // Esto asume que tienes un método que obtiene un perro por su ID
            }
        } catch (e: Exception) {
            // Manejar la excepción, puedes retornar una lista vacía o lanzar de nuevo la excepción
            emptyList()
        }
    }

    override suspend fun getStarredDogsObject(currentUser: String): List<Dog> {
        val userRepository = UserRepositoryImpl(db)
        return try {
            // Recupera al usuario actual desde la base de datos
            val user = userRepository.getUserDetailsById(currentUser)

            // Si el usuario tiene una lista de perros compartidos
            val starredDogIds = user!!.starred_dogs ?: emptyList()

            // Obtener el objeto de cada perro usando su ID
            starredDogIds.mapNotNull { dogId ->
                userRepository.getDogDetailsById(dogId)
            }
        } catch (e: Exception) {
            // Manejar la excepción, puedes retornar una lista vacía o lanzar de nuevo la excepción
            emptyList()
        }
    }

    override suspend fun updateStarredDogsById(uid: String, dogId: String) {
        try {
            db.collection("users").document(uid).update("starred_dogs", FieldValue.arrayUnion(dogId)).await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al actualizar perros favoritos", e)
        }
    }

    override suspend fun addNewDog(dogId: String, uid: String) {
        try {
            db.collection("users").document(uid).update("dogs", FieldValue.arrayUnion(dogId)).await()
        } catch (e: Exception){
            Log.e("FirestoreError", "Error al actualizar perros ", e)
        }
    }

    override suspend fun deleteDog(dogId: String, uid: String) {
        try {
            db.collection("users").document(uid).update("dogs", FieldValue.arrayRemove(dogId)).await()
        } catch (e: Exception){
            Log.e("FirestoreError", "Error al actualizar perros ", e)
        }
    }

    private suspend fun updateUserField(userId: String, field: String, value: Any) {
        try {
            db.collection("users").document(userId)
                .update(field, value)
                .await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al actualizar $field", e)
        }
    }


}