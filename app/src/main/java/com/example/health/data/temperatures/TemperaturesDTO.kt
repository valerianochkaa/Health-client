package com.example.health.data.temperatures

import com.google.gson.annotations.SerializedName

data class TemperaturesDTO (
    @SerializedName("temperatureId")
    val temperatureId: Int? = null,
    @SerializedName("userIdTemperature")
    val userIdTemperature: Int,
    @SerializedName("temperatureValue")
    val temperatureValue: Double,
    @SerializedName("recordDate")
    val recordDate: String
)