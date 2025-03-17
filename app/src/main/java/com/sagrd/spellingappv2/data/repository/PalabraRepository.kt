package com.sagrd.spellingappv2.data.repository

import com.sagrd.spellingappv2.data.local.dao.PalabraDao
import com.sagrd.spellingappv2.data.local.entities.PalabraEntity
import javax.inject.Inject

class PalabraRepository @Inject constructor (
    private val palabraDao: PalabraDao
){

    suspend fun savePalabra(palabra: PalabraEntity) = palabraDao.save(palabra)

    suspend fun deletePalabra(palabra: PalabraEntity) = palabraDao.deletePalabra(palabra)

    fun getAllPalabras() = palabraDao.getAllPalabras()

    suspend fun getPalabraById(id: Int) = palabraDao.getPalabraById(id)

    suspend fun buscarPalabrasPorTermino(termino: String): List<PalabraEntity> = palabraDao.buscarPalabrasPorTermino("%$termino%")

}