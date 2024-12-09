package com.the9rtyt.ninesweeper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MineSpace {
    private var flagged by mutableStateOf(false)
    private var revealed by mutableStateOf(false)
    private var mine = false
    private var adjacentMines = 0
    private var generated = false

    //MineSpace actions
    fun flag() {
        this.flagged = true
    }

    fun unFlag() {
        this.flagged = false
    }

    fun reveal() {
        this.revealed = true
    }

    fun incrementAdjacentMines() {
        this.adjacentMines++
    }

    fun generate(isMine: Boolean) {
        this.mine = isMine
        this.generated = true
    }

    fun reset() {
        flagged = false
        revealed = false
        adjacentMines = 0
        generated = false
    }

    //Getters
    fun isFlagged(): Boolean {
        return flagged
    }

    fun isRevealed(): Boolean {
        return revealed
    }

    fun isMine(): Boolean {
        return mine
    }

    fun getAdjacentMines(): Int {
        return adjacentMines
    }

    fun isGenerated(): Boolean {
        return generated
    }
}
