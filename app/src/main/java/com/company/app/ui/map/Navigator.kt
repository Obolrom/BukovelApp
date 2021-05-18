package com.company.app.ui.map

import android.content.Context
import android.util.Log
import com.company.app.App
import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.Graph
import com.company.app.pathfinder.ShortestPathFinder

class Navigator(private val context: Context) {

    fun getPath(graph: Graph, start: Int, destination: Int): Set<Set<Int>> {
        val pathFinder = ShortestPathFinder(graph, start, destination)
        val path = pathFinder.getShortestPath()
        return getPairsSet(path)
    }

    private fun getPairsSet(path: List<Int>): Set<Set<Int>> {
        val resultPairs = mutableSetOf<Set<Int>>()
        for (i in 1 until path.size) {
            resultPairs.add(setOf(path[i - 1], path[i]))
        }
        return resultPairs
    }
}