package com.company.app.ui.map

import com.company.app.R
import com.google.android.libraries.maps.model.*

abstract class Road(
        val name: String,
        val coordinates: List<LatLng>,
        var active: Boolean,
        var style: PolylineOptions
) {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is Road) return false

                if (name != other.name) return false
                if (coordinates != other.coordinates) return false
                if (active != other.active) return false
                if (style != other.style) return false

                return true
        }

        override fun hashCode(): Int {
                var result = name.hashCode()
                result = 31 * result + coordinates.hashCode()
                result = 31 * result + active.hashCode()
                result = 31 * result + style.hashCode()
                return result
        }
}

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
        .addAll(coordinates)) {

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                return super.equals(other)
        }

        override fun hashCode(): Int {
                return super.hashCode()
        }
}