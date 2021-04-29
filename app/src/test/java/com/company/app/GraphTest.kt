package com.company.app

import com.company.app.pathfinder.Graph
import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.ShortestPathFinder
import org.junit.Test

class GraphTest {
    lateinit var graph: Graph

    private fun initGraph1() {
        graph = Graph(10)
        graph.addEdge(Edge(0, 8, 750, "1r"))
        graph.addEdge(Edge(1, 0, 200, "1c"))
        graph.addEdge(Edge(2, 1, 150, "1e"))
        graph.addEdge(Edge(3, 1, 300, "1c"))
        graph.addEdge(Edge(4, 3, 400, "1c"))
        graph.addEdge(Edge(5, 2, 220, "1a"))
        graph.addEdge(Edge(6, 3, 266, "1d"))
        graph.addEdge(Edge(6, 5, 300, "1a"))
        graph.addEdge(Edge(7, 4, 504, "c"))
        graph.addEdge(Edge(7, 4, 400, "1c"))
        graph.addEdge(Edge(8, 2, 1950, "1e"))
        graph.addEdge(Edge(8, 9, 300, "1a"))
        graph.addEdge(Edge(8, 7, 700, "1c"))
        graph.addEdge(Edge(9, 5, 313, "1b"))
        graph.addEdge(Edge(9, 6, 400, "1a"))
    }

    @Test
    fun graphSP1() {
        initGraph1()
        val pathFinder = ShortestPathFinder(graph, 6, 8)
        val path = pathFinder.getShortestPath()
        println(path)
        assert(path.equals(listOf(6, 3, 1, 0, 8)))
    }

    @Test
    fun graphSP2() {
        initGraph1()
        graph.removeSlope("1b")
        val pathFinder = ShortestPathFinder(graph, 9, 8)
        val path = pathFinder.getShortestPath()
        println(path)
        assert(path.equals(listOf(9, 6, 3, 1, 0, 8)))
    }

    @Test
    fun graphSP3() {
        initGraph1()
        graph.removeSlope("1b")
        graph.removeSlope("1d")
        val pathFinder = ShortestPathFinder(graph, 9, 8)
        val path = pathFinder.getShortestPath()
        println(path)
        assert(path.equals(listOf(9, 6, 5, 2, 1, 0, 8)))
    }

    private fun initGraph2() {
        graph = Graph(4)
        graph.addEdge(Edge(0, 1, 20, "a"))
        graph.addEdge(Edge(1, 2, 30, "a"))
        graph.addEdge(Edge(0, 2, 55, "b"))
        graph.addEdge(Edge(0, 3, 15, "c"))
        graph.addEdge(Edge(3, 2, 39, "c"))
    }

    @Test
    fun graphSP4() {
        initGraph2()
        val pathFinder = ShortestPathFinder(graph, 0, 2)
        val path = pathFinder.getShortestPath()
        println(path)
        assert(path == listOf(0, 1, 2))
    }

    @Test
    fun graphSP5() {
        initGraph2()
        graph.removeSlope("a")
        val pathFinder = ShortestPathFinder(graph, 0, 2)
        val path = pathFinder.getShortestPath()
        println(path)
        assert(path == listOf(0, 3, 2))
    }

    @Test
    fun graphSP6() {
        initGraph2()
        graph.removeSlope("a")
        graph.removeSlope("c")
        val pathFinder = ShortestPathFinder(graph, 0, 2)
        val path = pathFinder.getShortestPath()
        println(path)
        assert(path == listOf(0, 2))
    }
}