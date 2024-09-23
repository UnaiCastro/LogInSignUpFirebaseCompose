package com.tfg.loginsignupfirebasecompose.data.implementations

import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.MessageRepository
import javax.inject.Inject


class MessageRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : MessageRepository {

}
