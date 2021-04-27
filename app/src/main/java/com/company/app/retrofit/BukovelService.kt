package com.company.app.retrofit

import com.company.app.ui.services.Service
import io.reactivex.Single
import retrofit2.http.GET

interface BukovelService {
    @GET("GetService")
    fun getTestService() : Single<Service>
}