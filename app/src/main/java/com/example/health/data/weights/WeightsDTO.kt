package com.example.health.data.weights

import com.google.gson.annotations.SerializedName

data class WeightsDTO (
    @SerializedName("weightId")
    val weightId: Int? = null,
    @SerializedName("userIdWeights")
    val userIdWeights: Int,
    @SerializedName("weightValue")
    val weightValue: Double,
    @SerializedName("recordDate")
    val recordDate: String
)
