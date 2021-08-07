package com.company.app.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.company.app.services.BukovelService
import com.company.app.ui.map.*
import com.company.app.ui.services.Service
import com.company.app.ui.services.ServiceReview
import com.google.android.libraries.maps.model.*
import com.google.gson.Gson
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import java.io.BufferedReader
import java.io.IOException

@Singleton
class MapRepository @Inject constructor(
    private val context: Context,
    private val bukovelService: BukovelService
) {
    private val gsonConverter = Gson()
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob())

    private val _slopes: MutableLiveData<List<Slope>> = MutableLiveData()
    val slopes: LiveData<List<Slope>?> = _slopes

    private val _edges: MutableList<EdgeRepresentation> = mutableListOf()
    val edges: List<EdgeRepresentation> = _edges

    private val _vertices: MutableList<Vertex> = ArrayList()
    val vertices: List<Vertex> = _vertices

    private val assets = context.assets

    init {
        val slopeList = mutableListOf<Slope>()
        _slopes.value = slopeList

        coroutineScope.launch(Dispatchers.IO) {
            getSlopes().collect { slope -> slopeList.add(slope) }
            getEdges().collect { edge -> _edges.add(edge) }
            getVertices().collect { vertex -> _vertices.add(vertex) }
        }
    }

    fun getServices() : MutableLiveData<List<Service>> {
        val services: MutableLiveData<List<Service>> = MutableLiveData<List<Service>>()

        return services
    }

    fun getServiceReviews(serviceName: String) : MutableLiveData<List<ServiceReview>> {
        val reviews: MutableLiveData<List<ServiceReview>> = MutableLiveData<List<ServiceReview>>()

        return reviews
    }

    fun registerUser(nickname: String, password: String) {
        bukovelService.register(nickname, password)
            .enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.d("net", "response ${response.message()}")
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.d("net", "fail")
                }
            })
    }

    private fun calculateDistance(slope: EdgeRepresentation): Double {
        var distance = 0.0
        for (i in 1 until slope.coordinates.size)
            distance += SphericalUtil
                .computeDistanceBetween(slope.coordinates[i - 1], slope.coordinates[i])
        return distance
    }

    private fun getVertices(): Flow<Vertex> = flow {
        val fileNames = assets?.list(verticesDirectory) ?: arrayOf()

        for (filePath in fileNames) {
            try {
                val file = assets.open("${verticesDirectory}/$filePath")
                val content = file.bufferedReader().use(BufferedReader::readText)
                val vertex = gsonConverter.fromJson(content, Vertex::class.java)
                emit(vertex)
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun getSlopes(): Flow<Slope> = flow {
        val fileNames = assets?.list(slopeDirectory) ?: arrayOf()

        for (filePath in fileNames) {
            try {
                val file = assets.open("${slopeDirectory}/$filePath")
                val content = file.bufferedReader().use(BufferedReader::readText)
                val slope = gsonConverter.fromJson(content, Slope::class.java)
                slope.active = true
                slope.style = PolylineOptions()
                    .width(7.0f)
                    .endCap(RoundCap())
                    .visible(true)
                    .addAll(slope.coordinates)
                    .addSpan(getGradientByComplexity(slope.complexity))
                emit(slope)
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }
    }.flowOn(Dispatchers.IO)

    fun getLifts(): Flow<Lift> = flow {
        val fileNames = assets?.list(liftsDirectory) ?: arrayOf()

        for (filePath in fileNames) {
            try {
                val file = assets.open("${liftsDirectory}/$filePath")
                val content = file.bufferedReader().use(BufferedReader::readText)
                val lift = gsonConverter.fromJson(content, Lift::class.java)
                lift.active = true
                lift.style = PolylineOptions()
                    .width(8.0f)
                    .endCap(RoundCap())
                    .visible(true)
                    .pattern(listOf(Gap(10F), Dash(15F)))
//                    .color(R.color.purple_200)
                    .addAll(lift.coordinates)
                emit(lift)
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun getEdges(): Flow<EdgeRepresentation> = flow {
        val fileNames = assets?.list(edgesDirectory) ?: arrayOf()

        for (filePath in fileNames) {
            try {
                val file = assets.open("${edgesDirectory}/$filePath")
                val content = file.bufferedReader().use(BufferedReader::readText)
                val edge = gsonConverter.fromJson(content, EdgeRepresentation::class.java)
                edge.active = true
                edge.style = PolylineOptions()
                    .width(8.0f)
                    .visible(true)
//                    .color(R.color.purple_200)
                    .addAll(edge.coordinates)
                if (filePath.contains("transition"))
                    emit(duplicateTransition(edge))

                emit(edge)
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }
    }.flowOn(Dispatchers.IO)

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
    }
}