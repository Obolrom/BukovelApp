package com.company.app.ui.map

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.company.app.App
import com.company.app.R
import com.company.app.pathfinder.Edge
import com.company.app.pathfinder.Graph
import com.company.app.ui.map.Complexity.*
import com.google.android.libraries.maps.*
import com.google.android.libraries.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.coroutines.*
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask
import kotlin.concurrent.thread


class MapsFragment : Fragment(), OnMapReadyCallback {

    private val fineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private val locationPermissionRequest: Int = 2001
    private val bukovelResortCenter: LatLng = LatLng(48.364952, 24.3990655)
    private val mapViewModel: MapViewModel by viewModels {
        MapViewModelFactory((activity?.application as App).repository)
    }
    private lateinit var mapView: MapView
    private val navigator: Navigator by lazy { Navigator(requireContext()) }
    private lateinit var bottomSheet: BottomSheetBehavior<LinearLayoutCompat>
    private lateinit var fabMenu: FloatingActionButton
    private lateinit var redSwitch: SwitchCompat
    private lateinit var blackSwitch: SwitchCompat
    private lateinit var startPicker: NumberPicker
    private lateinit var destinationPicker: NumberPicker
    private lateinit var directionButton: AppCompatButton
    private lateinit var googleMap: GoogleMap
    private lateinit var pickerValues: Array<String>
    private var startPickerMarker: Marker? = null
    private var destinationPickerMarker: Marker? = null
    private val route: MutableSet<Polyline> = mutableSetOf()
    private var isRoutePinned: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_maps, container, false)

        with(root) {
            bottomSheet = BottomSheetBehavior
                .from(findViewById(R.id.bottom_sheet))
            bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
            fabMenu = findViewById(R.id.fab_map_navigator)
            mapView = findViewById(R.id.map)
            startPicker = findViewById(R.id.start_picker)
            destinationPicker = findViewById(R.id.destination_picker)
            directionButton = findViewById(R.id.get_directions)
        }
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

        pickerValues = initPickerValues(mapViewModel.vertices)
        with(startPicker) {
            initPicker(this)
            setOnValueChangedListener { _, _, newVal ->
                startPickerSet(newVal)
            }
        }
        with(destinationPicker) {
            initPicker(this)
            setOnValueChangedListener { _, _, newVal ->
                destinationPickerSet(newVal)
            }
        }
        fabMenu.setOnClickListener(fabMenuButtonListener)
        redSwitch.setOnClickListener(redSwitchListener)
        blackSwitch.setOnClickListener(blackSwitchListener)
        directionButton.setOnClickListener(directionButtonListener)
        bottomSheet.addBottomSheetCallback(bottomSheetCallback)

        getLocationPermission()
    }

    private fun startPickerSet(newVal: Int) {
        startPickerMarker = startPickerMarker.run {
            this?.remove()
            buildRoute(newVal, destinationPicker.value)
            googleMap.addMarker(MarkerOptions()
                .position(mapViewModel.vertices[newVal].coordinate)
                .title(mapViewModel.vertices[newVal].title))
        }
    }

    private fun destinationPickerSet(newVal: Int) {
        destinationPickerMarker = destinationPickerMarker.run {
            this?.remove()
            buildRoute(startPicker.value, newVal)
            googleMap.addMarker(MarkerOptions()
                .position(mapViewModel.vertices[newVal].coordinate)
                .title(mapViewModel.vertices[newVal].title))
        }
    }

    private fun buildRoute(start: Int, destination: Int) {
        mapViewModel.coroutineScope.launch(Dispatchers.Main) {
            val path = navigator.getPath(
                Graph(mapViewModel.edgeRepresentationList),
                mapViewModel.vertices[start].vertex,
                mapViewModel.vertices[destination].vertex)
            Log.d("slopes", path.toString())
            showPathOnMap(path)
        }
    }

    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(view: View, newState: Int) {
            when(newState) {
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    startPickerSet(startPicker.value)
                    destinationPickerSet(destinationPicker.value)
                }
                BottomSheetBehavior.STATE_HIDDEN -> {
                    if ( ! isRoutePinned) hideRoutes()
                    startPickerMarker?.remove()
                    destinationPickerMarker?.remove()
                }
                else -> { }
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) { }
    }

    private val directionButtonListener = View.OnClickListener {
        isRoutePinned = !isRoutePinned
        if (isRoutePinned) {
            directionButton.text = resources.getString(R.string.unpin_route)
        } else {
            directionButton.text = resources.getString(R.string.pin_route)
            hideRoutes()
        }
    }

    private fun hideRoutes() {
        route.forEach { it.remove() }
    }

    private fun showPathOnMap(path: Set<Edge>) {
        if (isRoutePinned) return
        hideRoutes()
        mapViewModel.edgeRepresentationList.forEach { edge ->
            val edgePointsPair = Edge(edge)
            if (path.contains(edgePointsPair)) {
                route.add(googleMap.addPolyline(PolylineOptions()
                    .width(7.0f)
                    .visible(true)
                    .addAll(edge.coordinates)
                    .color(Color.parseColor("#7CFC00"))
                    .zIndex(1.0f)))
            }
        }
    }

    private val blackSwitchListener = View.OnClickListener {
        if ( ! redSwitch.isChecked) {
            setVisibility(RED, mapViewModel.redSlopes)
            redSwitch.isChecked = true
        }
        setVisibility(BLACK, mapViewModel.blackSlopes)
    }

    private val fabMenuButtonListener = View.OnClickListener {
        bottomSheet.state = if (bottomSheet.state == BottomSheetBehavior.STATE_COLLAPSED)
            BottomSheetBehavior.STATE_HIDDEN
        else
            BottomSheetBehavior.STATE_COLLAPSED
    }

    private val redSwitchListener = View.OnClickListener {
        setVisibility(RED, mapViewModel.redSlopes)
        if ( ! redSwitch.isChecked) {
            hideRoutes(mapViewModel.blackSlopes)
            blackSwitch.isChecked = false
        }
    }

    private fun initPickerValues(vertices: Array<Vertex>): Array<String> {
        val names = arrayListOf<String>()
        vertices.forEach { vertex -> names.add(vertex.title) }
        return names.toTypedArray()
    }

    private fun initPicker(picker: NumberPicker) {
        with(picker) {
            minValue = 0
            maxValue = pickerValues.size - 1
            wrapSelectorWheel = true
            displayedValues = pickerValues
        }
    }

    private fun hideRoutes(routes: MutableList<Polyline>) {
        routes.forEach { polyline -> polyline.remove() }
        routes.clear()
    }

    private fun setVisibility(complexity: Complexity, routes: MutableList<Polyline>) {
        if (routes.isNotEmpty()) {
            hideRoutes(routes)
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

        with(mapViewModel) {
            slopes.observe(viewLifecycleOwner, { slopes ->
                coroutineScope.launch(Dispatchers.Main) {
                    slopes.forEach { slope ->
                        val polyline = googleMap.addPolyline(slope.style)
                        if (slope.complexity == RED) {
                            redSlopes.add(polyline)
                        } else if (slope.complexity == BLACK) {
                            blackSlopes.add(polyline)
                        }
                    }
                }
            })

            lifts.observe(viewLifecycleOwner, { lifts ->
                coroutineScope.launch(Dispatchers.Main) {
                    lifts.forEach { lift -> googleMap.addPolyline(lift.style) }
                }
            })
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