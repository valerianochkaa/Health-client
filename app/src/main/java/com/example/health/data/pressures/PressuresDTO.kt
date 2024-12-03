package com.example.health.data.pressures

data class PressuresDTO(
    val pressureId: Int? = null,
    val userIdPressure: Int,
    val upperValue: Int? = null,
    val lowerValue: Int? = null,
    val pulseValue: Int? = null,
    val recordDate: String? = null,
)