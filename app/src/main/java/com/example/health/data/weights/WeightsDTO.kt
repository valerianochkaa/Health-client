package com.example.health.data.weights

data class WeightsDTO (
    val weightId: Int? = null,
    val userIdWeights: Int,
    val weightValue: Double,
    val recordDate: String
)