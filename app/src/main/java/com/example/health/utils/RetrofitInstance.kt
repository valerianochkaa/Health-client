package com.example.health.utils

import com.example.health.data.drugs.DrugsApi
import com.example.health.data.drugsInstrucions.DrugInstructionsApi
import com.example.health.data.pressures.PressuresApi
import com.example.health.data.temperatures.TemperaturesApi
import com.example.health.data.weights.WeightsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiWeights: WeightsApi by lazy {
        retrofit.create(WeightsApi::class.java)
    }
    val apiTemperatures: TemperaturesApi by lazy {
        retrofit.create(TemperaturesApi::class.java)
    }
    val apiPressures: PressuresApi by lazy {
        retrofit.create(PressuresApi::class.java)
    }
    val drugsApi: DrugsApi by lazy {
        retrofit.create(DrugsApi::class.java)
    }
    val drugInstructionsApi: DrugInstructionsApi by lazy {
        retrofit.create(DrugInstructionsApi::class.java)
    }
}