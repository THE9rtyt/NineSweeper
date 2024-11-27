package com.the9rtyt.ninesweeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.the9rtyt.ninesweeper.ui.theme.NineSweeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        val field = MineField(13, 25, 69)

        setContent {
            NineSweeperTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.LightGray
                ) {
                    Column(
                        modifier = Modifier
                    ) {
                        Row(
                            modifier = Modifier
                        ) {
                            Image(
                                bitmap = ImageBitmap.imageResource(R.drawable.mine_square),
                                contentDescription = "space with mine",
                                modifier = Modifier
                                    .clickable { field.generateField() }
                                    .weight(0.1f)
                                    .align(Alignment.Bottom)
                            )

                            Image(
                                bitmap = ImageBitmap.imageResource(R.drawable.flag_square),
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable { field.flagMode = !field.flagMode }
                                    .weight(0.1f)
                                    .align(Alignment.Top)
                            )
                        }


                        Box(
                            contentAlignment = Alignment.BottomCenter,
                            modifier = Modifier
                                .padding(4.dp)
                                .background(Color.Gray)
                        ) {
                            field.MineFieldView(
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }

        field.generateField()
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