package com.company.app.ui.map

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.company.app.App
import com.company.app.R
import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.Graph
import com.company.app.pathfinder.ShortestPathFinder
import com.google.android.libraries.maps.*
import com.google.android.libraries.maps.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsFragment : Fragment(), OnMapReadyCallback {

    private val fineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private val locationPermissionRequest: Int = 2001
    private val bukovelResortCenter: LatLng = LatLng(48.364952, 24.3990655)
    private val mapViewModel: MapViewModel by viewModels {
        MapViewModelFactory((activity?.application as App).repository)
    }
    private lateinit var root: View
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_maps, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        getLocationPermission()
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map ?: return
        with(googleMap) {
            moveCamera(CameraUpdateFactory.newLatLng(bukovelResortCenter))
            setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.style))
        }

        val slopes = mapViewModel.slopes
        val lifts = mapViewModel.lifts
        for (slope in slopes) {
            googleMap.addPolyline(slope.style)
        }

        for (lift in lifts) {
            googleMap.addPolyline(lift.style)
        }

        for (edge in (activity?.application as App).edges) {
            googleMap.addPolyline(PolylineOptions()
                .width(7.0f)
                .endCap(RoundCap())
                .visible(true)
                .addAll(edge.coordinates)
                .color(Color.parseColor("#7CFC00")))
        }

        (activity?.application as App).coroutineScope.launch {
            val graph = Graph(86)
            for (edge in (activity?.application as App).edges) {
                graph.addEdge(Edge(edge))
            }
            val pathFinder = ShortestPathFinder(graph, 80, 69)
            delay(5000)
            val path = pathFinder.getShortestPath()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context?.applicationContext,
                    path.toString(), Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getLocationPermission() {
        val permissions = arrayOf(fineLocation, coarseLocation)

        if (ContextCompat.checkSelfPermission(context?.applicationContext!!, fineLocation)
            == PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context?.applicationContext!!, coarseLocation).let { code ->
                if (code != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(permissions, locationPermissionRequest)
            }
        } else {
            requestPermissions(permissions, locationPermissionRequest)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // FIXME: 18.04.21 make it correct
        when (requestCode) {
            locationPermissionRequest -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "permission granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}