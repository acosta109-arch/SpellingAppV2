package com.sagrd.spellingappv2.data.dao

import androidx.room.*
import com.sagrd.spellingappv2.model.PracticaDetalle
import kotlinx.coroutines.flow.Flow

@Dao
interface PracticaDetalleDao {
    @Upsert()
    suspend fun upsert(detalle: PracticaDetalle)

    @Delete
    suspend fun delete(detalle: PracticaDetalle)

    @Query("SELECT * FROM PracticasDetalle WHERE detalleId=:detalleId")
    fun find(detalleId: Int): Flow<PracticaDetalle>

    @Query("SELECT * FROM PracticasDetalle ORDER BY detalleId")
    fun getListStream(): Flow<List<PracticaDetalle>>
}