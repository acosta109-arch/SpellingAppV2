package com.sagrd.spellingappv2.data.repository

import com.sagrd.spellingappv2.data.local.dao.HijoDao
import com.sagrd.spellingappv2.data.local.entities.HijoEntity
import javax.inject.Inject


class HijoRepository @Inject constructor(
    private val hijoDao: HijoDao
){
    suspend fun insertHijo(hijo: HijoEntity): Long {
        return hijoDao.insertHijo(hijo)
    }

    suspend fun insertHijos(hijos: List<HijoEntity>): List<Long> {
        return hijoDao.insertHijos(hijos)
    }

    suspend fun saveHijo(hijo: HijoEntity) {
        hijoDao.saveHijo(hijo)
    }

    suspend fun deleteHijo(hijo: HijoEntity) = hijoDao.deleteHijo(hijo)


    suspend fun getAllHijos() = hijoDao.getAllHijos()


    suspend fun getHijoById(id: Int) = hijoDao.getHijoById(id)


    suspend fun getHijosByUsuarioId(usuarioId: Int): List<HijoEntity> {
        return hijoDao.getHijosByUsuarioId(usuarioId)
    }

    suspend fun deleteHijosByUsuarioId(usuarioId: Int) {
        hijoDao.deleteHijosByUsuarioId(usuarioId)
    }

    suspend fun getHijosCount(): Int {
        return hijoDao.getHijosCount()
    }
}