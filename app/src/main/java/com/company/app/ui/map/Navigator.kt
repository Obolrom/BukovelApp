package com.company.app.ui.map

import android.content.Context
import android.util.Log
import com.company.app.App
import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.Graph
import com.company.app.pathfinder.ShortestPathFinder

class Navigator(private val context: Context) {

    fun getPath(graph: Graph, start: Int, destination: Int): List<Int> {
        val pathFinder = ShortestPathFinder(graph, start, destination)
        return pathFinder.getShortestPath()
    }
}