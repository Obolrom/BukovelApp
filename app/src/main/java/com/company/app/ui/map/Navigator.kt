package com.company.app.ui.map

import android.content.Context
import android.util.Log
import com.company.app.App
import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.Graph
import com.company.app.pathfinder.ShortestPathFinder

class Navigator(private val context: Context) {

    fun getPath(graph: Graph, start: Int, destination: Int): Set<Edge> {
        val pathFinder = ShortestPathFinder(graph, start, destination)
        val path = pathFinder.getShortestPath()
        return getPairsSet(path)
    }

    private fun getPairsSet(path: List<Edge>): Set<Edge> {
        val resultPairs = mutableSetOf<Edge>()
        path.forEach { edge -> resultPairs.add(edge) }
        return resultPairs
    }
}