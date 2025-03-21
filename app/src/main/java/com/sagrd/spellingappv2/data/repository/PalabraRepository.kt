package com.sagrd.spellingappv2.data.repository

import com.sagrd.spellingappv2.data.local.dao.PalabraDao
import com.sagrd.spellingappv2.data.local.entities.PalabraEntity
import com.sagrd.spellingappv2.data.remote.palabras.PalabrasDataSource
import com.sagrd.spellingappv2.data.remote.Resource
import com.sagrd.spellingappv2.data.remote.dto.PalabrasDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import android.util.Log

class PalabraRepository @Inject constructor(
    private val dataSource: PalabrasDataSource,
    private val palabraDao: PalabraDao
) {
    fun getPalabras(): Flow<Resource<List<PalabraEntity>>> = flow {
        emit(Resource.Loading())
        try {
            val palabrasRemotas = dataSource.getPalabras()

            val listaPalabras = palabrasRemotas.map { dto ->
                PalabraEntity(
                    palabraId = dto.PalabraId,
                    palabra = dto.Nombre,
                    descripcion = dto.Descripcion,
                    fotoUrl = dto.FotoUrl
                )
            }
            palabraDao.save(listaPalabras)
            emit(Resource.Success(listaPalabras))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexión $errorMessage"))
        } catch (e: Exception) {
            val palabrasLocales = palabraDao.getAllPalabras().first()
            if (palabrasLocales.isNotEmpty())
                emit(Resource.Success(palabrasLocales))
            else
                emit(Resource.Error("Error de conexión: ${e.message}"))
        }
    }

    suspend fun update(id: Int, palabraDto: PalabrasDto) = dataSource.actualizarPalabra(id, palabraDto)

    suspend fun find(id: Int): PalabraEntity? {
        val palabrasDto = dataSource.getPalabras()
        return palabrasDto
            .firstOrNull { it.PalabraId == id }
            ?.let { palabraDto ->
                PalabraEntity(
                    palabraId = palabraDto.PalabraId,
                    palabra = palabraDto.Nombre,
                    descripcion = palabraDto.Descripcion,
                    fotoUrl = palabraDto.FotoUrl
                )
            }
    }

    suspend fun save(palabraDto: PalabrasDto) = dataSource.savePalabra(palabraDto)

    suspend fun delete(id: Int) = dataSource.deletePalabra(id)
}
