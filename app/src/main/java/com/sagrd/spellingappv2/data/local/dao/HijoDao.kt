package com.sagrd.spellingappv2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sagrd.spellingappv2.data.local.entities.HijoEntity

@Dao
interface HijoDao {
    @Insert
    suspend fun insertHijo(hijo: HijoEntity): Int

    @Insert
    suspend fun insertHijos(hijos: List<HijoEntity>): List<Int>

    @Update
    suspend fun updateHijo(hijo: HijoEntity)

    @Delete
    suspend fun deleteHijo(hijo: HijoEntity)

    @Query("SELECT * FROM Hijos")
    suspend fun getAllHijos(): List<HijoEntity>

    @Query("SELECT * FROM Hijos WHERE hijoId = :id")
    suspend fun getHijoById(id: Int): HijoEntity?

    @Query("SELECT * FROM Hijos WHERE usuarioId = :usuarioId")
    suspend fun getHijosByUsuarioId(usuarioId: Int): List<HijoEntity>

    @Query("DELETE FROM Hijos WHERE usuarioId = :usuarioId")
    suspend fun deleteHijosByUsuarioId(usuarioId: Int)
}