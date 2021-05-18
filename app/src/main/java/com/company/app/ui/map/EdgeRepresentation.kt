package com.company.app.ui.map

import com.company.app.R
import com.google.android.libraries.maps.model.*

data class EdgeRepresentation(
        val name: String,
        val start: Int,
        val destination: Int,
        val complexity: Complexity,
        val distance: Int,
        val coordinates: List<LatLng>
) {
    var active: Boolean = true
    var style: PolylineOptions = PolylineOptions()
            .width(8.0f)
            .visible(true)
            .color(R.color.purple_200)
            .addAll(coordinates)
}