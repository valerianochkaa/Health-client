package com.example.health.data.weights

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WeightsApi {
    @GET("weights")
    suspend fun getAllWeights(): List<WeightsDTO>

    @GET("weights/user/{userId}")
    suspend fun getAllWeightsByUser(@Path("userId") userId: Int): List<WeightsDTO>

    @GET("weight/{weightId}")
    suspend fun getWeightById(@Path("weightId") weightId: Int): WeightsDTO

    @POST("weight/insert")
    suspend fun insertWeightAndGetId(@Body weight: WeightsDTO): WeightsDTO

    @DELETE("weight/delete/{weightId}")
    suspend fun deleteWeightById(@Path("weightId") weightId: Int)
}