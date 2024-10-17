package com.tfg.loginsignupfirebasecompose.domain.repositories

import com.tfg.loginsignupfirebasecompose.data.collectionsData.Chat
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.data.collectionsData.User

interface UserRepository {
    suspend fun getUserName(uid: String): String?
    suspend fun saveUser(uid: String, user: Map<String, Any>): Result<Unit>
    suspend fun getEmail(uid: String): String?
    suspend fun getProfileImageUrl(uid: String): String?
    suspend fun getChatsByIds(chatRoomIds: List<String>): List<Chat>
    suspend fun getUserChatRooms(uid: String): List<String>
    suspend fun getUserDetailsById(otherUserId: String): User?
    suspend fun getDogDetailsById(dogId: String): Dog?
    suspend fun saveEstablishment(establishment: HashMap<String, Any>): Result<String>
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
    suspend fun addChatToRoomChat(createdChat: String, uid: String)
    suspend fun getAddress(uid: String): String?
    suspend fun updateUserProfileImage(imageUrl: String?, uid: String)
    suspend fun saveBusinessInfo(
        userId: String,
        name: String,
        address: String,
        phone: String,
        latitude: Double,
        longitude: Double
    )
    suspend fun deleteBusinessInfo(userId: String)
    suspend fun saveCommunityInfo(userId: String, coords: Pair<Double, Double>?)
    suspend fun updateRegion(uid: String, it: String)
    suspend fun addToLikedEstablishments(currentUserId: String, establishmentId: String)
    suspend fun removeFromLikedEstablishments(currentUserId: String, establishmentId: String)
    suspend fun removeStarredDog(toString: String, dogId: String)
    suspend fun addToStarredDogs(toString: String, dogId: String)
}