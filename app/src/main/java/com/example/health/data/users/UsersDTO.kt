package com.example.health.data.users

import com.google.gson.annotations.SerializedName

data class UsersDTO (
    @SerializedName("userId")
    val userId: Int? = null,
    @SerializedName("userEmail")
    val userEmail: String,
    @SerializedName("userPassword")
    val userPassword: String
)