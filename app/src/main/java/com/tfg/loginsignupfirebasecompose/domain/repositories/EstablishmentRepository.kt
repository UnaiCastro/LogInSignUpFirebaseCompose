package com.tfg.loginsignupfirebasecompose.domain.repositories

import com.tfg.loginsignupfirebasecompose.data.collectionsData.Establishment

interface EstablishmentRepository {
    suspend fun getEstablishments(): List<Establishment>
    suspend fun getEstablishmentById(establishmentId: String): Establishment?
    suspend fun updateEstablishment(
        userId: String,
        value: String,
        value1: String,
        value2: String,
        first: Double?,
        second: Double?
    )

    suspend fun removeFromLikes(establishmentId: String, userId: String)
    suspend fun addToLikes(establishmentId: String, toString: String)
    suspend fun getEstablishmentsByIds(likedIds: List<String>): List<Establishment>
}