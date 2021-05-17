package com.company.app.ui.map

import android.content.Context
import com.company.app.App
import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.Graph
import com.company.app.pathfinder.ShortestPathFinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive

class Navigator(private val context: Context) {

    fun getPath(graph: Graph, start: Int, destination: Int): List<Int> {
        for (edge in (context.applicationContext as App).edges) {
            graph.addEdge(Edge(edge))
        }
        val pathFinder = ShortestPathFinder(graph, start, destination)
        (context.applicationContext as App).coroutineScope.ensureActive()
        return pathFinder.getShortestPath()
    }
}