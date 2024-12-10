package com.example.health.data.drugCategory

import com.google.gson.annotations.SerializedName

data class DrugCategoriesDTO(
    @SerializedName("drugCategoryId")
    val drugCategoryId : Int? = null,
    @SerializedName("drugCategoryName")
    val drugCategoryName : String
)