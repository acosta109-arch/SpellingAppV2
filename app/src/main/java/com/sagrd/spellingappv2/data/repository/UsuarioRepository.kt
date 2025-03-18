package com.sagrd.spellingappv2.data.repository

import com.sagrd.spellingappv2.data.local.dao.UsuarioDao
import com.sagrd.spellingappv2.data.local.entities.UsuarioConHijoEntity
import com.sagrd.spellingappv2.data.local.entities.UsuarioEntity
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val usuarioDao: UsuarioDao
){
    suspend fun insertUsuario(usuario: UsuarioEntity): Long {
        return usuarioDao.insertUsuario(usuario)
    }

    suspend fun updateUsuario(usuario: UsuarioEntity) {
        usuarioDao.updateUsuario(usuario)
    }

    suspend fun deleteUsuario(usuario: UsuarioEntity) {
        usuarioDao.deleteUsuario(usuario)
    }

    suspend fun getAllUsuarios(): List<UsuarioEntity> {
        return usuarioDao.getAllUsuarios()
    }

    suspend fun getUsuarioById(id: Int): UsuarioEntity? {
        return usuarioDao.getUsuarioById(id)
    }

    suspend fun getUsuarioConHijos(usuarioId: Int): UsuarioConHijoEntity? {
        return usuarioDao.getUsuarioConHijos(usuarioId)
    }

    suspend fun getAllUsuariosConHijos(): List<UsuarioConHijoEntity> {
        return usuarioDao.getAllUsuariosConHijos()
    }
}