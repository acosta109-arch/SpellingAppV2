package com.sagrd.spellingappv2.di

import android.content.Context
import androidx.room.Room
import com.sagrd.spellingappv2.data.SpellingDb
import com.sagrd.spellingappv2.data.dao.PalabraDao
import com.sagrd.spellingappv2.data.dao.PracticaDao
import com.sagrd.spellingappv2.data.dao.PracticaDetalleDao
import com.sagrd.spellingappv2.data.dao.UsuarioDao
import com.sagrd.spellingappv2.data.repository.PalabraRepository
import com.sagrd.spellingappv2.data.repository.PracticaDetalleRepository
import com.sagrd.spellingappv2.data.repository.PracticaRepository
import com.sagrd.spellingappv2.data.repository.UsuarioRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSpellingDb(@ApplicationContext context: Context): SpellingDb {
        val DATABASE_NAME = "SpellingDb"
        return Room.databaseBuilder(
            context,
            SpellingDb::class.java,
            DATABASE_NAME
        )
            .createFromAsset("databases/SpellingDb.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePalabraDao(spellingDb: SpellingDb): PalabraDao {
        return spellingDb.palabraDao
    }

    @Singleton
    @Provides
    fun provideUsuarioDao(spellingDb: SpellingDb): UsuarioDao {
        return spellingDb.usuarioDao
    }

    @Singleton
    @Provides
    fun providePracticaDao(spellingDb: SpellingDb): PracticaDao {
        return spellingDb.practicaDao
    }

    @Singleton
    @Provides
    fun providePracticaDetalleDao(spellingDb: SpellingDb): PracticaDetalleDao {
        return spellingDb.detalleDao
    }
}