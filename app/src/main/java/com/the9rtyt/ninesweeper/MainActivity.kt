package com.the9rtyt.ninesweeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.the9rtyt.ninesweeper.ui.theme.NineSweeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        val field = MineField(13, 25, 69)

        setContent {
            NineSweeperTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
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

                        field.MineFieldView(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondary)
                        )
                    }
                }
            }
        }

        field.generateField()
    }
}

@Composable
fun NineControlsView(field: MineField, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = "${field.mineCount}",
            fontSize = 40.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
        )

        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.mine_square),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clickable { field.generateField() }
        )

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
            )

            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.mine_square),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(if (field.flagMode) -1f else 1f)

            )
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
                field.MineFieldView(
                    modifier = Modifier
                )
            }
        }
    }
}