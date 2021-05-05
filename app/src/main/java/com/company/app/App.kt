package com.company.app

import android.app.Application
import com.company.app.repository.Repository
import com.company.app.ui.map.*
import com.google.android.libraries.maps.model.Dash
import com.google.android.libraries.maps.model.Gap
import com.google.android.libraries.maps.model.PolylineOptions
import com.google.android.libraries.maps.model.RoundCap
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException

class App: Application() {
    private val slopeDirectory = "slopes"
    private val edgesDirectory = "edges"
    private val liftsDirectory = "lifts"
    private val gsonConverter = Gson()
    val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob())
    val repository by lazy { Repository(this) }
    lateinit var slopes: List<Slope>
    lateinit var lifts: List<Lift>
    lateinit var edges: List<EdgeRepresentation>

    override fun onCreate() {
        super.onCreate()

        coroutineScope.launch(Dispatchers.IO) {
            val assets = applicationContext?.assets
            slopes = readSlopesDirectory(assets?.list(slopeDirectory) ?: arrayOf())
            lifts = readLiftsDirectory(assets?.list(liftsDirectory) ?: arrayOf())
            edges = readEdgesDirectory(assets?.list(edgesDirectory) ?: arrayOf())
        }
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
                        .color(R.color.lift)
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
                        .endCap(RoundCap())
                        .visible(true)
                        .pattern(listOf(Gap(10F), Dash(15F)))
                        .color(R.color.lift)
                        .addAll(edge.coordinates)
                directions.add(edge)
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }
        return directions
    }
}