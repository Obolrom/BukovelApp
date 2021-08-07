package com.company.app.services

import com.company.app.ui.services.Service
import com.company.app.ui.services.ServiceReview
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface BukovelService {
    @GET("GetServices")
    fun getServices() : Single<List<Service>>

    @GET("GetServiceReview")
    fun getServiceReviews(@Query("name") service: String) : Single<List<ServiceReview>>

    @FormUrlEncoded
    @POST("Register")
    fun register(@Field("name") name: String,
                 @Field("password") password: String) : Call<Boolean>
}