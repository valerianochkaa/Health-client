package com.example.health.data.drugs

data class DrugsDTO (
    val drugId: Int? = null,
    val drugCategoryId: Int,
    val drugName: String? = null,
    val drugPrice: Double? = null,
    val drugAnalog: String? = null,
)