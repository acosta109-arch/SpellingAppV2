package com.example.spellingappv2.data.dao

import androidx.room.*
import com.example.spellingappv2.model.PracticaDetalle
import kotlinx.coroutines.flow.Flow

@Dao
interface PracticaDetalleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(detalle: PracticaDetalle)

    @Delete
    suspend fun eliminar(detalle: PracticaDetalle)

    @Query("SELECT * FROM PracticasDetalle WHERE detalleId=:detalleId")
    fun buscar(detalleId: Int): Flow<PracticaDetalle>

    @Query("SELECT * FROM PracticasDetalle ORDER BY detalleId")
    fun getList(): Flow<List<PracticaDetalle>>
}