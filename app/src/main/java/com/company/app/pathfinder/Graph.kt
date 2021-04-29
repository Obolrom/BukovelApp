package com.company.app.pathfinder

import com.company.app.ui.map.Slope
import java.util.*
import kotlin.collections.ArrayList

class Graph(vertices: Int) {
    private var adjacencyList: MutableList<MutableList<Edge>> = ArrayList(vertices)
    val vertexAmount: Int
        get() = adjacencyList.size

    init {
        for (i in 0 until vertices)
            adjacencyList.add(LinkedList())
    }

    // TODO: 29.04.21 is it ok remove by name?
    fun removeSlope(name: String) {
        val forDeletion = mutableListOf<Edge>()
        for (slope in adjacencyList) {
            for (edge in slope) {
                if (edge.pieceOf == name) {
                    forDeletion.add(edge)
                }
            }
            slope.removeAll(forDeletion)
            forDeletion.clear()
        }
    }

    fun getAdjacentEdges(vertex: Edge): List<Edge> {
        return adjacencyList[vertex.destination]
    }

    fun addEdge(edge: Edge) {
        val node = adjacencyList[edge.start]
        node.add(edge)
    }
}