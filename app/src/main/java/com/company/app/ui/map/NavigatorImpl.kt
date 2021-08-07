package com.company.app.ui.map

import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.Graph
import com.company.app.pathfinder.ShortestPathFinder
import timber.log.Timber

class NavigatorImpl: Navigator {

    override suspend fun getPath(
        graph: Graph,
        start: Int,
        destination: Int
    ): Set<Edge> {
        val pathFinder = ShortestPathFinder(graph, start, destination)
        val path = pathFinder.getShortestPath()
        Timber.tag(TAG_PATH_FINDER).d(path.toString())

        return getPairsSet(path)
    }

    private fun getPairsSet(path: List<Edge>): Set<Edge> {
        val resultPairs = mutableSetOf<Edge>()
        path.forEach { edge -> resultPairs.add(edge) }
        return resultPairs
    }

    companion object {
        private const val TAG_PATH_FINDER = "TAG_PATH_FINDER"
    }
}