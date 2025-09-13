package com.app.snappyrulerset.model

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 12/09/2025
 */
import kotlin.math.*


data class Vec2(val x: Float, val y: Float) {
    operator fun minus(o: Vec2) = Vec2(x - o.x, y - o.y)
    operator fun plus(o: Vec2) = Vec2(x + o.x, y + o.y)
    fun length() = sqrt(x * x + y * y)
}


fun angleBetween(a: Vec2, b: Vec2): Float {
    val dot = a.x * b.x + a.y * b.y
    val la = a.length(); val lb = b.length();
    if (la == 0f || lb == 0f) return 0f
    val cos = (dot / (la * lb)).coerceIn(-1f, 1f)
    return acos(cos) * 180f / Math.PI.toFloat()
}


fun vecFromAngleDeg(angleDeg: Float, len: Float): Vec2 {
    val r = Math.toRadians(angleDeg.toDouble())
    return Vec2((cos(r) * len).toFloat(), (sin(r) * len).toFloat())
}


fun normalizeAngleDeg(a: Float): Float {
    var x = a % 360f
    if (x < 0) x += 360f
    return x
}


fun signedAngleDeg(a: Vec2, b: Vec2): Float {
    val angA = atan2(a.y.toDouble(), a.x.toDouble())
    val angB = atan2(b.y.toDouble(), b.x.toDouble())
    val deg = Math.toDegrees(angB - angA)
    return ((deg + 540) % 360 - 180).toFloat()
}