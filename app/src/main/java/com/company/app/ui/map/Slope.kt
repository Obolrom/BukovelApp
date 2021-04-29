package com.company.app.ui.map

import com.google.android.libraries.maps.model.*


class Slope(
        val name: String,
        val complexity: Complexity,
        val coordinates: List<LatLng>) {
    var active: Boolean = true
}

