package com.company.app.repository

import com.company.app.App
import com.company.app.retrofit.BukovelService
import com.company.app.retrofit.RetrofitServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class Repository(private val app: App) {
    private val applicationScope: CoroutineScope = CoroutineScope(SupervisorJob())
    private val bukovelService: BukovelService by lazy { RetrofitServices.bukovelService }

}