package com.example.health.data.drugsInstrucions

import com.google.gson.annotations.SerializedName

data class DrugInstructionsDTO (
    @SerializedName("drugInstructionId")
    val drugInstructionId : Int? = null,
    @SerializedName("description")
    val description: String,
    @SerializedName("activeIngredients")
    val activeIngredients: String,
    @SerializedName("composition")
    val composition: String,
    @SerializedName("indications")
    val indications: String,
    @SerializedName("contraindications")
    val contraindications: String,
    @SerializedName("sideEffects")
    val sideEffects: String,
    @SerializedName("overdose")
    val overdose: String,
    @SerializedName("storageConditions")
    val storageConditions: String,
    @SerializedName("prescriptionRequirements")
    val prescriptionRequirements: Boolean
)