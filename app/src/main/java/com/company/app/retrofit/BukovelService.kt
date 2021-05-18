package com.company.app.retrofit

import com.company.app.ui.services.Service
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface BukovelService {
    @GET("GetServices")
    fun getTestService() : Single<List<Service>>
}