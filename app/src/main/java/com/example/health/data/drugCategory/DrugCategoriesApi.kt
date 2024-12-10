package com.example.health.data.drugCategory

import retrofit2.http.GET
import retrofit2.http.Path

interface DrugCategoriesApi {
    @GET("drugs/categories")
    suspend fun getAllDrugCategories(): List<DrugCategoriesDTO>

    @GET("drug/category/{drugCategoryId}")
    suspend fun getDrugCategoryById(@Path("drugCategoryId") drugCategoryId: Int): DrugCategoriesDTO

    @GET("drug/categoryByDrugId/{drugId}")
    suspend fun getDrugCategoryByDrugId(@Path("drugId") drugId: Int): DrugCategoriesDTO
}