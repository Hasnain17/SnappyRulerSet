package com.app.snappyrulerset.tools

import com.app.snappyrulerset.model.Vec2

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 12/09/2025
 */


class ProtractorTool : ManipulableTool {
    override val name = "PROTRACTOR"
    override var position: Vec2 = Vec2(400f, 400f)
    override var rotationDeg: Float = 0f
}