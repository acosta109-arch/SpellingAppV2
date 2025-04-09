package com.sagrd.spellingappv2.fireBase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
): AuthManager {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override val isLoggedIn: Boolean
        get() = firebaseAuth.currentUser != null

    override suspend fun logout() {
        firebaseAuth.signOut()
    }
}