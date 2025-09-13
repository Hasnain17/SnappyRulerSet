package com.app.snappyrulerset.tools

import com.app.snappyrulerset.model.Vec2

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 12/09/2025
 */




sealed interface Tool {
    val name: String
}


interface ManipulableTool : Tool {
    var position: Vec2
    var rotationDeg: Float
}