package com.example.health.data.drugs

import com.google.gson.annotations.SerializedName

data class DrugsDTO(
    @SerializedName("drugId")
    val drugId: Int? = null,
    @SerializedName("drugCategoryId")
    val drugCategoryId: Int,
    @SerializedName("drugInstructionId")
    val drugInstructionId: Int,
    @SerializedName("drugName")
    val drugName: String,
    @SerializedName("drugPrice")
    val drugPrice: Double,
    @SerializedName("drugAnalog")
    val drugAnalog: String,
)