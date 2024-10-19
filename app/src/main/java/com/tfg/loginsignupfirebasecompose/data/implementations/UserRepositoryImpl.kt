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
    private val db: FirebaseFirestore,
) : UserRepository
{

    private val dogRepository = DogRepositoryImpl(db)

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



    override suspend fun getStarredDogs(uid: String): List<String> {
        return try {
            val document = db.collection(FirestoreCollections.users).document(uid).get().await()
            document.get("starred_dogs") as? List<String>
                ?: emptyList()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error retrieving favorite dogs for user: $uid", e)

            emptyList()
        }
    }

    override suspend fun updateStarredDogs(uid: String, currentStarred: MutableList<String>) {
        try {
            val documentRef = db.collection(FirestoreCollections.users).document(uid)
            val documentSnapshot = documentRef.get().await()

            if (documentSnapshot.exists()) {
                documentRef.update("starred_dogs", currentStarred).await()
            } else {

                Log.w("Firestore", "Document with UID $uid does not exist.")
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error updating starred dogs", e)
        }
    }

    override suspend fun getSharedDogs(uid: String): List<String> {
        return try {
            val userSnapshot = db.collection(FirestoreCollections.users).document(uid).get().await()
            userSnapshot.get("sharedDogs") as? List<String> ?: emptyList()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error retrieving shared dogs for user: $uid", e)

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
            Log.e("FirestoreError", "Error removing shared dog for user: $uid", e)

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
            Log.e("FirestoreError", "Error adding shared dog for user: $uid", e)
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
                dogRepository.getDogDetailsById(dogId)
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
                dogRepository.getDogDetailsById(dogId)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun updateStarredDogsById(uid: String, dogId: String) {
        try {
            db.collection(FirestoreCollections.users).document(uid).update("starred_dogs", FieldValue.arrayUnion(dogId)).await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error updating favorite dogs", e)
        }
    }

    override suspend fun addNewDog(dogId: String, uid: String) {
        try {
            db.collection(FirestoreCollections.users).document(uid).update("dogs", FieldValue.arrayUnion(dogId)).await()
        } catch (e: Exception){
            Log.e("FirestoreError", "Error updating union of dogs", e)
        }
    }

    override suspend fun deleteDog(dogId: String, uid: String) {
        try {
            db.collection(FirestoreCollections.users).document(uid).update("dogs", FieldValue.arrayRemove(dogId)).await()
        } catch (e: Exception){
            Log.e("FirestoreError", "Error updating deletion of dogs", e)
        }
    }

    override suspend fun addChatToRoomChat(createdChat: String, uid: String) {
        try {
            db.collection(FirestoreCollections.users).document(uid).update("chat_rooms", FieldValue.arrayUnion(createdChat)).await()
        } catch (e: Exception){
            Log.e("FirestoreError", "Error updating chatroom", e)
        }
    }

    override suspend fun getAddress(uid: String): String? {
        return try {
            db.collection(FirestoreCollections.users).document(uid).get().await().getString("address")
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error retrieving user's address", e)
            null
        }
    }

    override suspend fun updateUserProfileImage(imageUrl: String?, uid: String) {
        try {
            db.collection(FirestoreCollections.users).document(uid).update("profileImageUrl", imageUrl).await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error updating profile image", e)
        }
    }

    private suspend fun updateUserField(userId: String, field: String, value: Any) {
        try {
            db.collection(FirestoreCollections.users).document(userId)
                .update(field, value)
                .await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error updating $field", e)
        }
    }


    override suspend fun saveCommunityInfo(userId: String, coords: Pair<Double, Double>?) {
        try {
            db.collection(FirestoreCollections.users)
                .document(userId)
                .update("coordinates", mapOf("latitude" to coords?.first, "longitude" to coords?.second))
                .await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error saving community information", e)
        }
    }

    override suspend fun updateRegion(uid: String, it: String) {
        try {
            db.collection(FirestoreCollections.users).document(uid).update("region", it).await()
        } catch (e:Exception){
            Log.e("FirestoreError", "Error saving region information", e)
        }
    }

    override suspend fun addToLikedEstablishments(currentUserId: String, establishmentId: String) {
        try {
            val userRef = db.collection(FirestoreCollections.users).document(currentUserId)
            userRef.update("likedEstablishments", FieldValue.arrayUnion(establishmentId))
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error adding establishment to favorites", e)
        }

    }

    override suspend fun removeFromLikedEstablishments(
        currentUserId: String,
        establishmentId: String
    ) {
        try {
            val userRef = db.collection(FirestoreCollections.users).document(currentUserId)
            userRef.update("likedEstablishments", FieldValue.arrayRemove(establishmentId))
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error removing establishment from favorites", e)
        }

    }

    override suspend fun removeStarredDog(toString: String, dogId: String) {
        try {
            db.collection(FirestoreCollections.users).document(toString).update("starred_dogs", FieldValue.arrayRemove(dogId)).await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error removing dog from favorites", e)
        }
    }

    override suspend fun addToStarredDogs(toString: String, dogId: String) {
        try {
            db.collection(FirestoreCollections.users).document(toString).update("starred_dogs", FieldValue.arrayUnion(dogId)).await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error adding dog to favorites", e)
        }
    }


}