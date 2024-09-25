package com.tfg.loginsignupfirebasecompose.domain.repositories

import com.tfg.loginsignupfirebasecompose.data.collectionsData.Chat
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.data.collectionsData.User
import java.io.Serializable

interface UserRepository {
    suspend fun getUserName(uid: String): String?
    suspend fun saveUser(uid: String, user: Map<String, Any>): Result<Unit>
    suspend fun getEmail(uid: String): String?
    suspend fun getProfileImageUrl(uid: String): String?
    suspend fun getChatsByIds(chatRoomIds: List<String>): List<Chat>
    suspend fun getUserChatRooms(uid: String): List<String>
    suspend fun getUserDetailsById(otherUserId: String): User?
    suspend fun getDogDetailsById(dogId: String): Dog?
    suspend fun saveEstablishment(uid: String, establishment: HashMap<String, Serializable>): Result<Unit>
    suspend fun getStarredDogs(uid: String): List<String>?
    suspend fun updateStarredDogs(uid: String, currentStarred: MutableList<String>)
    suspend fun getSharedDogs(uid: String): List<String>
    suspend fun removeSharedDog(uid: String, dogId: String)
    suspend fun addSharedDog(uid: String, dogId: String)
    suspend fun updateName(userId: String, newName: String)
    suspend fun updatePhone(userId: String, newPhone: String)
    suspend fun updateAddress(userId: String, newAddress: String)
    suspend fun updateUserType(userId: String, newType: String)
    suspend fun updateProfileImage(userId: String, newImageUrl: String)
    suspend fun getSharedDogsObject(currentUser: String): List<Dog>
    suspend fun getStarredDogsObject(currentUser: String): List<Dog>
    suspend fun updateStarredDogsById(uid: String, dogId: String)
    suspend fun addNewDog(dogId: String, uid: String)
    suspend fun deleteDog(dogId: String, uid: String)

}