package com.company.app.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.company.app.retrofit.BukovelService
import com.company.app.ui.map.*
import com.company.app.ui.services.Service
import com.company.app.ui.services.ServiceReview
import com.google.android.libraries.maps.*
import com.google.android.libraries.maps.model.*
import com.google.gson.Gson
import com.google.maps.android.SphericalUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import java.io.BufferedReader
import java.io.IOException

@Singleton
class Repository @Inject constructor(
    private val context: Context,
    private val bukovelService: BukovelService
) {
    private val gsonConverter = Gson()
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob())
    var _slopes: List<Slope>? = null
    var _lifts: List<Lift>? = null
    var _edges: List<EdgeRepresentation>? = null
    var _vertices: Array<Vertex>? = null
    val assets = context.assets

    init {
        coroutineScope.launch(Dispatchers.IO) {
            _slopes = readSlopesDirectory(assets?.list(slopeDirectory) ?: arrayOf())
            _lifts = readLiftsDirectory(assets?.list(liftsDirectory) ?: arrayOf())
            _edges = readEdgesDirectory(assets?.list(edgesDirectory) ?: arrayOf())
            _vertices = readVerticesDirectory(assets?.list(verticesDirectory) ?: arrayOf())
        }
    }

    private val database: BukovelDatabase by lazy {
        BukovelDatabase.getDatabase(context, coroutineScope)
    }

    val vertices: Array<Vertex> = _vertices ?: arrayOf()
    val edgeRepresentations: List<EdgeRepresentation> = _edges ?: listOf()

    val slopes: LiveData<List<Slope>> = MutableLiveData<List<Slope>>().apply {
        value = _slopes
    }

    val lifts: LiveData<List<Lift>> = MutableLiveData<List<Lift>>().apply {
        value = _lifts
    }

    fun getServices() : MutableLiveData<List<Service>> {
        val services: MutableLiveData<List<Service>> = MutableLiveData<List<Service>>()
        bukovelService.getServices()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<List<Service>>() {
                override fun onSuccess(response: List<Service>) {
                    services.value = response
                }

                override fun onError(e: Throwable) { }
            })
        return services
    }

    fun getServiceReviews(serviceName: String) : MutableLiveData<List<ServiceReview>> {
        val reviews: MutableLiveData<List<ServiceReview>> = MutableLiveData<List<ServiceReview>>()
        bukovelService.getServiceReviews(serviceName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<List<ServiceReview>>() {
                override fun onSuccess(response: List<ServiceReview>) {
                    reviews.value = response
                }

                override fun onError(e: Throwable) { }
            })
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

    private fun readVerticesDirectory(fileNames: Array<String>): Array<Vertex> {
        val directions = arrayListOf<Vertex>()
        for (filePath in fileNames) {
            try {
                val file = assets.open("${verticesDirectory}/$filePath")
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
    }
}