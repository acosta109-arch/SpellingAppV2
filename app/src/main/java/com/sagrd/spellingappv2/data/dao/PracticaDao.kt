package com.sagrd.spellingappv2.data.dao

import androidx.room.*
import com.sagrd.spellingappv2.model.Practica
import kotlinx.coroutines.flow.Flow

@Dao
interface PracticaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(practica: Practica)

    @Delete
    suspend fun eliminar(practica: Practica)

    @Query("SELECT * FROM Practicas WHERE practicaId=:practicaId")
    fun buscar(practicaId: Int): Flow<Practica>

    @Query("SELECT * FROM Practicas ORDER BY practicaId")
    fun getList(): Flow<List<Practica>>
}