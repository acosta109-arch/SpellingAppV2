package com.sagrd.spellingappv2.di


import com.google.firebase.auth.FirebaseAuth
import com.sagrd.spellingappv2.fireBase.AuthManager
import com.sagrd.spellingappv2.fireBase.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthManager = AuthRepository(firebaseAuth)

}