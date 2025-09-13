package com.app.snappyrulerset.ui

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 12/09/2025
 */


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.snappyrulerset.model.CanvasState


@Composable
fun ToolPalette(state: CanvasState) {
    Row(modifier = Modifier.padding(8.dp)) {
        Button(onClick = { state.selectedTool = "RULER" }, modifier = Modifier.padding(end = 8.dp)) { Text("Ruler") }
        Button(onClick = { state.selectedTool = "SETSQUARE" }, modifier = Modifier.padding(end = 8.dp)) { Text("Set Square") }
        Button(onClick = { state.selectedTool = "PROTRACTOR" }) { Text("Protractor") }
    }
}