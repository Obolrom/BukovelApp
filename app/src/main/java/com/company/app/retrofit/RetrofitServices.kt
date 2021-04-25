package com.company.app.retrofit

object RetrofitServices {
    private const val BASE_URL = "https://www.simplifiedcoding.net/demos/"

    val bukovelService: BukovelService
        get() = RetrofitClient.getBukovelClient(BASE_URL).create(BukovelService::class.java)
}