package com.sagrd.spellingappv2.data.repositorios

import com.sagrd.spellingappv2.data.dao.PalabraDao
import com.sagrd.spellingappv2.model.Palabra
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PalabraRepository @Inject constructor(
    val palabraDao: PalabraDao
){
    suspend fun insertar(palabra: Palabra){
        palabraDao.insertar(palabra = palabra)
    }
    suspend fun eliminar(palabra: Palabra){
        palabraDao.eliminar(palabra = palabra)
    }

    fun buscar(palabraId : Int) : Flow<Palabra> {
        return palabraDao.buscar(palabraId = palabraId)
    }

    fun getList() : Flow<List<Palabra>> {
        return palabraDao.getList()
    }

}