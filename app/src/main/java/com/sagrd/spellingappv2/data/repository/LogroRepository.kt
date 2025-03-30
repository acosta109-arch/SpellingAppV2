package com.sagrd.spellingappv2.data.repository

import com.sagrd.spellingappv2.data.remote.Resource
import com.sagrd.spellingappv2.data.remote.dto.LogrosDto
import com.sagrd.spellingappv2.data.remote.logros.LogrosDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import retrofit2.HttpException
import android.util.Log
import com.sagrd.spellingappv2.data.remote.logros.LogrosManagerApi

class LogroRepository @Inject constructor(
    private val dataSource: LogrosDataSource,
    private val logrosManagerApi: LogrosManagerApi
){
    fun getLogros(): Flow<Resource<List<LogrosDto>>> = flow {
        try{
            emit(Resource.Loading())
            val logros = dataSource.getLogros()
            emit(Resource.Success(logros))
        }catch (e: HttpException){
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            Log.e("LogroRepository", "HttpException: $errorMessage")
            emit(Resource.Error("Error de conexion $errorMessage"))
        }catch (e: Exception){
            Log.e("LogroRepository", "Exception: ${e.message}")
            emit(Resource.Error("Error: ${e.message}"))

        }
    }

    suspend fun update(id: Int, logrosDto: LogrosDto) =
        dataSource.actualizarLogro(id, logrosDto)

    suspend fun find(id: Int) = dataSource.getLogro(id)

    suspend fun save(logrosDto: LogrosDto) = dataSource.saveLogro(logrosDto)

    suspend fun delete(id: Int) = dataSource.deleteLogro(id)

    suspend fun getLogrosCount(): Int {
        return try {
            val logros = logrosManagerApi.getLogros()
            logros.size
        } catch (e: Exception) {
            0
        }
    }

}