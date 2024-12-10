package com.example.health.data.pressures

import com.google.gson.annotations.SerializedName

data class PressuresDTO(
    @SerializedName("pressureId")
    val pressureId: Int? = null,
    @SerializedName("userIdPressure")
    val userIdPressure: Int,
    @SerializedName("upperValue")
    val upperValue: Int,
    @SerializedName("lowerValue")
    val lowerValue: Int,
    @SerializedName("pulseValue")
    val pulseValue: Int,
    @SerializedName("recordDate")
    val recordDate: String
)