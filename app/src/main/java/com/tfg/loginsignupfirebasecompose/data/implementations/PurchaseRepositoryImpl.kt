package com.tfg.loginsignupfirebasecompose.data.implementations

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.tfg.loginsignupfirebasecompose.domain.repositories.PurchaseRepository
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : PurchaseRepository
{
    override suspend fun newPurchase(dogId: String, price: Int, uid: String, userId: String) {
        Log.d("PurchaseRepository", "Creando la compra con dogId: $dogId, price: $price, uid: $uid, userId: $userId")
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        try {
            val purchaseData = hashMapOf(
                "dog_id" to dogId,
                "price" to price,
                "buyer_id" to uid,
                "seller_id" to userId,
                "date" to currentDate,
            )
            db.collection("purchase").add(purchaseData).await()
        } catch (e: Exception) {
            Log.e("PurchaseRepository", "Error al crear la compra", e)
        }

    }

}