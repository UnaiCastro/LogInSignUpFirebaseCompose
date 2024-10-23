package com.tfg.loginsignupfirebasecompose.domain.repositories

import com.google.firebase.auth.FirebaseUser


interface AuthRepository {
    suspend fun signUp(email: String, password: String): Result<FirebaseUser>
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    fun logout(): Unit
    fun getCurrentUser(): FirebaseUser?
    suspend fun sendPasswordResetEmail(value: String)
}