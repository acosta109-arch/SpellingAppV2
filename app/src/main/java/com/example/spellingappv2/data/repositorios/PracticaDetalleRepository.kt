package com.example.spellingappv2.data.repositorios

import com.example.spellingappv2.data.dao.PracticaDetalleDao
import com.example.spellingappv2.model.PracticaDetalle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PracticaDetalleRepository @Inject constructor(
    val detalleDao: PracticaDetalleDao
) {
    suspend fun insertar(detalle: PracticaDetalle){
        detalleDao.insertar(detalle = detalle)
    }
    suspend fun eliminar(detalle: PracticaDetalle){
        detalleDao.eliminar(detalle = detalle)
    }

    fun buscar(practicaId : Int) : Flow<PracticaDetalle> {
        return detalleDao.buscar(detalleId = practicaId)
    }

    fun getList() : Flow<List<PracticaDetalle>> {
        return detalleDao.getList()
    }
}