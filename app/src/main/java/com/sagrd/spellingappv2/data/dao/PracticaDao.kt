package com.sagrd.spellingappv2.data.dao

import androidx.room.*
import com.sagrd.spellingappv2.model.Practica
import kotlinx.coroutines.flow.Flow

@Dao
interface PracticaDao {
    @Upsert()
    suspend fun upsert(practica: Practica)

    @Delete
    suspend fun delete(practica: Practica)

    @Query("SELECT * FROM Practicas WHERE practicaId=:practicaId")
    fun find(practicaId: Int): Flow<Practica>

    @Query("SELECT * FROM Practicas ORDER BY practicaId")
    fun getListStream(): Flow<List<Practica>>
}