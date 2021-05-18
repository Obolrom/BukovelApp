package com.company.app.pathfinder

import com.company.app.ui.map.EdgeRepresentation

data class Edge(
        val start: Int,
        val destination: Int,
        val weight: Int,
        val pieceOf: String
) {
    constructor(destination: Int, weight: Int, pieceOf: String)
            : this(-1, destination, weight, pieceOf)

    constructor(destination: Int, weight: Int)
            : this(destination, weight, "")

    constructor(edgeRepresentation: EdgeRepresentation)
            : this(edgeRepresentation.start,
                    edgeRepresentation.destination,
                    edgeRepresentation.distance,
                    edgeRepresentation.name)
}