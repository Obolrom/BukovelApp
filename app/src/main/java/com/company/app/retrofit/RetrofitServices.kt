package com.company.app.retrofit

object RetrofitServices {
    private const val BASE_URL = "http://ikolopatin-001-site1.dtempurl.com/Home/"

    val bukovelService: BukovelService
        get() = RetrofitClient.getBukovelClient(BASE_URL).create(BukovelService::class.java)
}