package com.example.health.data.drugLike

import com.google.gson.annotations.SerializedName

data class DrugLikeDTO(
    @SerializedName("drugId")
    val drugId : Int,
    @SerializedName("userIdDrugLike")
    val userIdDrugLike : Int
)