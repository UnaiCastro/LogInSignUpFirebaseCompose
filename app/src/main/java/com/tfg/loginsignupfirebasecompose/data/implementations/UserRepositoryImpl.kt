package com.tfg.loginsignupfirebasecompose.data.implementations

import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.data.Firebase.FirestoreCollections
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Chat
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.data.collectionsData.User
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import kotlinx.coroutines.tasks.await
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
            val snapshot = db.collection(FirestoreCollections.chats)
                .whereIn(FieldPath.documentId(), chatRoomIds)
                .get()
                .await()
            snapshot.documents.map { document ->
                Chat(
                    chatId = document.id,
                    dogId = document.getString("dog_id") ?: "",
                    user1id = document.getString("user1id") ?: "",
                    user2id = document.getString("user2id") ?: "",
                    lastMessage = document.getString("last_message") ?: "",
                    created_at = document.getString("created_at") ?: "",
                    messages = document.get("messages") as? List<String> ?: emptyList()
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
            val document = db.collection(FirestoreCollections.users).document(userId).get().await()
            if (document.exists()) {
                val user = document.toObject(User::class.java)
                user?.let {
                    it.userId = document.id
                }
                user
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error fetching user details for userId: $userId", e)
            null
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

    override suspend fun saveEstablishment(
        establishment: HashMap<String, Any>
    ): Result<String> { // Cambiar a devolver un String que será el ID del establecimiento
        return try {
            // Suponiendo que estás usando Firestore
            val db = FirebaseFirestore.getInstance()
            val documentReference = db.collection(FirestoreCollections.establishments) // Nombre de la colección
                .add(establishment) // Usa add para crear el documento y obtener el ID
                .await() // Espera a que se complete la operación
            Result.success(documentReference.id) // Devuelve el ID del documento creado
        } catch (e: Exception) {
            Result.failure(e) // Devuelve el error si falla
        }
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
            val documentRef = db.collection(FirestoreCollections.users).document(uid)
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
            val user = userRepository.getUserDetailsById(currentUser)
            val sharedDogIds = user!!.sharedDogs ?: emptyList()
            sharedDogIds.mapNotNull { dogId ->
                userRepository.getDogDetailsById(dogId)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getStarredDogsObject(currentUser: String): List<Dog> {
        val userRepository = UserRepositoryImpl(db)
        return try {
            val user = userRepository.getUserDetailsById(currentUser)
            val starredDogIds = user!!.starred_dogs ?: emptyList()
            starredDogIds.mapNotNull { dogId ->
                userRepository.getDogDetailsById(dogId)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun updateStarredDogsById(uid: String, dogId: String) {
        try {
            db.collection(FirestoreCollections.users).document(uid).update("starred_dogs", FieldValue.arrayUnion(dogId)).await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al actualizar perros favoritos", e)
        }
    }

    override suspend fun addNewDog(dogId: String, uid: String) {
        try {
            db.collection(FirestoreCollections.users).document(uid).update("dogs", FieldValue.arrayUnion(dogId)).await()
        } catch (e: Exception){
            Log.e("FirestoreError", "Error al actualizar perros union ", e)
        }
    }

    override suspend fun deleteDog(dogId: String, uid: String) {
        try {
            db.collection(FirestoreCollections.users).document(uid).update("dogs", FieldValue.arrayRemove(dogId)).await()
        } catch (e: Exception){
            Log.e("FirestoreError", "Error al actualizar perros delete", e)
        }
    }

    override suspend fun addChatToRoomChat(createdChat: String, uid: String) {
        try {
            db.collection(FirestoreCollections.users).document(uid).update("chat_rooms", FieldValue.arrayUnion(createdChat)).await()
        } catch (e: Exception){
            Log.e("FirestoreError", "Error al actualizar chatroom", e)
        }
    }

    override suspend fun getAddress(uid: String): String? {
        return try {
            db.collection(FirestoreCollections.users).document(uid).get().await().getString("address")
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener la dirección del usuario", e)
            null
        }
    }

    override suspend fun updateUserProfileImage(imageUrl: String?, uid: String) {
        try {
            db.collection(FirestoreCollections.users).document(uid).update("profileImageUrl", imageUrl).await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al actualizar la imagen de perfil", e)
        }
    }

    private suspend fun updateUserField(userId: String, field: String, value: Any) {
        try {
            db.collection(FirestoreCollections.users).document(userId)
                .update(field, value)
                .await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al actualizar $field", e)
        }
    }

    override suspend fun saveBusinessInfo(
        userId: String,
        name: String,
        address: String,
        phone: String,
        latitude: Double,
        longitude: Double
    ) {
        val coordinates = hashMapOf(
            "latitude" to latitude,
            "longitude" to longitude
        )
        val establishment = hashMapOf(
            "name" to name,
            "address" to address,
            "phone" to phone,
            "coordinates" to coordinates,
            "owner_id" to userId,
            "likes" to 0
        )
        db.collection(FirestoreCollections.establishments).document().set(establishment)
    }

    override suspend fun deleteBusinessInfo(userId: String) {
        try {
            val establishments = db.collection(FirestoreCollections.establishments)
                .whereEqualTo("owner_id", userId)
                .get() // Esto retorna un QuerySnapshot
                .await()

            // Iterar sobre los documentos y eliminarlos uno por uno
            for (document in establishments.documents) {
                db.collection(FirestoreCollections.establishments)
                    .document(document.id)
                    .delete()
                    .await() // Esperar que se complete la eliminación
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al eliminar la información del establecimiento", e)
        }

    }

    override suspend fun saveCommunityInfo(userId: String, coords: Pair<Double, Double>?) {
        try {
            db.collection(FirestoreCollections.users)
                .document(userId)
                .update("coordinates", mapOf("latitude" to coords?.first, "longitude" to coords?.second))
                .await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al guardar la información de la comunidad", e)
        }
    }


}