package com.example.spellingappv2.di

import android.content.Context
import androidx.room.Room
import com.example.spellingappv2.data.SpellingDb
import com.example.spellingappv2.data.dao.PalabraDao
import com.example.spellingappv2.data.dao.PracticaDao
import com.example.spellingappv2.data.dao.PracticaDetalleDao
import com.example.spellingappv2.data.dao.UsuarioDao
import com.example.spellingappv2.data.repositorios.PalabraRepository
import com.example.spellingappv2.data.repositorios.PracticaDetalleRepository
import com.example.spellingappv2.data.repositorios.PracticaRepository
import com.example.spellingappv2.data.repositorios.UsuarioRepository
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
    fun ProvideSpellingDb(@ApplicationContext context: Context) : SpellingDb{
        val DATABASE_NAME = "SpellingDb"
        return Room.databaseBuilder(
            context,
            SpellingDb::class.java,
            DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .createFromAsset("databases/SpellingDb.db")
            .build()
    }


    @Provides
    fun ProvidesPalabraDao(spellingDb: SpellingDb): PalabraDao {
        return spellingDb.palabraDao
    }

    @Provides
    fun ProvidesPalabraRepository(palabraDao: PalabraDao): PalabraRepository {
        return PalabraRepository(palabraDao)
    }

    @Provides
    fun ProvideUsuarioDao(spellingDb: SpellingDb): UsuarioDao {
        return spellingDb.usuarioDao
    }

    @Provides
    fun ProvideUsuarioRepository(usuarioDao: UsuarioDao): UsuarioRepository {
        return UsuarioRepository(usuarioDao)
    }

    @Provides
    fun ProvidePracticaDao(spellingDb: SpellingDb) : PracticaDao {
        return spellingDb.practicaDao
    }
    @Provides
    fun ProvidePracticaRepository(practicaDao: PracticaDao) : PracticaRepository{
        return PracticaRepository(practicaDao = practicaDao)
    }
    @Provides
    fun ProvidePracticaDetalleDao(spellingDb: SpellingDb) : PracticaDetalleDao {
        return spellingDb.detalleDao
    }
    @Provides
    fun ProvidePracticaDetalleRepository(detalleDao: PracticaDetalleDao) : PracticaDetalleRepository {
        return PracticaDetalleRepository(detalleDao = detalleDao)
    }
}