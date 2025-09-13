package com.app.snappyrulerset.ui

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 12/09/2025
 */


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun HudOverlay(angle: Float?, lengthCm: Float?) {
    Column(modifier = Modifier.padding(8.dp).background(Color(0xAA000000))) {
        Text(text = "Angle: ${angle?.let { String.format("%.1f°", it) } ?: "--"}", color = Color.White)
        Text(text = "Length: ${lengthCm?.let { String.format("%.1f cm", it) } ?: "--"}", color = Color.White)
    }
}