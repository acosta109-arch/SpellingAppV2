package com.sagrd.spellingappv2.di

import android.content.Context
import androidx.room.Room
import com.sagrd.spellingappv2.data.local.database.SpellingAppDb
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            SpellingAppDb::class.java,
            "SpellingApp.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideHijoDao(spellingAppDb: SpellingAppDb) = spellingAppDb.hijoDao()

    @Provides
    @Singleton
    fun provideUsuarioDao(spellingAppDb: SpellingAppDb) =  spellingAppDb.usuarioDao()

    @Provides
    @Singleton
    fun providePinDao(spellingAppDb: SpellingAppDb) = spellingAppDb.pinDao()

    @Provides
    @Singleton
    fun providePalabra(spellingAppDb: SpellingAppDb) = spellingAppDb.palabraDao()
}