package com.sagrd.spellingappv2.data.repository

import com.sagrd.spellingappv2.data.dao.UsuarioDao
import com.sagrd.spellingappv2.model.Usuario
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    val usuarioDao: UsuarioDao
) {
    suspend fun upsert(usuario: Usuario) {
        usuarioDao.upsert(usuario = usuario)
    }

    suspend fun delete(usuario: Usuario) {
        usuarioDao.delete(usuario = usuario)
    }

    fun find(usuarioId: Int): Flow<Usuario> {
        return usuarioDao.find(usuarioId = usuarioId)
    }

    fun getListStream(): Flow<List<Usuario>> {
        return usuarioDao.getListStream()
    }
}