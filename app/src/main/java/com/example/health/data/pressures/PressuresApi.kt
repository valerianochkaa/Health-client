package com.example.health.data.pressures

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PressuresApi {
    @GET("pressures")
    suspend fun getAllPressures(): List<PressuresDTO>

    @GET("pressures/user/{userId}")
    suspend fun getAllPressuresByUser(@Path("userId") userId: Int): List<PressuresDTO>

    @GET("pressure/{pressureId}")
    suspend fun getPressureById(@Path("pressureId") pressureId: Int): PressuresDTO

    @POST("pressure/insert")
    suspend fun insertPressureAndGetId(@Body pressure: PressuresDTO): PressuresDTO

    @DELETE("pressure/delete/{pressureId}")
    suspend fun deletePressureById(@Path("pressureId") pressureId: Int)
}
