package com.sagrd.spellingappv2.presentation.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object AuthManager {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    val isLoggedIn: Boolean
        get() = currentUser != null

    val userId: String?
        get() = currentUser?.uid

    fun logout() {
        auth.signOut()
    }

    suspend fun getUserData(): Map<String, Any>? {
        val uid = userId ?: return null
        return try {
            val document = db.collection("usuarios").document(uid).get().await()
            if (document.exists()) {
                document.data
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun addAuthStateListener(listener: FirebaseAuth.AuthStateListener) {
        auth.addAuthStateListener(listener)
    }

    fun removeAuthStateListener(listener: FirebaseAuth.AuthStateListener) {
        auth.removeAuthStateListener(listener)
    }
}