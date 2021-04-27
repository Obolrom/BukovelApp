package com.company.app.repository

import com.company.app.App
import com.company.app.retrofit.BukovelService
import com.company.app.retrofit.RetrofitServices
import com.company.app.ui.services.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.SupervisorJob

class Repository(private val app: App) {
    private val bukovelService: BukovelService by lazy { RetrofitServices.bukovelService }

    fun callRetrofitApi() : Service {
        var service: Service = Service(4.3f, "sdf", "dsfs")
        bukovelService.getTestService()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<Service>() {
                override fun onSuccess(response: Service) {
                    service = response
                }

                override fun onError(e: Throwable) { }
            })
        return service
    }
}