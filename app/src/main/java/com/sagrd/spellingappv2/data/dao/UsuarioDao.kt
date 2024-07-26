package com.sagrd.spellingappv2.data.dao

import androidx.room.*
import com.sagrd.spellingappv2.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario)

    @Delete
    suspend fun eliminar(usuario: Usuario)

    @Query("SELECT * FROM Usuarios WHERE usuarioId =:usuarioId")

    fun buscar(usuarioId: Int): Flow<Usuario>

    @Query("SELECT * FROM Usuarios ORDER BY usuarioId")
    fun getList(): Flow<List<Usuario>>

}