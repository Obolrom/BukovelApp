package com.company.app.ui.map

import android.graphics.Color
import com.google.android.libraries.maps.model.StrokeStyle
import com.google.android.libraries.maps.model.StyleSpan

fun getGradientByComplexity(complexity: Complexity): StyleSpan {
    val gradient = complexity.getGradient()

   return StyleSpan(StrokeStyle
            .gradientBuilder(gradient.highest, gradient.lowest)
            .build())
}

enum class Complexity {
    BLUE, RED, BLACK;

    fun getGradient(): Gradient {
        return when(this) {
            BLUE -> BlueGradient
            RED -> RedGradient
            BLACK -> BlackGradient
        }
    }
}

abstract class Gradient(
        val lowest: Int,
        val highest: Int)

object RedGradient: Gradient(
        Color.parseColor("#ff810000"),
        Color.parseColor("#ffce1212"))

object BlueGradient: Gradient(
        Color.parseColor("#ff3f3697"),
        Color.parseColor("#ff3d84b8"))

object BlackGradient: Gradient(
        Color.parseColor("#ff000000"),
        Color.parseColor("#ff252525"))