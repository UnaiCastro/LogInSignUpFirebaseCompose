package com.tfg.loginsignupfirebasecompose.data.implementations

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Chat
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Message
import com.tfg.loginsignupfirebasecompose.domain.repositories.ChatRepository
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ChatRepository {


    override suspend fun getMessageIdsForChat(chatId: String): List<String> {
        return try {
            val querySnapshot = db.collection("chats")
                .document(chatId)
                .get()
                .await()
            querySnapshot.get("messages") as? List<String> ?: emptyList()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener los IDs de mensajes", e)
            emptyList()
        }
    }

    override suspend fun getMessageById(messageId: String): Message? {
        return try {
            if (messageId.isNotEmpty()) {
                val document = db.collection("messages")
                    .document(messageId)
                    .get()
                    .await()

                document.toObject(Message::class.java)
            } else {
                Log.e("FirestoreError", "El ID del mensaje está vacío o es inválido")
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener el mensaje", e)
            null
        }
    }

    override suspend fun getOtherUserId(chatId: String, senderId: String): String {
        return try {
            val document = db.collection("chats")
                .document(chatId)
                .get()
                .await()
            document.getString("user1id")?.let {
                if (it == senderId) document.getString("user2id") else it
            } ?: ""
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener el ID del otro usuario", e)
            ""
        }
    }

    override suspend fun isCreatedChat(
        userId: String,
        dogId: String,
        ownerId: String
    ): String {
        return try {
            val querySnapshot = db.collection("chats")
                .whereEqualTo("dog_id", dogId)
                .whereIn("user1id", listOf(userId, ownerId))
                .whereIn("user2id", listOf(userId, ownerId))
                .get().await()
            val existingChat = querySnapshot.documents.firstOrNull()
            existingChat?.id ?: run {
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val newChatData = hashMapOf(
                    "dog_id" to dogId,
                    "user1id" to userId,
                    "user2id" to ownerId,
                    "created_at" to currentDate,
                    "lastMessage" to "",
                    "messages" to emptyList<String>()
                )
                val newChatRef = db.collection("chats").add(newChatData).await()
                newChatRef.id
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener o crear el chat", e)
            ""
        }
    }

    override suspend fun getChatById(chatId: String): Chat? {
        return try {
            val document = db.collection("chats").document(chatId).get().await()
            document.toObject(Chat::class.java)?.apply {
                messages = document.get("messages") as? List<String> ?: emptyList()
                this.chatId = document.id
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener el chat con ID: $chatId", e)
            null
        }
    }

    override suspend fun updateChatWithNewMessage(chatId: String, messageId: String) {
        try {
            db.collection("chats").document(chatId).update(
                mapOf(
                    "lastMessage" to messageId,
                    "messages" to FieldValue.arrayUnion(messageId)
                )
            ).await()

            Log.d("Firestore", "Chat actualizado con el nuevo mensaje")
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al actualizar el chat con el nuevo mensaje", e)
        }
    }

}