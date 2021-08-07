package com.company.app.pathfinder

import kotlinx.coroutines.yield
import timber.log.Timber
import java.util.*
import kotlin.NoSuchElementException

class ShortestPathFinder(
    private val graph: Graph,
    private val source: Int,
    private val destination: Int
) {
    private val priorityQueue: PriorityQueue<Edge> = PriorityQueue(graph.edgeAmount) { vertex1, vertex2 ->
        vertex1.weight.compareTo(vertex2.weight)
    }
    private val visitedVertices: MutableSet<Int> = hashSetOf()
    private val shortestPaths: ShortestPaths = ShortestPaths(graph.edgeAmount)

    suspend fun getShortestPath(): List<Edge> {
        dijkstra()
        return shortestPaths.getShortestPath(destination).asReversed()
    }

    suspend fun dijkstra() {
        priorityQueue.add(Edge(source, 0))
        shortestPaths.updateShortestDistance(Edge(source, 0),0)

        try {
            while (visitedVertices.size != graph.edgeAmount) {
                val currentEdge = priorityQueue.remove()
                visitedVertices.add(currentEdge.destination)
                if (visitedVertices.contains(destination)) {
                    throw ShortestPathFoundException()
                }
                visitNeighbors(currentEdge)
                yield()
            }
        } catch (nse: NoSuchElementException) {
            Timber.tag(TAG_PATH_FINDER).e("No route found")
        } catch (spf: ShortestPathFoundException) { }

        Timber.d("DIJKSTRA FOUND PATH")
    }

    private fun visitNeighbors(parentEdge: Edge) {
        var edgeDistance: Int
        var fullDistance: Int

        val adjacencyList = graph.getAdjacentEdges(parentEdge)
        for (edge in adjacencyList) {
            if ( ! visitedVertices.contains(edge.destination)) {
                edgeDistance = edge.weight
                fullDistance = shortestPaths.getDistance(parentEdge) + edgeDistance

                if (fullDistance < shortestPaths.getDistance(edge)) {
                    shortestPaths.updateShortestDistance(edge, fullDistance)
                    shortestPaths.updateParentByShortestPath(edge, parentEdge.destination)
                }

                priorityQueue.add(Edge(edge.destination, shortestPaths.getDistance(edge)))
            }
        }
    }

    companion object {
        private const val TAG_PATH_FINDER = "TAG_PATH_FINDER"
    }
}