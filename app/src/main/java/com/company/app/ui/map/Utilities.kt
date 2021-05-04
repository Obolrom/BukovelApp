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

object RedGradient: Gradient(Color.RED, Color.RED)

object BlueGradient: Gradient(Color.BLUE, Color.BLUE)

object BlackGradient: Gradient(Color.BLACK, Color.BLACK)