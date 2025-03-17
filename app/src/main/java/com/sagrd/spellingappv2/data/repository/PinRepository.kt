package com.sagrd.spellingappv2.data.repository

import com.sagrd.spellingappv2.data.local.dao.PinDao
import com.sagrd.spellingappv2.data.local.entities.PinEntity
import javax.inject.Inject

class PinRepository @Inject constructor(
    private val pinDao: PinDao
){
    suspend fun savePin(pin: PinEntity) = pinDao.savePin(pin)

    suspend fun deletePin(pin: PinEntity) = pinDao.deletePin(pin)

    fun getAllPines() = pinDao.getAllPines()

    suspend fun getPinById(id: Int) = pinDao.getPinById(id)

    suspend fun getPinByValor(pin: String) = pinDao.getPinByValor(pin)

    suspend fun validarPin(pin: String): Boolean {
        val pinEntity = pinDao.getPinByValor(pin)
        return pinEntity != null
    }
}