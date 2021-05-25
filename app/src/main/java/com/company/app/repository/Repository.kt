package com.company.app.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.company.app.App
import com.company.app.retrofit.BukovelService
import com.company.app.retrofit.RetrofitServices
import com.company.app.ui.map.EdgeRepresentation
import com.company.app.ui.map.Lift
import com.company.app.ui.map.Slope
import com.company.app.ui.map.Vertex
import com.company.app.ui.services.Service
import com.company.app.ui.services.ServiceReview
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val app: App) {
    private val bukovelService: BukovelService by lazy { RetrofitServices.bukovelService }
    private val database: BukovelDatabase by lazy {
        BukovelDatabase.getDatabase(app.applicationContext, coroutineScope)
    }

    val coroutineScope: CoroutineScope = app.coroutineScope

    val vertices: Array<Vertex> = app.vertices
    val edgeRepresentations: List<EdgeRepresentation> = app.edges

    val slopes: LiveData<List<Slope>> = MutableLiveData<List<Slope>>().apply {
        value = app.slopes
    }

    val lifts: LiveData<List<Lift>> = MutableLiveData<List<Lift>>().apply {
        value = app.lifts
    }

    fun getServices() : MutableLiveData<List<Service>> {
        val services: MutableLiveData<List<Service>> = MutableLiveData<List<Service>>()
        bukovelService.getServices()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<List<Service>>() {
                override fun onSuccess(response: List<Service>) {
                    services.value = response
                }

                override fun onError(e: Throwable) { }
            })
        return services
    }

    fun getServiceReviews(serviceName: String) : MutableLiveData<List<ServiceReview>> {
        val reviews: MutableLiveData<List<ServiceReview>> = MutableLiveData<List<ServiceReview>>()
        bukovelService.getServiceReviews(serviceName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<List<ServiceReview>>() {
                override fun onSuccess(response: List<ServiceReview>) {
                    reviews.value = response
                }

                override fun onError(e: Throwable) { }
            })
        return reviews
    }

    fun registerUser(nickname: String, password: String) {
        bukovelService.register(nickname, password)
            .enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.d("net", "response ${response.message()}")
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.d("net", "fail")
                }
            })
    }
}