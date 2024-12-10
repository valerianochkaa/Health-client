package com.example.health.data.drugsInstrucions

import retrofit2.http.GET
import retrofit2.http.Path

interface DrugInstructionsApi {
    @GET("drugs/instructions")
    suspend fun getAllDrugInstructions(): List<DrugInstructionsDTO>
    @GET("drug/instruction/{drugInstructionId}")
    suspend fun getDrugInstructionById(@Path("drugInstructionId") drugInstructionId: Int): DrugInstructionsDTO
    @GET("drug/instructionByDrugId/{drugId}")
    suspend fun getDrugInstructionByDrugId(@Path("drugId") drugId: Int): DrugInstructionsDTO
}