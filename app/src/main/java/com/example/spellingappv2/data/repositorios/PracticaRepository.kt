package com.example.spellingappv2.data.repositorios

import com.example.spellingappv2.data.dao.PracticaDao
import com.example.spellingappv2.model.Practica
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PracticaRepository @Inject constructor(
    val practicaDao: PracticaDao
) {
    suspend fun insertar(practica: Practica){
        practicaDao.insertar(practica = practica)
    }
    suspend fun eliminar(practica: Practica){
        practicaDao.eliminar(practica = practica)
    }

    fun buscar(practicaId : Int) : Flow<Practica> {
        return practicaDao.buscar(practicaId = practicaId)
    }

    fun getList() : Flow<List<Practica>> {
        return practicaDao.getList()
    }
}