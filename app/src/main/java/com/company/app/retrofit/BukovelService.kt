package com.company.app.retrofit

import com.company.app.ui.services.Service
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface BukovelService {
    @GET("GetServices")
    fun getTestService() : Single<List<Service>>

    @FormUrlEncoded
    @POST("Register")
    fun register(@Field("name") name: String,
                 @Field("password") password: String) : Call<Boolean>
}