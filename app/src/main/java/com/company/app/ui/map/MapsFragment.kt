package com.company.app.ui.map

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.company.app.R
import com.company.app.appComponent
import com.company.app.databinding.FragmentMapsBinding
import com.company.app.pathfinder.Edge
import com.company.app.ui.AbsFragment
import com.company.app.ui.map.Complexity.*
import com.google.android.libraries.maps.*
import com.google.android.libraries.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.coroutines.*
import timber.log.Timber

class MapsFragment : AbsFragment(R.layout.fragment_maps), OnMapReadyCallback {

    private val mapViewModel: MapViewModel by viewModels { appViewModelFactory }

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheet: BottomSheetBehavior<LinearLayoutCompat>
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)

        with(binding.root) {
            bottomSheet = BottomSheetBehavior
                .from(findViewById(R.id.bottom_sheet))
            bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
            startPicker = findViewById(R.id.start_picker)
            destinationPicker = findViewById(R.id.destination_picker)
            directionButton = findViewById(R.id.get_directions)
        }
        with(binding.map) {
            onCreate(savedInstanceState)
            getMapAsync(this@MapsFragment)
        }
        redSwitch = binding.root.findViewById(R.id.red_complexity_level)
        blackSwitch = binding.root.findViewById(R.id.black_complexity_level)

        return _binding!!.root
    }

    override fun initViewModels() { }

    override fun initViews() {
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
        binding.fabMapNavigator.setOnClickListener(fabMenuButtonListener)
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
        mapViewModel.buildRoute(start, destination, ::showPathOnMap)
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
            slopes.value?.forEach { slope ->
                val polyline = googleMap.addPolyline(slope.style)
                if (slope.complexity == complexity)
                    routes.add(polyline)
                else
                    polyline.remove()
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
            slopes.observe(viewLifecycleOwner) { slopes ->
                slopes?.forEach { slope ->
                    val polyline = googleMap.addPolyline(slope.style)
                    if (slope.complexity == RED) {
                        redSlopes.add(polyline)
                    } else if (slope.complexity == BLACK) {
                        blackSlopes.add(polyline)
                    }
                }
            }

            lifts.observe(viewLifecycleOwner) { lift ->
                googleMap.addPolyline(lift.style)
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
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.map.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.map.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.map.onLowMemory()
    }

    companion object {
        private const val fineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
        private const val coarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
        private const val locationPermissionRequest: Int = 2001
        private val bukovelResortCenter: LatLng = LatLng(48.364952, 24.3990655)
    }
}