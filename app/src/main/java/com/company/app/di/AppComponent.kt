package com.company.app.di

import com.company.app.BukovelApp
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    CoreModule::class,
])
interface AppComponent:
        AndroidInjector<BukovelApp>,
        Injector