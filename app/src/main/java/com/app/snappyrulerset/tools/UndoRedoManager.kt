package com.app.snappyrulerset.tools

/**
 * @Author: Muhammad Hasnain Altaf
 * @Date: 12/09/2025
 */


class UndoRedoManager<T>(
    initial: T,
    private val maxHistory: Int = 20   // default limit
) {
    private val past = mutableListOf<T>()
    var present: T = initial
        private set
    private val future = mutableListOf<T>()

    /**
     * Save a new state.
     */
    fun save(newState: T) {
        past.add(present)

        // Enforce history size limit
        if (past.size > maxHistory) {
            past.removeAt(0) // drop the oldest
        }

        present = newState
        future.clear()
    }

    /**
     * Undo last action.
     */
    fun undo() {
        if (canUndo()) {
            future.add(0, present)
            present = past.removeLast()
        }
    }

    /**
     * Redo previously undone action.
     */
    fun redo() {
        if (canRedo()) {
            past.add(present)
            present = future.removeAt(0)
        }
    }

    fun canUndo(): Boolean = past.isNotEmpty()
    fun canRedo(): Boolean = future.isNotEmpty()

    // Helpers for testing
    fun pastSize(): Int = past.size
    fun futureSize(): Int = future.size
}