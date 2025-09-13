package com.app.snappyrulerset.tools.util

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 12/09/2025
 */
import com.app.snappyrulerset.model.Vec2
import kotlin.math.*


object Snapper {
    private val angleSnaps = listOf(0f, 30f, 45f, 60f, 90f, 120f, 135f, 150f, 180f)


    fun snapAngleDeg(angle: Float, hardSnapSet: Set<Float> = setOf(30f,45f,60f,90f,120f,135f,150f,180f), snapRadiusDeg: Float = 5f) : Float {
// normalize and return nearest snap if within radius
        val norm = ((angle % 360f) + 360f) % 360f
        val candidate = angleSnaps.minByOrNull { d -> abs(((norm - d + 540f) % 360f) - 180f) } ?: norm
        val diff = abs((norm - candidate + 540f) % 360f - 180f)
        return if (diff <= snapRadiusDeg) candidate else angle
    }


    fun snapToPoint(p: Vec2, points: List<Vec2>, worldToScreen: (Vec2) -> Vec2, snapPx: Float): Vec2 {
        var best = p
        var bestDist = Float.MAX_VALUE
        val screenP = worldToScreen(p)
        for (pt in points) {
            val s = worldToScreen(pt)
            val d = hypot(s.x - screenP.x, s.y - screenP.y)
            if (d < bestDist && d <= snapPx) { best = pt; bestDist = d }
        }
        return best
    }
}