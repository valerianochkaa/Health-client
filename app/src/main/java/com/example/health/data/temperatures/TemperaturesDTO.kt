package com.example.health.data.temperatures

data class TemperaturesDTO (
    val temperatureId: Int? = null,
    val userIdTemperature: Int,
    val temperatureValue: Double? = null,
    val recordDate: String? = null,
)