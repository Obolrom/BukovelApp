package com.company.app

import android.app.Application
import android.util.Log
import com.company.app.repository.Repository
import com.company.app.ui.map.*
import com.google.android.libraries.maps.model.Dash
import com.google.android.libraries.maps.model.Gap
import com.google.android.libraries.maps.model.PolylineOptions
import com.google.android.libraries.maps.model.RoundCap
import com.google.gson.Gson
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import kotlin.math.roundToInt

class App: Application() {
    private val gsonConverter = Gson()
    val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob())
    val repository by lazy { Repository(this) }
    lateinit var slopes: List<Slope>
    lateinit var lifts: List<Lift>
    lateinit var edges: List<EdgeRepresentation>
    lateinit var vertices: Array<Vertex>

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        coroutineScope.launch(Dispatchers.IO) {
            val assets = applicationContext?.assets
            slopes = readSlopesDirectory(assets?.list(slopeDirectory) ?: arrayOf())
            lifts = readLiftsDirectory(assets?.list(liftsDirectory) ?: arrayOf())
            edges = readEdgesDirectory(assets?.list(edgesDirectory) ?: arrayOf())
            vertices = readVerticesDirectory(assets?.list(verticesDirectory) ?: arrayOf())
        }
    }

    private fun calculateDistance(slope: EdgeRepresentation): Double {
        var distance = 0.0
        for (i in 1 until slope.coordinates.size)
            distance += SphericalUtil
                    .computeDistanceBetween(slope.coordinates[i - 1], slope.coordinates[i])
        return distance
    }

    private fun readVerticesDirectory(fileNames: Array<String>): Array<Vertex> {
        val directions = arrayListOf<Vertex>()
        for (filePath in fileNames) {
            try {
                val file = assets.open("$verticesDirectory/$filePath")
                val content = file.bufferedReader().use(BufferedReader::readText)
                val vertex = gsonConverter.fromJson(content, Vertex::class.java)
                directions.add(vertex)
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }
        return directions.toTypedArray()
    }

    private fun readSlopesDirectory(fileNames: Array<String>): List<Slope> {
        val directions = mutableListOf<Slope>()
        for (filePath in fileNames) {
            try {
                val file = assets.open("$slopeDirectory/$filePath")
                val content = file.bufferedReader().use(BufferedReader::readText)
                val slope = gsonConverter.fromJson(content, Slope::class.java)
                slope.active = true
                slope.style = PolylineOptions()
                        .width(7.0f)
                        .endCap(RoundCap())
                        .visible(true)
                        .addAll(slope.coordinates)
                        .addSpan(getGradientByComplexity(slope.complexity))
                directions.add(slope)
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }
        return directions
    }

    private fun readLiftsDirectory(fileNames: Array<String>): List<Lift> {
        val directions = mutableListOf<Lift>()
        for (filePath in fileNames) {
            try {
                val file = assets.open("$liftsDirectory/$filePath")
                val content = file.bufferedReader().use(BufferedReader::readText)
                val lift = gsonConverter.fromJson(content, Lift::class.java)
                lift.active = true
                lift.style = PolylineOptions()
                        .width(8.0f)
                        .endCap(RoundCap())
                        .visible(true)
                        .pattern(listOf(Gap(10F), Dash(15F)))
                        .color(R.color.purple_200)
                        .addAll(lift.coordinates)
                directions.add(lift)
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }
        return directions
    }

    private fun readEdgesDirectory(fileNames: Array<String>): List<EdgeRepresentation> {
        val directions = mutableListOf<EdgeRepresentation>()
        for (filePath in fileNames) {
            try {
                val file = assets.open("$edgesDirectory/$filePath")
                val content = file.bufferedReader().use(BufferedReader::readText)
                val edge = gsonConverter.fromJson(content, EdgeRepresentation::class.java)
                edge.active = true
                edge.style = PolylineOptions()
                        .width(8.0f)
                        .visible(true)
                        .color(R.color.purple_200)
                        .addAll(edge.coordinates)
                if (filePath.contains("transition"))
                    directions.add(duplicateTransition(edge))
                directions.add(edge)
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }
        return directions
    }

    private fun duplicateTransition(edge: EdgeRepresentation): EdgeRepresentation {
        return EdgeRepresentation(
            edge.name,
            edge.destination,
            edge.start,
            edge.complexity,
            edge.distance,
            edge.coordinates)
    }

    companion object {
        private const val slopeDirectory = "slopes"
        private const val edgesDirectory = "edges"
        private const val liftsDirectory = "lifts"
        private const val verticesDirectory = "vertices"
        private var INSTANCE: App? = null
        val app: App
            get() = INSTANCE!!
    }
}