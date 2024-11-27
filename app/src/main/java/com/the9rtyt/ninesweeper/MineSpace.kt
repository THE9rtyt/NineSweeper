package com.the9rtyt.ninesweeper

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource

class MineSpace {
    var flagged by mutableStateOf(false)
    var revealed by mutableStateOf(false)
    var mine by mutableStateOf(false)
    var adjacentMines by mutableIntStateOf(0)

    fun reveal() {
        this.revealed = true
    }

    @Composable
    fun MineSpaceView(modifier: Modifier = Modifier) {
        if (!this.revealed) {
            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.covered_square),
                contentDescription = R.string.covered_square_content_text.toString(),
                modifier = modifier,
            )
            if (this.flagged) {
                Image(
                    bitmap = ImageBitmap.imageResource(R.drawable.flag_square),
                    contentDescription = "flagged space",
                    modifier = modifier,
                )
            }
        } else { //revealed
            if (this.mine) {
                Image(
                    bitmap = ImageBitmap.imageResource(R.drawable.mine_square),
                    contentDescription = "space with mine",
                    modifier = modifier,
                )
            } else {
                Image(
                    bitmap = ImageBitmap.imageResource(R.drawable.background_square),
                    contentDescription = null,
                    modifier = modifier,
                )
                if (this.adjacentMines > 0) {
                    val number = when (this.adjacentMines) {
                        1 -> R.drawable.text1
                        2 -> R.drawable.text2
                        3 -> R.drawable.text3
                        4 -> R.drawable.text4
                        5 -> R.drawable.text5
                        6 -> R.drawable.text6
                        7 -> R.drawable.text7
                        8 -> R.drawable.text8
                        else -> R.drawable.text9
                    }

                    Image(
                        bitmap = ImageBitmap.imageResource(number),
                        contentDescription = "space with $this.adjacentMines adjacent mines",
                        modifier = modifier
                    )
                }
            }
        }
    }
}
