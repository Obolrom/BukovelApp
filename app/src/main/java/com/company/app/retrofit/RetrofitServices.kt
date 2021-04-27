package com.company.app.retrofit

object RetrofitServices {
    private const val BASE_URL = "https://192.168.0.104:5001/Home/"

    val bukovelService: BukovelService
        get() = RetrofitClient.getBukovelClient(BASE_URL).create(BukovelService::class.java)
}