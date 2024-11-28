package com.the9rtyt.ninesweeper

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

class MineField(
    private val sizeX: Int,
    private val sizeY: Int,
    private val mines: Int,
) {
    enum class GameStatus {
        READY,
        PLAYING,
        LOST,
        WON
    }

    var mineCount by mutableIntStateOf(mines)
    var flagMode by mutableStateOf(false)
    private var gameStatus = GameStatus.READY

    private var fieldCleared = 0
    private var spaceWidth = 99f
    private var spaceHeight = 99f

    private val field = Array(sizeX) {
        Array(sizeY) {
            MineSpace()
        }
    }

    @Composable
    fun MineFieldView(modifier: Modifier = Modifier) {
        //bring up image bitmaps
        val coveredSquare = ImageBitmap.imageResource(R.drawable.covered_square)
        val flaggedSquare = ImageBitmap.imageResource(R.drawable.flag_square)
        val mineSquare = ImageBitmap.imageResource(R.drawable.mine_square)
        val backgroundSquare = ImageBitmap.imageResource(R.drawable.background_square)

        //bring up text bitmaps
        val text1Square = ImageBitmap.imageResource(R.drawable.text1)
        val text2Square = ImageBitmap.imageResource(R.drawable.text2)
        val text3Square = ImageBitmap.imageResource(R.drawable.text3)
        val text4Square = ImageBitmap.imageResource(R.drawable.text4)
        val text5Square = ImageBitmap.imageResource(R.drawable.text5)
        val text6Square = ImageBitmap.imageResource(R.drawable.text6)
        val text7Square = ImageBitmap.imageResource(R.drawable.text7)
        val text8Square = ImageBitmap.imageResource(R.drawable.text8)

        coveredSquare.prepareToDraw()
        flaggedSquare.prepareToDraw()
        mineSquare.prepareToDraw()
        backgroundSquare.prepareToDraw()

        text1Square.prepareToDraw()
        text2Square.prepareToDraw()
        text3Square.prepareToDraw()
        text4Square.prepareToDraw()
        text5Square.prepareToDraw()
        text6Square.prepareToDraw()
        text7Square.prepareToDraw()
        text8Square.prepareToDraw()

        Canvas(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { tapOffset ->
                            val column = (tapOffset.x / spaceWidth).toInt()
                            val row = (tapOffset.y / spaceHeight).toInt()

                            if (gameStatus == GameStatus.READY) {
                                gameStatus = GameStatus.PLAYING
                            }
                            if (gameStatus != GameStatus.PLAYING) return@detectTapGestures

                            Log.i("MineSweeper", "tap at row: $row column: $column")

                            if (flagMode) {
                                onFlagModeClicked(column, row)
                            } else {
                                onSpaceClicked(column, row)
                            }

                            if (fieldCleared == sizeX * sizeY - mines) {
                                gameWon()
                            }
                        }
                    )
                }
        ) {
            spaceWidth = size.width / sizeX
            spaceHeight = size.height / sizeY

            field.forEachIndexed { x, row ->
                row.forEachIndexed { y, space ->
                    val offset = Offset(x * spaceWidth, y * spaceHeight)
                    val intOffset = IntOffset(offset.x.toInt(), offset.y.toInt())
                    val intSize = IntSize(spaceWidth.toInt(), spaceHeight.toInt())

                    if (!space.revealed) {
                        drawImage(
                            image = coveredSquare,
                            dstOffset = intOffset,
                            dstSize = intSize,
                        )
                        if (space.flagged) {
                            drawImage(
                                image = flaggedSquare,
                                dstOffset = intOffset,
                                dstSize = intSize,
                            )
                        }
                    } else { //revealed
                        if (space.mine) {
                            drawImage(
                                image = mineSquare,
                                dstOffset = intOffset,
                                dstSize = intSize,
                            )
                        } else {
                            drawImage(
                                image = backgroundSquare,
                                dstOffset = intOffset,
                                dstSize = intSize,
                            )
                            if (space.adjacentMines > 0) {
                                val number = when (space.adjacentMines) {
                                    1 -> text1Square
                                    2 -> text2Square
                                    3 -> text3Square
                                    4 -> text4Square
                                    5 -> text5Square
                                    6 -> text6Square
                                    7 -> text7Square
                                    else -> text8Square
                                }

                                drawImage(
                                    image = number,
                                    dstOffset = intOffset,
                                    dstSize = intSize,
                                )
                            }
                        }
                    }
                }
            }
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
            if (mineCount > 0) {
                clickedSpace.flagged = !clickedSpace.flagged
                if (clickedSpace.flagged) {
                    mineCount--
                } else {
                    mineCount++
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
        mineCount = mines
        flagMode = false
        gameStatus = GameStatus.READY
    }
}
