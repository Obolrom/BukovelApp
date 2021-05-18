package com.company.app.repository

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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope

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

    fun callRetrofitApi() : MutableLiveData<List<Service>> {
        val services: MutableLiveData<List<Service>> = MutableLiveData<List<Service>>()
        bukovelService.getTestService()
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
}