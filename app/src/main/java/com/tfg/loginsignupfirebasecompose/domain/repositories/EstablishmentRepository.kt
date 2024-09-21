package com.tfg.loginsignupfirebasecompose.domain.repositories

import com.tfg.loginsignupfirebasecompose.data.collectionsData.Establishment

interface EstablishmentRepository {
    suspend fun getEstablishments(): List<Establishment>
    suspend fun getEstablishmentById(establishmentId: String): Establishment?
}