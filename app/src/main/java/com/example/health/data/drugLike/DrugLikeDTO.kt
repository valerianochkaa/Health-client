package com.example.health.data.drugLike

import com.google.gson.annotations.SerializedName

data class DrugLikeDTO(
    @SerializedName("drugIdLike")
    val drugIdLike : Int,
    @SerializedName("userIdDrugLike")
    val userIdDrugLike : Int
)