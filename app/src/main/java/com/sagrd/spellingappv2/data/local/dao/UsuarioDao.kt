package com.sagrd.spellingappv2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.sagrd.spellingappv2.data.local.entities.UsuarioConHijoEntity
import com.sagrd.spellingappv2.data.local.entities.UsuarioEntity

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insertUsuario(usuario: UsuarioEntity): Int

    @Update
    suspend fun updateUsuario(usuario: UsuarioEntity)

    @Delete
    suspend fun deleteUsuario(usuario: UsuarioEntity)

    @Query("SELECT * FROM Usuarios")
    suspend fun getAllUsuarios(): List<UsuarioEntity>

    @Query("SELECT * FROM Usuarios WHERE usuarioId = :id")
    suspend fun getUsuarioById(id: Int): UsuarioEntity?

    @Transaction
    @Query("SELECT * FROM Usuarios WHERE usuarioId = :usuarioId")
    suspend fun getUsuarioConHijos(usuarioId: Int): UsuarioConHijoEntity?

    @Transaction
    @Query("SELECT * FROM Usuarios")
    suspend fun getAllUsuariosConHijos(): List<UsuarioConHijoEntity>
}