package com.tfg.loginsignupfirebasecompose.domain.repositories

interface UserRepository {
    suspend fun getUserName(uid: String): String?
    suspend fun saveUser(uid: String, user: Map<String, Any>): Result<Unit>
    suspend fun getEmail(uid: String): String?
    suspend fun getProfileImageUrl(uid: String): String?
}