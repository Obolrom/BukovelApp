package com.company.app.di

import com.company.app.App
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: App) {

    @Provides
    fun getContext() = application.applicationContext
}

@Module(includes = [
    WebServiceModule::class,
    ViewModelModule::class,
])
class CoreModule