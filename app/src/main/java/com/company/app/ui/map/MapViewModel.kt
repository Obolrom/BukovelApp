package com.company.app.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.Graph
import com.company.app.repository.Repository
import com.google.android.libraries.maps.model.Polyline
import kotlinx.coroutines.*

class MapViewModel(private val repository: Repository): ViewModel() {

    private val navigator: Navigator by lazy { NavigatorImpl() }

    private var navigatorJob: Job? = null

    val slopes = repository.slopes
    val lifts = repository.lifts
    val edgeRepresentationList = repository.edgeRepresentations
    val coroutineScope = repository.coroutineScope
    val vertices: Array<Vertex> = repository.vertices
    val redSlopes: MutableList<Polyline> = mutableListOf()
    val blackSlopes: MutableList<Polyline> = mutableListOf()

    fun buildRoute(start: Int, destination: Int, showOnMap: (Set<Edge>) -> Unit) {
        navigatorJob?.cancel()
        navigatorJob = viewModelScope.launch(Dispatchers.IO) {
            val path = navigator.getPath(
                Graph(edgeRepresentationList),
                vertices[start].vertex,
                vertices[destination].vertex
            )
            yield()
            withContext(Dispatchers.Main) { showOnMap.invoke(path) }
        }
    }
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