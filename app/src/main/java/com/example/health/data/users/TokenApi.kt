package com.example.health.data.users

import com.example.database.tokens.TokenDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenApi {
    @POST("/token/check")
    suspend fun checkToken(@Body tokenDTO: TokenDTO): TokenRemote
}
data class TokenRemote(
    val valid: Boolean,
    val message: String
)