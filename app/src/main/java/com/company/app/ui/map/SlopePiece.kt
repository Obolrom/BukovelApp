package com.company.app.ui.map

import com.google.android.libraries.maps.model.LatLng

data class SlopePiece(
        val start: Int,
        val destination: Int,
        val name: String,
        val complexity: Complexity,
        val coordinates: List<LatLng>
) {
    var active: Boolean = true
}