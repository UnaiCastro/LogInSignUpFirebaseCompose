package com.tfg.loginsignupfirebasecompose.data.implementations

import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.data.Firebase.FirestoreCollections
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
            val querySnapshot = db.collection(FirestoreCollections.chats)
                .document(chatId)
                .get()
                .await()
            querySnapshot.get("messages") as? List<String> ?: emptyList()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error retrieving message IDs", e)
            emptyList()
        }
    }

    override suspend fun getOtherUserId(chatId: String, senderId: String): String {
        return try {
            val document = db.collection(FirestoreCollections.chats)
                .document(chatId)
                .get()
                .await()
            document.getString("user1id")?.let {
                if (it == senderId) document.getString("user2id") else it
            } ?: ""
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error retrieving the other user's ID", e)
            ""
        }
    }

    override suspend fun isCreatedChat(
        userId: String,
        dogId: String,
        ownerId: String
    ): String {
        return try {
            val querySnapshot = db.collection(FirestoreCollections.chats)
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
                val newChatRef = db.collection(FirestoreCollections.chats).add(newChatData).await()
                newChatRef.id
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error retrieving or creating the chat", e)
            ""
        }
    }

    override suspend fun getChatById(chatId: String): Chat? {
        return try {
            val document = db.collection(FirestoreCollections.chats).document(chatId).get().await()
            document.toObject(Chat::class.java)?.apply {
                messages = document.get("messages") as? List<String> ?: emptyList()
                this.chatId = document.id
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error retrieving chat with ID: $chatId", e)
            null
        }
    }

    override suspend fun updateChatWithNewMessage(chatId: String, messageId: String) {
        try {
            db.collection(FirestoreCollections.chats).document(chatId).update(
                mapOf(
                    "lastMessage" to messageId,
                    "messages" to FieldValue.arrayUnion(messageId)
                )
            ).await()

            Log.d("Firestore", "Chat updated with the new message")
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error updating the chat with the new message", e)
        }
    }

    override suspend fun getChatsByIds(chatRoomIds: List<String>): List<Chat> {
        return try {
            val snapshot = db.collection(FirestoreCollections.chats)
                .whereIn(FieldPath.documentId(), chatRoomIds)
                .get()
                .await()
            snapshot.documents.map { document ->
                Chat(
                    chatId = document.id,
                    dogId = document.getString("dog_id") ?: "",
                    user1id = document.getString("user1id") ?: "",
                    user2id = document.getString("user2id") ?: "",
                    lastMessage = document.getString("last_message") ?: "",
                    created_at = document.getString("created_at") ?: "",
                    messages = document.get("messages") as? List<String> ?: emptyList()
                )
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error fetching chats", e)
            emptyList()
        }
    }

}