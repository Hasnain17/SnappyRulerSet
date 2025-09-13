package com.app.snappyrulerset.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.app.snappyrulerset.model.CanvasState
import com.app.snappyrulerset.model.Circle
import com.app.snappyrulerset.model.FreehandPath
import com.app.snappyrulerset.model.LineSegment
import com.app.snappyrulerset.tools.UndoRedoManager
import com.app.snappyrulerset.model.Vec2
import com.app.snappyrulerset.tools.RulerTool
import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.snappyrulerset.tools.ProtractorTool
import com.app.snappyrulerset.tools.SetSquareTool
import com.app.snappyrulerset.tools.util.Snapper
import java.io.File
import kotlin.math.*

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 12/09/2025
 */

@Composable
fun DrawingScreen() {
    val canvasState = remember { CanvasState() }
    val undo = remember { UndoRedoManager(initial = canvasState.shapes.map { it }.toMutableList()) }

    // Tools
    val ruler = remember { RulerTool() }
    val setSquare = remember { SetSquareTool("45") }
    val protractor = remember { ProtractorTool() }
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFFAFAFA)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues()) // avoid system bar overlap
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                ToolPalette(canvasState)

                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            // Long press toggles snapping
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        canvasState.snapEnabled = !canvasState.snapEnabled
                                        Toast.makeText(
                                            context,
                                            if (canvasState.snapEnabled) "Snap enabled" else "Snap disabled",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                            // Handle pan/zoom/rotate gestures
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, rotation ->
                                    if (canvasState.selectedTool in listOf("RULER", "SETSQUARE", "PROTRACTOR")) {
                                        // Move & rotate tools
                                        when (canvasState.selectedTool) {
                                            "RULER" -> {
                                                ruler.position += Vec2(pan.x, pan.y)
                                                ruler.rotationDeg += (rotation * 180f / Math.PI.toFloat())
                                                canvasState.currentAngle = ruler.rotationDeg
                                                canvasState.currentLengthCm = ruler.lengthPx / 50f
                                            }
                                            "SETSQUARE" -> {
                                                setSquare.position += Vec2(pan.x, pan.y)
                                                setSquare.rotationDeg += (rotation * 180f / Math.PI.toFloat())
                                                canvasState.currentAngle = setSquare.rotationDeg
                                            }
                                            "PROTRACTOR" -> {
                                                protractor.position += Vec2(pan.x, pan.y)
                                                protractor.rotationDeg += (rotation * 180f / Math.PI.toFloat())
                                                canvasState.currentAngle = protractor.rotationDeg
                                            }
                                        }
                                    } else {
                                        // Pan + zoom the canvas itself
                                        canvasState.offsetX += pan.x
                                        canvasState.offsetY += pan.y
                                        if (zoom != 1f) canvasState.zoom *= zoom
                                    }
                                }
                            }
                            // Freehand drawing with snapping
                            .pointerInput(canvasState.selectedTool) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        if (canvasState.selectedTool == "FREEHAND") {
                                            val p = screenToWorld(offset, canvasState)
                                            val fh = FreehandPath(mutableListOf(p))
                                            canvasState.shapes.add(fh)
                                        }
                                    },
                                    onDrag = { change, _ ->
                                        if (canvasState.selectedTool == "FREEHAND") {
                                            var p = screenToWorld(change.position, canvasState)
                                            if (canvasState.snapEnabled) {
                                                p = Snapper.snapToPoint(
                                                    p,
                                                    collectSnapPoints(canvasState),
                                                    { it }, // identity transform for now
                                                    snapPx = 20f
                                                )
                                            }
                                            val last = canvasState.shapes.lastOrNull() as? FreehandPath
                                            last?.points?.add(p)
                                        }
                                    },
                                    onDragEnd = {
                                        if (canvasState.selectedTool == "FREEHAND") {
                                            undo.save(canvasState.shapes.map { it }.toMutableList())
                                        }
                                    }
                                )
                            }
                    ) {
                        // Apply global zoom + pan transforms
                        withTransform({
                            translate(left = canvasState.offsetX, top = canvasState.offsetY)
                            scale(canvasState.zoom)
                        }) {
                            // Draw saved shapes
                            for (shape in canvasState.shapes) {
                                when (shape) {
                                    is FreehandPath -> drawPathFromPoints(shape.points)
                                    is LineSegment -> drawLine(
                                        color = Color.Black,
                                        start = Offset(shape.a.x, shape.a.y),
                                        end = Offset(shape.b.x, shape.b.y),
                                        strokeWidth = 3f,
                                        cap = StrokeCap.Round
                                    )
                                    is Circle -> drawCircle(
                                        color = Color.Black,
                                        radius = shape.radius,
                                        center = Offset(shape.center.x, shape.center.y),
                                        style = Stroke(width = 2f)
                                    )
                                }
                            }

                            // Draw tools (they inherit zoom + offset)
                            when (canvasState.selectedTool) {
                                "RULER" -> {
                                    val rad = Math.toRadians(ruler.rotationDeg.toDouble())
                                    val dx = cos(rad) * ruler.lengthPx / 2
                                    val dy = sin(rad) * ruler.lengthPx / 2
                                    drawLine(
                                        start = Offset(ruler.position.x - dx.toFloat(), ruler.position.y - dy.toFloat()),
                                        end = Offset(ruler.position.x + dx.toFloat(), ruler.position.y + dy.toFloat()),
                                        color = Color.Gray,
                                        strokeWidth = 4f
                                    )
                                }
                                "SETSQUARE" -> {
                                    val base = setSquare.sizePx
                                    val points = listOf(
                                        Offset(0f, 0f),
                                        Offset(base, 0f),
                                        Offset(0f, -base)
                                    )
                                    val rad = Math.toRadians(setSquare.rotationDeg.toDouble())
                                    val cosA = cos(rad).toFloat()
                                    val sinA = sin(rad).toFloat()
                                    val transformed = points.map {
                                        Offset(
                                            setSquare.position.x + (it.x * cosA - it.y * sinA),
                                            setSquare.position.y + (it.x * sinA + it.y * cosA)
                                        )
                                    }
                                    drawLine(color = Color.Blue, start = transformed[0], end = transformed[1], strokeWidth = 3f)
                                    drawLine(color = Color.Blue, start = transformed[1], end = transformed[2], strokeWidth = 3f)
                                    drawLine(color = Color.Blue, start = transformed[2], end = transformed[0], strokeWidth = 3f)
                                }
                                "PROTRACTOR" -> {
                                    drawArc(
                                        color = Color.Green,
                                        startAngle = 0f + protractor.rotationDeg,
                                        sweepAngle = 180f,
                                        useCenter = false,
                                        topLeft = Offset(protractor.position.x - 150f, protractor.position.y - 75f),
                                        size = androidx.compose.ui.geometry.Size(300f, 150f),
                                        style = Stroke(width = 3f)
                                    )
                                }
                            }
                        }
                    }

                    HudOverlay(angle = canvasState.currentAngle, lengthCm = canvasState.currentLengthCm)
                }

                // Bottom row
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Button(
                        onClick = {
                            try {
                                val bmp = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888)
                                val c = AndroidCanvas(bmp)

                                val paint = android.graphics.Paint().apply {
                                    color = android.graphics.Color.BLACK
                                    style = android.graphics.Paint.Style.STROKE
                                    strokeWidth = 3f
                                }

                                // Draw shapes
                                canvasState.shapes.forEach { shape ->
                                    when (shape) {
                                        is FreehandPath -> {
                                            val path = android.graphics.Path()
                                            if (shape.points.isNotEmpty()) {
                                                path.moveTo(shape.points[0].x, shape.points[0].y)
                                                for (i in 1 until shape.points.size) {
                                                    path.lineTo(shape.points[i].x, shape.points[i].y)
                                                }
                                            }
                                            c.drawPath(path, paint)
                                        }
                                        is LineSegment -> {
                                            c.drawLine(shape.a.x, shape.a.y, shape.b.x, shape.b.y, paint)
                                        }
                                        is Circle -> {
                                            c.drawCircle(shape.center.x, shape.center.y, shape.radius, paint)
                                        }
                                    }
                                }

                                // Draw selected tools (optional)
                                val toolPaint = android.graphics.Paint().apply {
                                    style = android.graphics.Paint.Style.STROKE
                                    strokeWidth = 4f
                                    color = android.graphics.Color.GRAY
                                }

                                when (canvasState.selectedTool) {
                                    "RULER" -> {
                                        val rad = Math.toRadians(ruler.rotationDeg.toDouble())
                                        val dx = cos(rad) * ruler.lengthPx / 2
                                        val dy = sin(rad) * ruler.lengthPx / 2
                                        c.drawLine(
                                            (ruler.position.x - dx).toFloat(),
                                            (ruler.position.y - dy).toFloat(),
                                            (ruler.position.x + dx).toFloat(),
                                            (ruler.position.y + dy).toFloat(),
                                            toolPaint
                                        )
                                    }
                                    "SETSQUARE" -> {
                                        val base = setSquare.sizePx
                                        val points = listOf(
                                            Offset(0f, 0f),
                                            Offset(base, 0f),
                                            Offset(0f, -base)
                                        )
                                        val rad = Math.toRadians(setSquare.rotationDeg.toDouble())
                                        val cosA = cos(rad).toFloat()
                                        val sinA = sin(rad).toFloat()
                                        val transformed = points.map {
                                            Offset(
                                                setSquare.position.x + (it.x * cosA - it.y * sinA),
                                                setSquare.position.y + (it.x * sinA + it.y * cosA)
                                            )
                                        }
                                        c.drawLine(transformed[0].x, transformed[0].y, transformed[1].x, transformed[1].y, toolPaint)
                                        c.drawLine(transformed[1].x, transformed[1].y, transformed[2].x, transformed[2].y, toolPaint)
                                        c.drawLine(transformed[2].x, transformed[2].y, transformed[0].x, transformed[0].y, toolPaint)
                                    }
                                    "PROTRACTOR" -> {
                                        val rect = android.graphics.RectF(
                                            protractor.position.x - 150f,
                                            protractor.position.y - 75f,
                                            protractor.position.x + 150f,
                                            protractor.position.y + 75f
                                        )
                                        c.drawArc(rect, protractor.rotationDeg, 180f, false, toolPaint)
                                    }
                                }

                                // Save bitmap
                                val fileName = "snappy_ruler_export_${System.currentTimeMillis()}.png"
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                    val resolver = context.contentResolver
                                    val contentValues = android.content.ContentValues().apply {
                                        put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                                        put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "image/png")
                                        put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/SnappyRulerSet")
                                    }
                                    val uri = resolver.insert(
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        contentValues
                                    )
                                    if (uri != null) {
                                        resolver.openOutputStream(uri).use { out ->
                                            if (out != null) {
                                                bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
                                            }
                                        }
                                        Toast.makeText(context, "Shapes exported successfully", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                    val outFile = File(dir, fileName)
                                    outFile.outputStream().use { out ->
                                        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
                                    }
                                    Toast.makeText(context, "Saved to ${outFile.absolutePath}", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Export failed: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Export PNG")
                    }




                    Button(onClick = {
                        if (undo.canUndo()) {
                            undo.undo()
                            canvasState.shapes = undo.present
                        }
                    }, modifier = Modifier.padding(end = 8.dp)) { Text("Undo") }

                    Button(onClick = {
                        if (undo.canRedo()) {
                            undo.redo()
                            canvasState.shapes = undo.present
                        }
                    }) { Text("Redo") }
                }
            }
        }
    }
}

private fun DrawScope.drawPathFromPoints(points: List<Vec2>) {
    if (points.size < 2) return
    val path = Path()
    path.moveTo(points[0].x, points[0].y)
    for (i in 1 until points.size) path.lineTo(points[i].x, points[i].y)
    drawPath(path, color = Color.Black, style = Stroke(width = 3f))
}

private operator fun Vec2.plus(other: Vec2): Vec2 = Vec2(this.x + other.x, this.y + other.y)

private fun collectSnapPoints(state: CanvasState): List<Vec2> {
    val points = mutableListOf<Vec2>()
    state.shapes.forEach { shape ->
        when (shape) {
            is LineSegment -> {
                points.add(shape.a)
                points.add(shape.b)
            }
            is Circle -> points.add(shape.center)
            is FreehandPath -> points.addAll(shape.points)
        }
    }
    return points
}

private fun screenToWorld(offset: Offset, state: CanvasState): Vec2 {
    val x = (offset.x - state.offsetX) / state.zoom
    val y = (offset.y - state.offsetY) / state.zoom
    return Vec2(x, y)
}