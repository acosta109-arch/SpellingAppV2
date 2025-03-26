package com.sagrd.spellingappv2.data.remote.pines

import com.sagrd.spellingappv2.data.remote.dto.PinesDto
import javax.inject.Inject

class PinesDataSource @Inject constructor(
    private val pinesManagerApi: PinesManagerApi
) {
    suspend fun getPines() = pinesManagerApi.getPines()

    suspend fun getPine(id: Int) = pinesManagerApi.getPin(id)

    suspend fun savePine(pineDto: PinesDto) = pinesManagerApi.savePin(pineDto)

    suspend fun actualizarPine(id: Int, pineDto: PinesDto) = pinesManagerApi.actualizarPin(id, pineDto)

    suspend fun deletePine(id: Int) = pinesManagerApi.deletePin(id)
}