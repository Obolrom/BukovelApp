package com.company.app.di

import com.company.app.ui.map.MapsFragment

interface Injector {
    fun inject(target: MapsFragment)
}