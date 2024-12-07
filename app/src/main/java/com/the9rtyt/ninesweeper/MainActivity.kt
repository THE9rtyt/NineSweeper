package com.the9rtyt.ninesweeper

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.the9rtyt.ninesweeper.ui.theme.NineSweeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        setContent {
            NineSweeperTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    val field = MineField(13, 25, 69)

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(4.dp)
                    ) {
                        NineControlsView(
                            field = field,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .background(MaterialTheme.colorScheme.secondary)
                        )

                        Spacer(
                            modifier = Modifier
                                .height(4.dp)
                        )

                        MineFieldView(
                            field = field,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NineControlsView(field: MineField, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        //draw mine count
        Text(
            text = "${field.mines - field.flagCount}",
            fontSize = 40.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
        )

        //draw reset mine
        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.mine_square),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clickable { field.gameReset() }
        )

        //draw flag/mine mode
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .aspectRatio(1f)
                .clickable { field.flagMode = !field.flagMode }
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.flag_square),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(if (field.flagMode) 1f else -1f)
            )

            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.mine_square),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()

            )
        }
    }
}


@Composable
fun MineFieldView(field: MineField, modifier: Modifier = Modifier) {
    var spaceWidth by remember { mutableFloatStateOf(90f) }
    var spaceHeight by remember { mutableFloatStateOf(90f) }

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


                        Log.d("NineSweeper", "tap at row: $row column: $column")

                        field.clearSpace(column, row)
                    }
                )
            }
    ) {
        spaceWidth = size.width / field.sizeX
        spaceHeight = size.height / field.sizeY

        field.field.forEachIndexed { x, row ->
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

@Preview(showBackground = true)
@Composable
fun FieldPreview() {
    val field = MineField(12, 12, 45)
    NineSweeperTheme {
        Surface(
            modifier = Modifier,
            color = Color.Gray
        ) {
            Box(
                modifier = Modifier.scale(1.0f),
                contentAlignment = Alignment.Center
            ) {
                MineFieldView(
                    field = field,
                    modifier = Modifier
                )
            }
        }
    }
}