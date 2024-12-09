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

        if (gameStatus != GameStatus.PLAYING) {
            if (gameStatus == GameStatus.READY) gameBegin()
            else return
        }



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

        if (!clickedSpace.isRevealed() && !clickedSpace.isFlagged()) { //if it's not revealed and not flagged
            clickedSpace.reveal()
            fieldCleared++
            if (!clickedSpace.isMine()) {
                if (clickedSpace.getAdjacentMines() == 0) {
                    clear(x, y)
                }
            } else {
                gameLost()
            }
        }
    }

    private fun onFlagModeClicked(x: Int, y: Int) {
        val clickedSpace = field[x][y]

        if (clickedSpace.isRevealed()) {
            if (clickedSpace.getAdjacentMines() > 0) {
                clearNum(x, y)
            }
        } else {
            if (clickedSpace.isFlagged()) {
                clickedSpace.unFlag()
                flagCount--
            } else {
                if (flagCount < mines) {
                    clickedSpace.flag()
                    flagCount++
                }
            }
        }
    }

    private fun clear(x: Int, y: Int) {
        for (offsetX in -1..1) {
            for (offsetY in -1..1) {
                if (offsetX == 0 && offsetY == 0) continue
                if (x + offsetX < 0 || x + offsetX >= sizeX || y + offsetY < 0 || y + offsetY >= sizeY) continue

                val space = field[x + offsetX][y + offsetY]
                if (space.isFlagged() || space.isRevealed()) continue
                space.reveal()
                fieldCleared++

                if (space.getAdjacentMines() == 0) {
                    clear(
                        x = x + offsetX,
                        y = y + offsetY
                    )
                } else if (space.isMine()) {
                    gameLost()
                }
            }
        }
    }

    private fun clearNum(x: Int, y: Int) {
        var foundFlags = 0
        //loop around space to find flagged spaces
        for (offsetX in -1..1) {
            for (offsetY in -1..1) {
                if (x == 0 && y == 0) continue
                if (x + offsetX < 0 || x + offsetX >= sizeX ||
                    y + offsetY < 0 || y + offsetY >= sizeY
                ) continue
                if (field[x + offsetX][y + offsetY].isFlagged()) foundFlags++
            }
        }

        if (foundFlags >= field[x][y].getAdjacentMines()) {
            clear(x, y)
        }
    }

    private fun gameBegin() {
        generateField()
        gameStatus = GameStatus.PLAYING
    }

    private fun gameLost() {
        Log.i(this.toString(), "Game Lost")
        gameStatus = GameStatus.LOST

        //loop through field and reveal the mines
        for (rows in field) {
            for (space in rows) {
                if (space.isMine()) {
                    space.reveal()
                }
            }
        }

    }

    private fun gameWon() {
        Log.i(this.toString(), "Game Won")
        gameStatus = GameStatus.WON
    }

    fun gameReset() {
        //reset field MineSpaces
        //loop throw columns
        for (rows in field) {
            //loop through rows
            for (space in rows) {
                space.reset()
            }
        }

        //reset fields
        flagCount = 0
        flagMode = false
        gameStatus = GameStatus.READY
        fieldCleared = 0
    }

    private fun generateField() {
        Log.i(this.toString(), "Generating ${sizeX}x${sizeY} field with $mines mines")
        var genMines = mines
        var spacesGenerated = 0

        //generate mines and add numbers
        //loop throw columns
        for (column in 0..<sizeX) {
            //loop through rows
            for (row in 0..<sizeY) {
                val space = field[column][row]
                if(space.isGenerated()) continue

                val newMine = Math.random() * (sizeX * sizeY - spacesGenerated) < genMines
                if (newMine) {
                    genMines--
                    for (x in -1..1) {
                        for (y in -1..1) {
//                            if (x == 0 && y == 0) continue
                            if (column + x < 0 || column + x >= sizeX || row + y < 0 || row + y >= sizeY) continue
                            field[column + x][row + y].incrementAdjacentMines()
                        }
                    }
                }

                space.generate(newMine)
                spacesGenerated++
            }
        }
    }

    override fun toString(): String {
        return "NineField"
    }
}
