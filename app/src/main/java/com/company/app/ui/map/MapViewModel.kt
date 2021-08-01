package com.company.app.ui.map

import androidx.lifecycle.*
import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.Graph
import com.company.app.repository.MapRepository
import com.google.android.libraries.maps.model.Polyline
import kotlinx.coroutines.*
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val mapRepository: MapRepository,
): ViewModel() {

    private val navigator: Navigator by lazy { NavigatorImpl() }

    private var navigatorJob: Job? = null

    val slopes: LiveData<List<Slope>?> = mapRepository.slopes
    val lifts: LiveData<Lift> = mapRepository.getLifts().asLiveData()
    val edgeRepresentationList: List<EdgeRepresentation> = mapRepository.edges
    val vertices: Array<Vertex> = mapRepository.vertices.toTypedArray()
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