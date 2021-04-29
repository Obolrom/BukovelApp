package com.company.app.pathfinder

class Edge(
        val start: Int,
        val destination: Int,
        val weight: Int,
        val pieceOf: String
) {
    constructor(destination: Int, weight: Int, pieceOf: String)
            : this(-1, destination, weight, pieceOf)
}