package com.company.app.ui.map

import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.Graph

interface Navigator {

    suspend fun getPath(
        graph: Graph,
        start: Int,
        destination: Int
    ): Set<Edge>

}