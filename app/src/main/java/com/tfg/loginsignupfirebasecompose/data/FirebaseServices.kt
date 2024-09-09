package com.tfg.loginsignupfirebasecompose.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

data class FirebaseServices (
    val firebaseAuth: FirebaseAuth,
    val firebaseFirestore: FirebaseFirestore,
    val firebaseStorage: FirebaseStorage
)