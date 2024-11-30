package com.the9rtyt.ninesweeper

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MineField(
    val sizeX: Int,
    val sizeY: Int,
    val mines: Int,
) {
    enum class GameStatus {
        READY,
        PLAYING,
        LOST,
        WON
    }

    var flagCount by mutableIntStateOf(0)
    var flagMode by mutableStateOf(false)
    private var gameStatus = GameStatus.READY

    private var fieldCleared = 0

    val field = Array(sizeX) {
        Array(sizeY) {
            MineSpace()
        }
    }

    fun clearSpace(x: Int, y: Int) {
        if (gameStatus == GameStatus.READY) {
            gameStatus = GameStatus.PLAYING
        }
        if (gameStatus != GameStatus.PLAYING) return



        if (flagMode) {
            onFlagModeClicked(x, y)
        } else {
            onSpaceClicked(x, y)
        }

        if (fieldCleared == sizeX * sizeY - mines) {
            gameWon()
        }
    }

    private fun onSpaceClicked(x: Int, y: Int) {
        val clickedSpace = field[x][y]

        if (!clickedSpace.revealed && !clickedSpace.flagged) { //if it's not revealed and not flagged
            clickedSpace.reveal()
            fieldCleared++
            if (!clickedSpace.mine) { //if it's not a mine
                if (clickedSpace.adjacentMines == 0) {
                    clear(x, y)
                }
            } else { //if it's a mine
                gameLost()
            }
        }
    }

    private fun onFlagModeClicked(x: Int, y: Int) {
        val clickedSpace = field[x][y]

        if (clickedSpace.revealed) {
            if (clickedSpace.adjacentMines > 0) {
                clearNum(x, y)
            }
        } else { //not revealed
            if (clickedSpace.flagged) {
                clickedSpace.flagged = false
                flagCount--
            } else {
                if (flagCount < mines) {
                    clickedSpace.flagged = true
                    flagCount++
                }
            }
        }
    }

    private fun clear(x: Int, y: Int) {
        for (offsetX in -1..1) {
            for (offsetY in -1..1) {
                if (x == 0 && y == 0) continue
                if (x + offsetX < 0 || x + offsetX >= sizeX || y + offsetY < 0 || y + offsetY >= sizeY) continue

                val space = field[x + offsetX][y + offsetY]
                if (space.flagged || space.revealed) continue
                space.reveal()
                fieldCleared++

                if (space.adjacentMines == 0) {
                    clear(
                        x = x + offsetX,
                        y = y + offsetY
                    )
                } else if (space.mine) {
                    gameLost()
                }
            }
        }
    }

    private fun clearNum(x: Int, y: Int) {
        var foundMines = 0
        //loop around space to find flagged spaces
        for (offsetX in -1..1) {
            for (offsetY in -1..1) {
                if (x == 0 && y == 0) continue
                if (x + offsetX < 0 || x + offsetX >= sizeX ||
                    y + offsetY < 0 || y + offsetY >= sizeY
                ) continue
                if (field[x + offsetX][y + offsetY].flagged) foundMines++
            }
        }

        if (foundMines >= field[x][y].adjacentMines) {
            clear(x, y)
        }
    }

    private fun gameLost() {
        gameStatus = GameStatus.LOST

        //loop through field and reveal the mines
        for (rows in field) {
            for (space in rows) {
                if (space.mine) {
                    space.reveal()
                }
            }
        }

    }

    private fun gameWon() {
        gameStatus = GameStatus.WON
    }

    fun generateField() {
        Log.i("MineSweeper", "Generating field with $mines mines")
        var genMines = mines
        var spacesGenerated = 0

        //reset field MineSpaces
        //loop throw columns
        for (rows in field) {
            //loop through rows
            for (space in rows) {
                space.revealed = false
                space.flagged = false
                space.adjacentMines = 0
            }
        }

        //generate mines and add numbers
        //loop throw columns
        for (column in 0..<sizeX) {
            //loop through rows
            for (row in 0..<sizeY) {
                val newMine = Math.random() * (sizeX * sizeY - spacesGenerated) < genMines
                if (newMine) {
                    genMines--
                    for (x in -1..1) {
                        for (y in -1..1) {
//                            if (x == 0 && y == 0) continue
                            if (column + x < 0 || column + x >= sizeX || row + y < 0 || row + y >= sizeY) continue
                            field[column + x][row + y].adjacentMines++
                        }
                    }
                }

                field[column][row].mine = newMine

                spacesGenerated++
            }
        }

        //reset things
        fieldCleared = 0
        flagCount = 0
        flagMode = false
        gameStatus = GameStatus.READY
    }
}
