package com.example.health.data.drugLike

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DrugLikeApi {
    @GET("drugs/likes/{userId}")
    suspend fun getAllDrugLikes(@Path("userId") userId: Int): List<DrugLikeDTO>

    @GET("drugs/likesDrug/{userId}")
    suspend fun getDrugLikesByUser(@Path("userId") userId: Int): List<DrugLikeDTO>

    @POST("drug/insertLike/{drugId}")
    suspend fun insertLike(@Path("drugId") drugId: Int, @Body like: DrugLikeDTO): DrugLikeDTO

    @DELETE("drug/deleteLike/{drugId}")
    suspend fun deleteLikeByDrugId(@Path("drugId") drugId: Int)

}