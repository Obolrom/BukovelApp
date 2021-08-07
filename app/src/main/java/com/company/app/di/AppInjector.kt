package com.company.app.di

import com.company.app.BukovelApp

object AppInjector {

    lateinit var appComponent: AppComponent

    fun init(app: BukovelApp) {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .webServiceModule(WebServiceModule)
            .build()

        appComponent.inject(app)
    }
}