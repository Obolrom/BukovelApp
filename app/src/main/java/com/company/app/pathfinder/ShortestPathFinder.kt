package com.company.app.pathfinder

import java.util.*
import kotlin.NoSuchElementException

class ShortestPathFinder(
        private val graph: Graph,
        val source: Int,
        val destination: Int) {
    private val priorityQueue: PriorityQueue<Edge> = PriorityQueue(graph.vertexAmount) { vertex1, vertex2 ->
        vertex1.weight.compareTo(vertex2.weight)
    }
    private val visited: MutableSet<Int> = hashSetOf()
    private val shortestPaths: ShortestPaths = ShortestPaths(graph.vertexAmount)

    fun getShortestPath(): List<Int> {
        dijkstra()
        return shortestPaths.getShortestPath(destination).asReversed()
    }

    private fun dijkstra() {
        priorityQueue.add(Edge(source, 0, ""))
        shortestPaths.updateDistance(source, 0)

        try {
            while (visited.size != graph.vertexAmount) {
                val currentVertex = priorityQueue.remove()
                visited.add(currentVertex.destination)
                if (visited.contains(destination)) {
                    throw ShortestPathFoundException()
                }
                visitNeighbors(currentVertex)
            }
        } catch (nse: NoSuchElementException) {

        } catch (spf: ShortestPathFoundException) {

        }
    }

    private fun visitNeighbors(parentVertex: Edge) {
        var edgeDistance: Int
        var fullDistance: Int

        val adjacencyList = graph.getAdjacentEdges(parentVertex)
        for (node in adjacencyList) {
            if ( ! visited.contains(node.destination)) {
                edgeDistance = node.weight
                fullDistance = shortestPaths.getDistance(parentVertex) + edgeDistance

                if (fullDistance < shortestPaths.getDistance(node)) {
                    shortestPaths.updateDistance(node.destination, fullDistance)
                    shortestPaths.updateParent(node.destination, parentVertex.destination)
                }

                priorityQueue.add(Edge(node.destination, shortestPaths.getDistance(node), ""))
            }
        }
    }
}