package com.company.app.di

import androidx.lifecycle.ViewModel
import com.company.app.ui.map.MapViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun provideMapViewModel(viewModel: MapViewModel): ViewModel
}