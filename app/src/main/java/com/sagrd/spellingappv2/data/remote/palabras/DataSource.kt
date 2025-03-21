package com.sagrd.spellingappv2.data.remote.palabras

import com.sagrd.spellingappv2.data.remote.dto.PalabrasDto
import javax.inject.Inject

class PalabrasDataSource @Inject constructor(
    private val palabrasManagerApi: PalabrasManagerApi
) {
    suspend fun getPalabras() = palabrasManagerApi.getPalabras()

    suspend fun getPalabra(id: Int) = palabrasManagerApi.getPalabra(id)

    suspend fun savePalabra(palabraDto: PalabrasDto) = palabrasManagerApi.savePalabra(palabraDto)

    suspend fun actualizarPalabra(id: Int, palabraDto: PalabrasDto) = palabrasManagerApi.actualizarPalabra(id, palabraDto)

    suspend fun deletePalabra(id: Int) = palabrasManagerApi.deletePalabra(id)
}


