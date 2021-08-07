package com.company.app

import com.company.app.pathfinder.Graph
import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.ShortestPathFinder
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.junit.Test

class GraphTest {
    lateinit var graph: Graph
    private val coroutineScope = CoroutineScope(SupervisorJob())

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
        coroutineScope.launch {
            val pathFinder = ShortestPathFinder(graph, 6, 8)
            val path = pathFinder.getShortestPath()
            println(path)
            assert(path == (listOf(6, 3, 1, 0, 8)))
        }
    }

    @Test
    fun graphSP2() {
        initGraph1()
        coroutineScope.launch {
            graph.removeSlope("1b")
            val pathFinder = ShortestPathFinder(graph, 9, 8)
            val path = pathFinder.getShortestPath()
            println(path)
            assert(path == (listOf(9, 6, 3, 1, 0, 8)))
        }
    }

    @Test
    fun graphSP3() {
        initGraph1()
        coroutineScope.launch {
            graph.removeSlope("1b")
            graph.removeSlope("1d")
            val pathFinder = ShortestPathFinder(graph, 9, 8)
            val path = pathFinder.getShortestPath()
            println(path)
            assert(path == listOf(9, 6, 5, 2, 1, 0, 8))
        }
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
        coroutineScope.launch {
            val pathFinder = ShortestPathFinder(graph, 0, 2)
            val path = pathFinder.getShortestPath()
            println(path)
            assert(path == listOf(0, 1, 2))
        }
    }

    @Test
    fun graphSP5() {
        initGraph2()
        coroutineScope.launch {
            graph.removeSlope("a")
            val pathFinder = ShortestPathFinder(graph, 0, 2)
            val path = pathFinder.getShortestPath()
            println(path)
            assert(path == listOf(0, 3, 2))
        }
    }

    @Test
    fun graphSP6() {
        initGraph2()
        coroutineScope.launch {
            graph.removeSlope("a")
            graph.removeSlope("c")
            val pathFinder = ShortestPathFinder(graph, 0, 2)
            val path = pathFinder.getShortestPath()
            println(path)
            assert(path == listOf(0, 2))
        }
    }

    private fun initRealGraph() {
        graph = Graph(130)
        graph.addEdge(Edge(56, 55, 68, ""))
        graph.addEdge(Edge(48, 36, 1147, ""))
        graph.addEdge(Edge(42, 46, 457, ""))
        graph.addEdge(Edge(43, 45, 328, ""))
        graph.addEdge(Edge(36, 42, 147, ""))
        graph.addEdge(Edge(42, 43, 117, ""))
        graph.addEdge(Edge(43, 44, 511, ""))
        graph.addEdge(Edge(44, 47, 566, ""))
        graph.addEdge(Edge(44, 47, 480, ""))
        graph.addEdge(Edge(36, 45, 1315, ""))
        graph.addEdge(Edge(45, 46, 66, ""))
        graph.addEdge(Edge(46, 47, 390, ""))
        graph.addEdge(Edge(47, 48, 303, ""))
        graph.addEdge(Edge(31, 33, 1009, ""))
        graph.addEdge(Edge(33, 34, 228, ""))
        graph.addEdge(Edge(34, 35, 336, ""))
        graph.addEdge(Edge(35, 31, 883, ""))
        graph.addEdge(Edge(32, 31, 229, ""))
        graph.addEdge(Edge(34, 32, 632, ""))
        graph.addEdge(Edge(35, 32, 410, ""))
        graph.addEdge(Edge(74, 80, 1093, ""))
        graph.addEdge(Edge(75, 74, 43, ""))
        graph.addEdge(Edge(76, 75, 130, ""))
        graph.addEdge(Edge(77, 76, 137, ""))
        graph.addEdge(Edge(78, 77, 316, ""))
        graph.addEdge(Edge(80, 78, 674, ""))
        graph.addEdge(Edge(81, 77, 633, ""))
        graph.addEdge(Edge(82, 76, 526, ""))
        graph.addEdge(Edge(83, 75, 560, ""))
        graph.addEdge(Edge(19, 73, 330, ""))
        graph.addEdge(Edge(73, 74, 260, ""))
        graph.addEdge(Edge(69, 65, 1965, ""))
        graph.addEdge(Edge(63, 67, 661, ""))
        graph.addEdge(Edge(65, 63, 235, ""))
        graph.addEdge(Edge(67, 68, 450, ""))
        graph.addEdge(Edge(68, 69, 863, ""))
        graph.addEdge(Edge(50, 64, 1145, ""))
        graph.addEdge(Edge(61, 60, 290, ""))
        graph.addEdge(Edge(39, 36, 797, ""))
        graph.addEdge(Edge(36, 37, 122, ""))
        graph.addEdge(Edge(37, 38, 128, ""))
        graph.addEdge(Edge(38, 39, 1380, ""))
        graph.addEdge(Edge(38, 39, 846, ""))
        graph.addEdge(Edge(36, 41, 844, ""))
        graph.addEdge(Edge(36, 40, 860, ""))
        graph.addEdge(Edge(30, 27, 619, ""))
        graph.addEdge(Edge(27, 31, 718, ""))
        graph.addEdge(Edge(5, 6, 157, ""))
        graph.addEdge(Edge(6, 7, 335, ""))
        graph.addEdge(Edge(8, 9, 240, ""))
        graph.addEdge(Edge(9, 5, 652, ""))
        graph.addEdge(Edge(9, 6, 313, ""))
        graph.addEdge(Edge(1, 0, 125, ""))
        graph.addEdge(Edge(2, 1, 147, ""))
        graph.addEdge(Edge(3, 2, 90, ""))
        graph.addEdge(Edge(4, 3, 505, ""))
        graph.addEdge(Edge(8, 4, 1138, ""))
        graph.addEdge(Edge(5, 2, 266, ""))
        graph.addEdge(Edge(10, 7, 1917, ""))
        graph.addEdge(Edge(7, 1, 58, ""))
        graph.addEdge(Edge(0, 8, 700, ""))
        graph.addEdge(Edge(17, 11, 965, ""))
        graph.addEdge(Edge(79, 65, 597, ""))
        graph.addEdge(Edge(65, 79, 742, ""))
        graph.addEdge(Edge(65, 79, 629, ""))
        graph.addEdge(Edge(12, 15, 711, ""))
        graph.addEdge(Edge(17, 10, 1095, ""))
        graph.addEdge(Edge(72, 80, 959, ""))
        graph.addEdge(Edge(19, 72, 263, ""))
        graph.addEdge(Edge(80, 81, 339, ""))
        graph.addEdge(Edge(81, 82, 220, ""))
        graph.addEdge(Edge(82, 83, 76, ""))
        graph.addEdge(Edge(83, 19, 110, ""))
        graph.addEdge(Edge(20, 27, 1519, ""))
        graph.addEdge(Edge(21, 20, 188, ""))
        graph.addEdge(Edge(22, 21, 41, ""))
        graph.addEdge(Edge(27, 28, 477, ""))
        graph.addEdge(Edge(28, 29, 145, ""))
        graph.addEdge(Edge(29, 56, 608, ""))
        graph.addEdge(Edge(56, 85, 496, ""))
        graph.addEdge(Edge(85, 22, 773, ""))
        graph.addEdge(Edge(85, 20, 834, ""))
        graph.addEdge(Edge(26, 23, 850, ""))
        graph.addEdge(Edge(25, 84, 791, ""))
        graph.addEdge(Edge(24, 84, 578, ""))
        graph.addEdge(Edge(25, 24, 590, ""))
        graph.addEdge(Edge(26, 25, 41, ""))
        graph.addEdge(Edge(27, 26, 758, ""))
        graph.addEdge(Edge(84, 20, 59, ""))
        graph.addEdge(Edge(27, 24, 1251, ""))
        graph.addEdge(Edge(23, 26, 733, ""))
        graph.addEdge(Edge(70, 71, 936, ""))
        graph.addEdge(Edge(71, 70, 946, ""))
        graph.addEdge(Edge(71, 69, 927, ""))
        graph.addEdge(Edge(49, 59, 851, ""))
        graph.addEdge(Edge(58, 51, 795, ""))
        graph.addEdge(Edge(57, 53, 282, ""))
        graph.addEdge(Edge(54, 52, 561, ""))
        graph.addEdge(Edge(57, 54, 216, ""))
        graph.addEdge(Edge(51, 48, 95, ""))
        graph.addEdge(Edge(52, 51, 121, ""))
        graph.addEdge(Edge(53, 52, 424, ""))
        graph.addEdge(Edge(54, 53, 154, ""))
        graph.addEdge(Edge(55, 54, 299, ""))
        graph.addEdge(Edge(57, 55, 117, ""))
        graph.addEdge(Edge(58, 57, 27, ""))
        graph.addEdge(Edge(59, 58, 27, ""))
        graph.addEdge(Edge(18, 16, 316, ""))
        graph.addEdge(Edge(4, 3, 504, ""))
        graph.addEdge(Edge(21, 17, 261, ""))
        graph.addEdge(Edge(22, 69, 349, ""))
        graph.addEdge(Edge(59, 85, 368, ""))
        graph.addEdge(Edge(71, 73, 346, ""))
        graph.addEdge(Edge(68, 74, 507, ""))
        graph.addEdge(Edge(10, 8, 116, ""))
        graph.addEdge(Edge(8, 10, 116, ""))
        graph.addEdge(Edge(70, 69, 346, ""))
        graph.addEdge(Edge(69, 70, 346, ""))
        graph.addEdge(Edge(10, 11, 177, ""))
        graph.addEdge(Edge(11, 10, 177, ""))
        graph.addEdge(Edge(20, 23, 141, ""))
        graph.addEdge(Edge(23, 20, 141, ""))
        graph.addEdge(Edge(30, 31, 37, ""))
        graph.addEdge(Edge(31, 30, 37, ""))
        graph.addEdge(Edge(48, 49, 98, ""))
        graph.addEdge(Edge(49, 48, 98, ""))
        graph.addEdge(Edge(50, 49, 106, ""))
        graph.addEdge(Edge(49, 50, 106, ""))
        graph.addEdge(Edge(65, 64, 46, ""))
        graph.addEdge(Edge(64, 65, 46, ""))
        graph.addEdge(Edge(72, 71, 89, ""))
        graph.addEdge(Edge(71, 72, 89, ""))
        graph.addEdge(Edge(70, 17, 120, ""))
        graph.addEdge(Edge(17, 70, 120, ""))
        graph.addEdge(Edge(78, 79, 324, ""))
    }

    @Test
    fun from33To0() = runBlocking {
        initRealGraph()
        val pathFinder = ShortestPathFinder(graph, 33, 48)
        val path = pathFinder.getShortestPath()
        println(path)
    }

    @Test
    fun from32To50() = runBlocking {
        initRealGraph()
        val pathFinder = ShortestPathFinder(graph, 32, 50)
        val path = pathFinder.getShortestPath()
        println(path)
        assert(path == listOf(32, 50))
    }

    @Test
    fun from50To32() = runBlocking {
        initRealGraph()
        val pathFinder = ShortestPathFinder(graph, 50, 32)
        val path = pathFinder.getShortestPath()
        println(path)
        assert(path == listOf(32, 50))
    }
}