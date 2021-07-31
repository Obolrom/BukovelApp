package com.company.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.company.app.di.AppComponent
import com.company.app.di.AppInjector
import com.company.app.repository.MapRepository
import timber.log.Timber
import javax.inject.Inject

class App: Application(), ViewModelStoreOwner, LifecycleObserver {

    lateinit var appComponent: AppComponent

    // should make prefetch, because
    @Inject
    lateinit var mapRepository: MapRepository

    override fun onCreate() {
        super.onCreate()

        AppInjector.init(this)

        appComponent = AppInjector.appComponent

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun getViewModelStore() = ViewModelStore()
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }