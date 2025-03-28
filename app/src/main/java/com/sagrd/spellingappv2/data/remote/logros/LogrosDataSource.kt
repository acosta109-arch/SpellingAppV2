package com.sagrd.spellingappv2.data.remote.logros

import com.sagrd.spellingappv2.data.remote.dto.LogrosDto
import javax.inject.Inject

class LogrosDataSource @Inject constructor(
    private val logrosManagerApi: LogrosManagerApi
){
    suspend fun getLogros() = logrosManagerApi.getLogros()

    suspend fun getLogro(id: Int) = logrosManagerApi.getLogro(id)

    suspend fun saveLogro(logrosDto: LogrosDto) = logrosManagerApi.saveLogro(logrosDto)

    suspend fun actualizarLogro(id: Int, logrosDto: LogrosDto) = logrosManagerApi.actualizarLogro(id, logrosDto)

    suspend fun deleteLogro(id: Int) = logrosManagerApi.deleteLogro(id)
}