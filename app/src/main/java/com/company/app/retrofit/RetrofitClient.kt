package com.company.app.retrofit

import com.company.app.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.ContinuationInterceptor

object RetrofitClient {
    private var retrofit: Retrofit? = null
    private val converter: Converter.Factory
    private val callAdapter: CallAdapter.Factory
    private val okHttpClient: OkHttpClient

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE

        okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

        converter = GsonConverterFactory.create()
        callAdapter = RxJava2CallAdapterFactory.create()
    }

    fun getBukovelClient(url: String): Retrofit {
        retrofit = retrofit ?: (
                Retrofit.Builder()
                        .baseUrl(url)
                        .client(okHttpClient)
                        .addConverterFactory(converter)
                        .addCallAdapterFactory(callAdapter)
                        .build()
            )

        return retrofit!!
    }
}