package com.tfg.loginsignupfirebasecompose.data.implementations

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Message
import com.tfg.loginsignupfirebasecompose.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
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
            querySnapshot.get("messageIds") as? List<String> ?: emptyList()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener los IDs de mensajes", e)
            emptyList()
        }
    }

    override suspend fun getMessageById(messageId: String): Message? {
        return try {
            val document = db.collection("messages")
                .document(messageId)
                .get()
                .await()
            document.toObject(Message::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener el mensaje", e)
            null
        }
    }

    override suspend fun addMessage(newMessage: Message) {
        try {
            db.collection("messages").add(newMessage).await()
            Log.d("Firestore", "Mensaje agregado exitosamente")
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al agregar el mensaje", e)
        }
    }

    override suspend fun getOtherUserId(chatId: String, senderId: String): String {
        return try {
            val document = db.collection("chats")
                .document(chatId)
                .get()
                .await()
            document.getString("senderId") ?: ""

        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener el ID del otro usuario", e)
            ""
        }
    }

    override suspend fun isCreatedChat(
        userId: StateFlow<String>,
        dogId: String,
        ownerId: String
    ): String {
        return try {
            // Obtener todos los chats de Firestore
            val chats = db.collection("chats").get().await()

            // Buscar si ya existe un chat con los criterios dados (dogId, user1Id, user2Id)
            val existingChat = chats.documents.find { document ->
                val chatDogId = document.getString("dog_id")
                val user1Id = document.getString("user1id")
                val user2Id = document.getString("user2id")

                (chatDogId == dogId) && (
                        (user1Id == userId.value && user2Id == ownerId) ||
                                (user1Id == ownerId && user2Id == userId.value)
                        )
            }

            // Si se encuentra un chat que cumple con los criterios, devolvemos el chatId
            if (existingChat != null) {
                return existingChat.id // Retorna el ID del chat existente
            }

            // Si no existe el chat, creamos uno nuevo
            val newChatData = hashMapOf(
                "dog_id" to dogId,
                "user1id" to userId.value,
                "user2id" to ownerId,
                "created_at" to System.currentTimeMillis(),
                "last_message" to ""
            )

            // Agregamos el nuevo chat a Firestore
            val newChatRef = db.collection("chats").add(newChatData).await()

            // Devolvemos el ID del nuevo chat creado
            return newChatRef.id

        } catch (e: Exception) {
            Log.e("FirestoreError", "Error al obtener o crear el chat", e)
            ""
        }
    }

}