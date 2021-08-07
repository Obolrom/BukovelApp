package com.company.app.services

import com.company.app.ui.services.Service
import com.company.app.ui.services.ServiceReview
import retrofit2.Call
import retrofit2.http.*

interface BukovelService {
    @GET("GetServices")
    fun getServices() : List<Service>

    @GET("GetServiceReview")
    fun getServiceReviews(@Query("name") service: String) : List<ServiceReview>

    @FormUrlEncoded
    @POST("Register")
    fun register(@Field("name") name: String,
                 @Field("password") password: String) : Call<Boolean>
}