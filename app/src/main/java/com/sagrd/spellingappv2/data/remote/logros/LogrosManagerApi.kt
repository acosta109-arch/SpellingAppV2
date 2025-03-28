package com.sagrd.spellingappv2.data.remote.logros

import com.sagrd.spellingappv2.data.remote.dto.LogrosDto
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LogrosManagerApi {
    @GET("api/Logros")
    suspend fun getLogros(): List<LogrosDto>

    @GET("api/Logros/{id}")
    suspend fun getLogro(@Path("id") id: Int): LogrosDto

    @POST("api/Logros")
    suspend fun saveLogro(@Body logrosDto: LogrosDto?): LogrosDto

    @PUT("api/Logros/{id}")
    suspend fun actualizarLogro(
        @Path("id") logroId: Int,
        @Body logroDto: LogrosDto
    ): LogrosDto

    @DELETE("api/Logros/{id}")
    suspend fun deleteLogro(@Path("id") id: Int): ResponseBody
}