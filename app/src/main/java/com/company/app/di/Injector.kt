package com.company.app.di

import com.company.app.ui.AbsFragment
import com.company.app.ui.map.MapsFragment

interface Injector {
    fun inject(target: MapsFragment)
    fun inject(target: AbsFragment)
}