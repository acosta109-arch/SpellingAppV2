package com.sagrd.spellingappv2.data.remote.pines

import com.sagrd.spellingappv2.data.remote.dto.PinesDto
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PinesManagerApi {
    @GET("api/Pines")
    suspend fun getPines(): List<PinesDto>

    @GET("api/Pines/{id}")
    suspend fun getPin(@Path("id") id: Int): PinesDto

    @POST("api/Pines")
    suspend fun savePin(@Body pineDto: PinesDto?): PinesDto

    @PUT("api/Pines/{id}")
    suspend fun actualizarPin(
        @Path("id") pineId: Int,
        @Body pineDto: PinesDto
    ): PinesDto

    @DELETE("api/Pines/{id}")
    suspend fun deletePin(@Path("id") id: Int): ResponseBody
}