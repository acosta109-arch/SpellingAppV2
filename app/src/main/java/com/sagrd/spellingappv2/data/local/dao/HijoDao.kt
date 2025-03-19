package com.sagrd.spellingappv2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.sagrd.spellingappv2.data.local.entities.HijoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HijoDao {
    @Insert
    suspend fun insertHijo(hijo: HijoEntity): Long  // Room devolverá un Long aunque la clave primaria sea Int

    @Insert
    suspend fun insertHijos(hijos: List<HijoEntity>): List<Long>  // Para inserciones masivas

    @Upsert
    suspend fun saveHijo(hijo: HijoEntity)

    @Delete
    suspend fun deleteHijo(hijo: HijoEntity)

    @Query("SELECT * FROM Hijos")
    fun getAllHijos(): Flow<List<HijoEntity>>

    @Query("SELECT * FROM Hijos WHERE hijoId = :id")
    suspend fun getHijoById(id: Int): HijoEntity?

    @Query("SELECT * FROM Hijos WHERE usuarioId = :usuarioId")
    suspend fun getHijosByUsuarioId(usuarioId: Int): List<HijoEntity>

    @Query("DELETE FROM Hijos WHERE usuarioId = :usuarioId")
    suspend fun deleteHijosByUsuarioId(usuarioId: Int)
}
