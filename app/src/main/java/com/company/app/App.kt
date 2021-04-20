package com.company.app

import android.app.Application
import com.company.app.repository.Repository

class App: Application() {
    val repository by lazy { Repository(this) }

    override fun onCreate() {
        super.onCreate()
    }
}