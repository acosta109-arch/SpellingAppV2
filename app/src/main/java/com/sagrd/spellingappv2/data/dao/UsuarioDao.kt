package com.sagrd.spellingappv2.data.dao

import androidx.room.*
import com.sagrd.spellingappv2.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Upsert()
    suspend fun upsert(usuario: Usuario)

    @Delete
    suspend fun delete(usuario: Usuario)

    @Query("SELECT * FROM Usuarios WHERE usuarioId =:usuarioId")
    fun find(usuarioId: Int): Flow<Usuario>

    @Query("SELECT * FROM Usuarios ORDER BY usuarioId")
    fun getListStream(): Flow<List<Usuario>>

}