package com.app.snappyrulerset.testing

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 13/09/2025
 */
import com.app.snappyrulerset.model.Vec2
import kotlin.math.sqrt
import org.junit.Assert.assertEquals
import org.junit.Test

class Vec2MathTest {

    @Test
    fun testVectorAddition() {
        val v1 = Vec2(3f, 4f)
        val v2 = Vec2(1f, 2f)
        val result = v1 + v2
        assertEquals(4f, result.x)
        assertEquals(6f, result.y)
    }

    @Test
    fun testVectorLength() {
        val v = Vec2(3f, 4f)
        val length = sqrt((v.x * v.x + v.y * v.y).toDouble())
        assertEquals(5.0, length, 0.01)
    }
}