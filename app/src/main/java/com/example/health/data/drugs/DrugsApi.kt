package com.example.health.data.drugs

import retrofit2.http.GET
import retrofit2.http.Path

interface DrugsApi {
    @GET("drugs/{id}")
    suspend fun getDrugById(@Path("id") id: Int): DrugsDTO
}