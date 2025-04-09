package com.sagrd.spellingappv2.fireBase

import com.google.firebase.auth.FirebaseUser

interface AuthManager {
    val currentUser: FirebaseUser?
    val isLoggedIn: Boolean
    suspend fun logout()
}