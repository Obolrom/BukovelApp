package com.company.app

import com.company.app.pathfinder.Graph
import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.ShortestPathFinder
import org.junit.Test

class GraphTest {
    lateinit var graph: Graph

    private fun initGraph1() {
        graph = Graph(10)
        graph.addEdge(Edge(0, 8, 750))
        graph.addEdge(Edge(1, 0, 200))
        graph.addEdge(Edge(2, 1, 150))
        graph.addEdge(Edge(3, 1, 300))
        graph.addEdge(Edge(4, 3, 400))
        graph.addEdge(Edge(5, 2, 220))
        graph.addEdge(Edge(6, 3, 266))
        graph.addEdge(Edge(6, 5, 300))
        graph.addEdge(Edge(7, 4, 504))
        graph.addEdge(Edge(7, 4, 400))
        graph.addEdge(Edge(8, 2, 1950))
        graph.addEdge(Edge(8, 9, 300))
        graph.addEdge(Edge(8, 7, 700))
        graph.addEdge(Edge(9, 5, 313))
        graph.addEdge(Edge(9, 6, 400))
    }

    @Test
    fun graphCreation() {
        initGraph1()
        val pathFinder = ShortestPathFinder(graph, 6, 8)
        val path = pathFinder.getShortestPath()
        println(path)
        assert(path.equals(listOf(6, 3, 1, 0, 8)))
    }
}