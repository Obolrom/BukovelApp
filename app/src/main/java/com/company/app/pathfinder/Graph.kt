package com.company.app.pathfinder

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

    fun getAdjacentEdges(vertex: Edge): List<Edge> {
        return adjacencyList[vertex.destination]
    }

    fun addEdge(edge: Edge) {
        val node = adjacencyList[edge.start]
        node.add(edge)
    }
}