package com.example.deadlineku.firebase

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseHelper {

    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

}