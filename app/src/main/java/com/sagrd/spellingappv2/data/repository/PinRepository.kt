package com.sagrd.spellingappv2.data.repository

import com.sagrd.spellingappv2.data.local.dao.PinDao
import com.sagrd.spellingappv2.data.local.entities.PinEntity
import com.sagrd.spellingappv2.data.remote.pines.PinesDataSource
import com.sagrd.spellingappv2.data.remote.Resource
import com.sagrd.spellingappv2.data.remote.dto.PalabrasDto
import com.sagrd.spellingappv2.data.remote.dto.PinesDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class PinRepository @Inject constructor(
    private val dataSource: PinesDataSource,
    private val pinDao: PinDao
) {
    fun getPines(): Flow<Resource<List<PinEntity>>> = flow {
        emit(Resource.Loading())
        try {
            val pinesRemotas = dataSource.getPines()

            val listaPines = pinesRemotas.map { dto ->
                PinEntity(
                    pinId = dto.pinId,
                    pin = dto.pin
                )
            }
            pinDao.savePin(listaPines)
            emit(Resource.Success(listaPines))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexión $errorMessage"))
        } catch (e: Exception) {
            val pinesLocales = pinDao.getAllPines().first()
            if (pinesLocales.isNotEmpty())
                emit(Resource.Success(pinesLocales))
            else
                emit(Resource.Error("Error de conexión: ${e.message}"))
        }
    }

    suspend fun update(id: Int, pineDto: PinesDto) = dataSource.actualizarPine(id, pineDto)

    suspend fun find(id: Int): PinEntity? {
        val pinesDto = dataSource.getPines()
        return pinesDto
            .firstOrNull { it.pinId == id }
            ?.let { pineDto ->
                PinEntity(
                    pinId = pineDto.pinId,
                    pin = pineDto.pin
                )
            }
    }

    suspend fun save(pineDto: PinesDto) = dataSource.savePine(pineDto)

    suspend fun delete(id: Int) = dataSource.deletePine(id)
}