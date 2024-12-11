package com.example.health.data.users

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersApi {
    @POST("login")
    fun loginUser(@Body userCredentials: UserCredentials): Call<LoginResponse>
    @POST("register")
    fun registerUser(@Body userCredentials: UserCredentials): Call<LoginResponse>
}
data class UserCredentials(
    val email: String,
    val password: String
)
data class LoginResponse(
    val token: String
)
