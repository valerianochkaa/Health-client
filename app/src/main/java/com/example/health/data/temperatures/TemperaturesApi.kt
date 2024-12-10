package com.example.health.data.temperatures

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TemperaturesApi {
    @POST("temperature/insert")
    suspend fun insertTemperatureAndGetId(@Body temperature: TemperaturesDTO): TemperaturesDTO
    @GET("temperatures")
    suspend fun getAllTemperatures(): List<TemperaturesDTO>

    @GET("temperatures/user/{userId}")
    suspend fun getAllTemperaturesByUser(@Path("userId") userId: Int): List<TemperaturesDTO>

    @GET("temperature/{temperatureId}")
    suspend fun getTemperatureById(@Path("temperatureId") temperatureId: Int): TemperaturesDTO



    @DELETE("temperature/delete/{temperatureId}")
    suspend fun deleteTemperatureById(@Path("temperatureId") temperatureId: Int)
}
