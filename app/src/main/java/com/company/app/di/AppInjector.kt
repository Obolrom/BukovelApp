package com.company.app.di

import com.company.app.App

object AppInjector {

    lateinit var appComponent: AppComponent

    fun init(app: App) {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .webServiceModule(WebServiceModule)
            .build()

        appComponent.inject(app)
    }
}