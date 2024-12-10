package com.example.health.data.drugs

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DrugsApi {
    @GET("drugs")
    suspend fun getAllDrugs(): List<DrugsDTO>

    @GET("drug/{drugId}")
    suspend fun getDrugById(@Path("drugId") drugId: Int): DrugsDTO

    @GET("drugs/categories/{drugCategoryId}")
    suspend fun getAllDrugsByCategory(@Path("drugCategoryId") drugCategoryId: Int): List<DrugsDTO>
}
