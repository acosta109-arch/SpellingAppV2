package com.sagrd.spellingappv2.data.repository

import com.sagrd.spellingappv2.data.dao.PracticaDetalleDao
import com.sagrd.spellingappv2.model.PracticaDetalle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PracticaDetalleRepository @Inject constructor(
    val detalleDao: PracticaDetalleDao
) {
    suspend fun upsert(detalle: PracticaDetalle){
        detalleDao.upsert(detalle = detalle)
    }
    suspend fun delete(detalle: PracticaDetalle){
        detalleDao.delete(detalle = detalle)
    }

    fun find(practicaId : Int) : Flow<PracticaDetalle> {
        return detalleDao.find(detalleId = practicaId)
    }

    fun getListStream() : Flow<List<PracticaDetalle>> {
        return detalleDao.getListStream()
    }
}