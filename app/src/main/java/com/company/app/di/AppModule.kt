package com.company.app.di

import android.content.Context
import com.company.app.BukovelApp
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: BukovelApp) {

    @Provides
    fun getContext(): Context = application.applicationContext
}

@Module(includes = [
    WebServiceModule::class,
    ViewModelModule::class,
])
class CoreModule