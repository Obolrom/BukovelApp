package com.company.app.ui.map

import androidx.lifecycle.*
import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.Graph
import com.company.app.repository.Repository
import com.google.android.libraries.maps.model.Polyline
import kotlinx.coroutines.*
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val repository: Repository,
): ViewModel() {

    private val navigator: Navigator by lazy { NavigatorImpl() }

    private var navigatorJob: Job? = null

    val slopes: LiveData<List<Slope>> = MutableLiveData<List<Slope>>().apply {
        value = repository._slopes
    }
    val lifts = repository.lifts
    val edgeRepresentationList = repository.edgeRepresentations
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