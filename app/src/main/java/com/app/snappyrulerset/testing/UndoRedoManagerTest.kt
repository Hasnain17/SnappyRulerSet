package com.app.snappyrulerset.testing

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 13/09/2025
 */
import com.app.snappyrulerset.model.FreehandPath
import com.app.snappyrulerset.tools.UndoRedoManager
import com.app.snappyrulerset.model.Vec2
import org.junit.Assert.*
import org.junit.Test

class UndoRedoManagerTest {

    @Test
    fun testUndoRedo() {
        val shape1 = FreehandPath(mutableListOf(Vec2(0f, 0f), Vec2(10f, 10f)))
        val shape2 = FreehandPath(mutableListOf(Vec2(5f, 5f), Vec2(15f, 15f)))

        val manager = UndoRedoManager(initial = mutableListOf(shape1))

        // Save a new state
        manager.save(mutableListOf(shape1, shape2))
        assertEquals(2, manager.present.size)

        // Undo → should return to initial state
        assertTrue(manager.canUndo())
        manager.undo()
        assertEquals(1, manager.present.size)

        // Redo → should go forward again
        assertTrue(manager.canRedo())
        manager.redo()
        assertEquals(2, manager.present.size)
    }
}