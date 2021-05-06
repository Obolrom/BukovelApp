package com.company.app.pathfinder

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Graph(vertices: Int) {
    private var adjacencyList: MutableMap<Int, MutableList<Edge>> =
            Hashtable(vertices * 2)
    val vertexAmount: Int
        get() = adjacencyList.size

    init {
        for (i in 0 until vertices)
            adjacencyList[i] = LinkedList()
    }

    fun removeSlope(name: String) {
        val forDeletion = mutableListOf<Edge>()
        for (slope in adjacencyList) {
            for (edge in slope.value) {
                if (edge.pieceOf == name)
                    forDeletion.add(edge)
            }
            slope.value.removeAll(forDeletion)
            forDeletion.clear()
        }
    }

    fun getAdjacentEdges(vertex: Edge): List<Edge> {
        return adjacencyList[vertex.destination] ?: listOf()
    }

    fun addEdge(edge: Edge) {
        val node = adjacencyList[edge.start]
        node?.add(edge)
    }
}