package com.app.snappyrulerset.tools

import com.app.snappyrulerset.model.Vec2

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 12/09/2025
 */


class RulerTool(override var position: Vec2 = Vec2(200f, 200f), override var rotationDeg: Float = 0f) : ManipulableTool {
    override val name = "RULER"
    var lengthPx: Float = 400f
}