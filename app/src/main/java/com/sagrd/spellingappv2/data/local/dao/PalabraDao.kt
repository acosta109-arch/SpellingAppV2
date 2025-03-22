package com.sagrd.spellingappv2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.sagrd.spellingappv2.data.local.entities.PalabraEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PalabraDao {
    @Update
    suspend fun save(palabra: List<PalabraEntity>)

    @Delete
    suspend fun deletePalabra(palabra: PalabraEntity)

    @Query("SELECT * FROM palabras")
    fun getAllPalabras(): Flow<List<PalabraEntity>>

    @Query("SELECT * FROM palabras WHERE palabraId = :id")
    suspend fun getPalabraById(id: Int): PalabraEntity?


}