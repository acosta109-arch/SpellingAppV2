package com.sagrd.spellingappv2.data.repository

import com.sagrd.spellingappv2.data.dao.PracticaDao
import com.sagrd.spellingappv2.model.Practica
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PracticaRepository @Inject constructor(
    private val practicaDao: PracticaDao
) {
    suspend fun upsert(practica: Practica){
        practicaDao.upsert(practica = practica)
    }
    suspend fun delete(practica: Practica){
        practicaDao.delete(practica = practica)
    }

    fun find(practicaId : Int) : Flow<Practica> {
        return practicaDao.find(practicaId = practicaId)
    }

    fun getListStream() : Flow<List<Practica>> {
        return practicaDao.getListStream()
    }
}