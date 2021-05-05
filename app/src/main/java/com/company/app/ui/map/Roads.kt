package com.company.app.ui.map

import com.company.app.R
import com.google.android.libraries.maps.model.*

abstract class Road(
        val name: String,
        val coordinates: List<LatLng>,
        var active: Boolean,
        var style: PolylineOptions
)

class Slope(
        name: String,
        val complexity: Complexity,
        coordinates: List<LatLng>
) : Road(name, coordinates, true, PolylineOptions()
        .width(7.0f)
        .endCap(RoundCap())
        .visible(true)
        .addAll(coordinates)
        .addSpan(getGradientByComplexity(complexity)))

class Lift(
        name: String,
        coordinates: List<LatLng>
) : Road(name, coordinates, true, PolylineOptions()
        .width(8.0f)
        .endCap(RoundCap())
        .visible(true)
        .pattern(listOf(Gap(10F), Dash(15F)))
        .color(R.color.lift)
        .addAll(coordinates))