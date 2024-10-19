package com.tfg.loginsignupfirebasecompose.data.implementations

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.data.Firebase.FirestoreCollections
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Message
import com.tfg.loginsignupfirebasecompose.domain.repositories.MessageRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class MessageRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : MessageRepository {


    override suspend fun addMessage(newMessage: Message): String {
        return try {
            val messageRef = db.collection(FirestoreCollections.messages).document()
            val newMessageDefinitive = hashMapOf(
                "chatId" to newMessage.chatId,
                "senderId" to newMessage.senderId,
                "receiverId" to newMessage.receiverId,
                "messageContent" to newMessage.messageContent,
                "timestamp" to newMessage.timestamp
            )
            messageRef.set(newMessageDefinitive).await()
            Log.d("Firestore", "Mensaje agregado exitosamente a la colección de mensajes")
            messageRef.id

        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al agregar el mensaje a la colección", e)
            ""
        }
    }

    override suspend fun getMessageById(lastMessageId: String): Message? {
        return try {
            val document = db.collection(FirestoreCollections.messages).document(lastMessageId).get().await()
            document.toObject(Message::class.java)
        } catch (e: Exception) {
            Log.e("MessageRepository", "Error fetching message by ID: $lastMessageId", e)
            null
        }
    }

}
