package com.sagrd.spellingappv2.data.remote.palabras

import com.sagrd.spellingappv2.data.remote.dto.PalabrasDto
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PalabrasManagerApi {
    @GET("api/Palabras")
    suspend fun getPalabras(): List<PalabrasDto>

    @GET("api/Palabras/{id}")
    suspend fun getPalabra(@Path("id") id: Int): PalabrasDto

    @POST("api/Palabras")
    suspend fun savePalabra(@Body palabraDto: PalabrasDto?): PalabrasDto

    @PUT("api/Palabras/{id}")
    suspend fun actualizarPalabra(
        @Path("id") palabraId: Int,
        @Body palabraDto: PalabrasDto
    ): PalabrasDto

    @DELETE("api/Palabras/{id}")
    suspend fun deletePalabra(@Path("id") id: Int): ResponseBody
}
