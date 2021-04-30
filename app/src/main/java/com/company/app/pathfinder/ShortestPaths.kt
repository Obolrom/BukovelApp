package com.company.app.pathfinder

import java.util.*
import kotlin.collections.ArrayList

class ShortestPaths(vertices: Int) {
    private val paths: MutableList<Path> = ArrayList(vertices)

    init {
        for (i in 0 until vertices)
            paths.add(Path())
    }

    fun getShortestPath(destination: Int): List<Int> {
        val path: MutableList<Int> = LinkedList()
        var currentNode = destination
        path.add(currentNode)
        while (paths[currentNode].parent != ROOT) {
            path.add(paths[currentNode].parent)
            currentNode = paths[currentNode].parent
        }
        return path
    }

    fun updateShortestDistance(vertex: Int, distance: Int) {
        paths[vertex].distance = distance
    }

    fun updateParentByShortestPath(vertex: Int, parent: Int) {
        paths[vertex].parent = parent
    }

    fun getDistance(edge: Edge): Int {
        return paths[edge.destination].distance
    }

    class Path {
        var distance: Int = Int.MAX_VALUE
        var parent: Int = ROOT
    }

    companion object {
        const val ROOT = -1
    }
}