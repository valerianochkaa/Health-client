package com.example.database.tokens

import com.google.gson.annotations.SerializedName

data class  TokenDTO (
    @SerializedName("tokenId")
    val tokenId: String,
    @SerializedName("tokenEmail")
    val tokenEmail: String,
    @SerializedName("token")
    val token: String
)