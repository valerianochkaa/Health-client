package com.example.health.data.drugs

data class DrugsList (
    val name : String,
    var categoryName: String,
    val like: Boolean,
    val description : String,
    val activeIngredients : String,
    val composition : String,
    val indications : String,
    val contraindications : String,
    val sideEffects : String,
    val overdose : String,
    val storageConditions : String,
    val prescriptionRequirements : Boolean,
    val price: Double,
    val analog: String,
)