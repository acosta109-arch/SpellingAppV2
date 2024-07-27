package com.sagrd.spellingappv2.data.repository

import com.sagrd.spellingappv2.data.dao.PalabraDao
import com.sagrd.spellingappv2.model.Palabra
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PalabraRepository @Inject constructor(
    val palabraDao: PalabraDao
){
    suspend fun upsert(palabra: Palabra){
        palabraDao.upsert(palabra = palabra)
    }
    suspend fun delete(palabra: Palabra){
        palabraDao.delete(palabra = palabra)
    }

    fun find(palabraId : Int) : Flow<Palabra> {
        return palabraDao.find(palabraId = palabraId)
    }

    fun getListStream() : Flow<List<Palabra>> {
        return palabraDao.getListStream()
    }

}