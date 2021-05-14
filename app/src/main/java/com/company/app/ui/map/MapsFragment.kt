package com.company.app.ui.map

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.company.app.App
import com.company.app.R
import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.Graph
import com.company.app.pathfinder.ShortestPathFinder
import com.company.app.ui.map.Complexity.*
import com.google.android.libraries.maps.*
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MapStyleOptions
import com.google.android.libraries.maps.model.Polyline
import com.google.android.libraries.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MapsFragment : Fragment(), OnMapReadyCallback {

    private val fineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private val locationPermissionRequest: Int = 2001
    private val bukovelResortCenter: LatLng = LatLng(48.364952, 24.3990655)
    private val mapViewModel: MapViewModel by viewModels {
        MapViewModelFactory((activity?.application as App).repository)
    }
    private lateinit var root: View
    private lateinit var mapView: MapView
    private lateinit var bottomSheet: BottomSheetBehavior<LinearLayoutCompat>
    private lateinit var fabMenu: FloatingActionButton
    private lateinit var redSwitch: SwitchCompat
    private lateinit var blackSwitch: SwitchCompat
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_maps, container, false)

        bottomSheet = BottomSheetBehavior
            .from(root.findViewById(R.id.bottom_sheet))
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        fabMenu = root.findViewById(R.id.fab_map_navigator)
        mapView = root.findViewById(R.id.map) as MapView
        with(mapView) {
            onCreate(savedInstanceState)
            getMapAsync(this@MapsFragment)
        }
        redSwitch = root.findViewById(R.id.red_complexity_level)
        blackSwitch = root.findViewById(R.id.black_complexity_level)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        fabMenu.setOnClickListener {
            bottomSheet.state = if (bottomSheet.state == BottomSheetBehavior.STATE_COLLAPSED)
                BottomSheetBehavior.STATE_HIDDEN
            else
                BottomSheetBehavior.STATE_COLLAPSED
        }
        redSwitch.setOnClickListener {
            setVisibility(RED, mapViewModel.redSlopes)
        }
        blackSwitch.setOnClickListener {
            setVisibility(BLACK, mapViewModel.blackSlopes)
        }
        getLocationPermission()
    }

    private fun setVisibility(complexity: Complexity, routes: MutableList<Polyline>) {
        if (routes.isNotEmpty()) {
            routes.forEach { polyline -> polyline.remove() }
            routes.clear()
            return
        }
        with(mapViewModel) {
            coroutineScope.launch(Dispatchers.Main) {
                slopes.value!!.forEach { slope ->
                    val polyline = googleMap.addPolyline(slope.style)
                    if (slope.complexity == complexity)
                        routes.add(polyline)
                    else
                        polyline.remove()
                }
            }
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map ?: return
        with(googleMap) {
            moveCamera(CameraUpdateFactory.newLatLng(bukovelResortCenter))
            setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.style))
        }

        mapViewModel.slopes.observe(viewLifecycleOwner, { slopes ->
            slopes.forEach { slope ->
                val polyline = googleMap.addPolyline(slope.style)
                if (slope.complexity == RED) {
                    mapViewModel.redSlopes.add(polyline)
                } else if (slope.complexity == BLACK) {
                    mapViewModel.blackSlopes.add(polyline)
                }
            }
        })

        mapViewModel.lifts.observe(viewLifecycleOwner, { lifts ->
            lifts.forEach { lift -> googleMap.addPolyline(lift.style) }
        })

//        for (edge in (activity?.application as App).edges) {
//            googleMap.addPolyline(PolylineOptions()
//                .width(7.0f)
//                .visible(true)
//                .addAll(edge.coordinates)
//                .color(Color.parseColor("#7CFC00")))
//        }
//
//        (activity?.application as App).coroutineScope.launch {
//            val graph = Graph(86)
//            for (edge in (activity?.application as App).edges) {
//                graph.addEdge(Edge(edge))
//            }
//            val pathFinder = ShortestPathFinder(graph, 80, 69)
//            delay(5000)
//            val path = pathFinder.getShortestPath()
//        }
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

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}