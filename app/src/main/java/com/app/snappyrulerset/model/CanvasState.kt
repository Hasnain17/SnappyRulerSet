package com.app.snappyrulerset.model

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 12/09/2025
 */


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


class CanvasState {
    var shapes: MutableList<CanvasShape> = mutableListOf()
    var selectedTool: String by mutableStateOf("RULER")
    var zoom: Float by mutableStateOf(1f)
    var offsetX: Float by mutableStateOf(0f)
    var offsetY: Float by mutableStateOf(0f)


    // Precision HUD
    var currentAngle: Float? by mutableStateOf(null)
    var currentLengthCm: Float? by mutableStateOf(null)

    var snapEnabled: Boolean = true

}