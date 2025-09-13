package com.app.snappyrulerset.tools

import com.app.snappyrulerset.model.Vec2

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 12/09/2025
 */




class SetSquareTool(val variant: String = "45") : ManipulableTool {
    override val name = "SETSQUARE"
    override var position: Vec2 = Vec2(350f, 350f)
    override var rotationDeg: Float = 0f
    val sizePx = 200f
}