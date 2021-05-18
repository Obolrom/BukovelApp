package com.company.app.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.company.app.repository.Repository
import com.google.android.libraries.maps.model.Polyline

class MapViewModel(private val repository: Repository): ViewModel() {
    val slopes = repository.slopes
    val lifts = repository.lifts
    val edgeRepresentationList = repository.edgeRepresentations
    val coroutineScope = repository.coroutineScope
    val vertices: Array<Vertex> = repository.vertices
    val redSlopes: MutableList<Polyline> = mutableListOf()
    val blackSlopes: MutableList<Polyline> = mutableListOf()
}

class MapViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}