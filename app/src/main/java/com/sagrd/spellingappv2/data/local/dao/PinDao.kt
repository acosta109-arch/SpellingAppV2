package com.sagrd.spellingappv2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.sagrd.spellingappv2.data.local.entities.PinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PinDao {
    @Upsert
    suspend fun savePin(pin: List<PinEntity>)

    @Delete
    suspend fun deletePin(pin: PinEntity)

    @Query("SELECT * FROM Pines")
    fun getAllPines(): Flow<List<PinEntity>>

    @Query("SELECT * FROM Pines WHERE pinId = :id")
    suspend fun getPinById(id: Int): PinEntity?

    @Query("SELECT * FROM Pines WHERE pin = :pin")
    suspend fun getPinByValor(pin: String): PinEntity?
}