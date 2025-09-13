package com.app.snappyrulerset.model

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 12/09/2025
 */


sealed interface CanvasShape


data class FreehandPath(val points: MutableList<Vec2> = mutableListOf()) : CanvasShape


data class LineSegment(val a: Vec2, val b: Vec2) : CanvasShape


data class Circle(val center: Vec2, val radius: Float) : CanvasShape