package com.the9rtyt.ninesweeper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MineSpace {
    var flagged by mutableStateOf(false)
    var revealed by mutableStateOf(false)
    var mine = false
    var adjacentMines = 0

    fun reveal() {
        this.revealed = true
    }
}
