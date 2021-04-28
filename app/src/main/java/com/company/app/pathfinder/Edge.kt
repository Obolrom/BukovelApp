package com.company.app.pathfinder

class Edge(
        val start: Int,
        val destination: Int,
        val weight: Int
) {
    constructor(destination: Int, weight: Int)
            : this(-1, destination, weight)
}