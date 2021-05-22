package com.company.app.pathfinder

import java.util.*
import kotlin.collections.ArrayList

class ShortestPaths(vertices: Int) {
    private val paths: MutableList<Path> = ArrayList(vertices)

    init {
        for (i in 0 until vertices)
            paths.add(Path())
    }

    fun getShortestPath(destination: Int): List<Edge> {
        val path: MutableList<Edge> = LinkedList()
        var currentNode = destination
        while (paths[currentNode].parent != ROOT) {
            path.add(paths[currentNode].edge!!)
            currentNode = paths[currentNode].parent
        }
        return path
    }

    fun updateShortestDistance(edge: Edge, distance: Int) {
        paths[edge.destination].distance = distance
        paths[edge.destination].edge = edge
    }

    fun updateParentByShortestPath(edge: Edge, parent: Int) {
        paths[edge.destination].parent = parent
        paths[edge.destination].edge = edge
    }

    fun getDistance(edge: Edge): Int {
        return paths[edge.destination].distance
    }

    class Path {
        var edge: Edge? = null
        var distance: Int = Int.MAX_VALUE
        var parent: Int = ROOT
        override fun toString(): String {
            return "$edge"
        }
    }

    companion object {
        const val ROOT = -1
    }
}