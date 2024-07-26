package com.sagrd.spellingappv2.data.repositorios

import com.sagrd.spellingappv2.data.dao.UsuarioDao
import com.sagrd.spellingappv2.model.Usuario
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    val usuarioDao: UsuarioDao
) {
    suspend fun insertar(usuario: Usuario) {
        usuarioDao.insertar(usuario = usuario)
    }

    suspend fun eliminar(usuario: Usuario) {
        usuarioDao.eliminar(usuario = usuario)
    }

    fun buscar(usuarioId: Int): Flow<Usuario> {
        return usuarioDao.buscar(usuarioId = usuarioId)
    }

    fun getList(): Flow<List<Usuario>> {
        return usuarioDao.getList()
    }
}