package com.company.app.pathfinder

import com.company.app.ui.map.EdgeRepresentation
import java.util.*

class Graph(vertices: Int) {
    private var adjacencyMap: MutableMap<Int, MutableList<Edge>> =
            Hashtable(vertices * 2)
    val edgeAmount: Int
        get() = adjacencyMap.size

    init {
        for (i in 0 until vertices)
            adjacencyMap[i] = LinkedList()
    }

    constructor(edges: List<EdgeRepresentation>)
        : this(edges.size) {
        edges.forEach { edgeRepresentation ->
            addEdge(Edge(edgeRepresentation))
        }
    }

    fun removeSlope(name: String) {
        val forDeletion = mutableListOf<Edge>()
        for (slope in adjacencyMap) {
            for (edge in slope.value) {
                if (edge.pieceOf == name)
                    forDeletion.add(edge)
            }
            slope.value.removeAll(forDeletion)
            forDeletion.clear()
        }
    }

    fun getAdjacentEdges(vertex: Edge): List<Edge> {
        return adjacencyMap[vertex.destination] ?: listOf()
    }

    fun addEdge(edge: Edge) {
        val node = adjacencyMap[edge.start]
        node?.add(edge)
    }
}