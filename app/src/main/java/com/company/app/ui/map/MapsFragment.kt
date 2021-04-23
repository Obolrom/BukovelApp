package com.company.app.ui.map

import android.content.pm.PackageManager
import android.graphics.Color
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.company.app.App
import com.company.app.R
import com.google.android.libraries.maps.*

import com.google.android.libraries.maps.model.*

class MapsFragment : Fragment(), OnMapReadyCallback {

    private val fineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private val locationPermissionRequest: Int = 2001
    private val bukovelResortCenter: LatLng = LatLng(48.364952, 24.3990655)
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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getLocationPermission()
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map ?: return
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bukovelResortCenter))
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.style))

        val slopes = (activity?.application as App).slopes
        for (slope in slopes) {
            val slopeView = PolylineOptions()
                    .width(7.0f)
                    .endCap(RoundCap())
                    .addAll(slope.coordinates)
                    .visible(true)
                    .pattern(listOf(Gap(10F), Dash(30F)))
            googleMap.addPolyline(slopeView)
        }
    }

    private fun getLocationPermission() {
        val permissions = arrayOf(fineLocation, coarseLocation)

        if (ContextCompat.checkSelfPermission(context?.applicationContext!!, fineLocation)
            == PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context?.applicationContext!!, coarseLocation).let {
                code ->
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