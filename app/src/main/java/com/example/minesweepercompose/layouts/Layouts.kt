package com.example.minesweepercompose.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.minesweepercompose.R
import com.example.minesweepercompose.engine.Field
import com.example.minesweepercompose.engine.MinesweeperEngine.Companion.DIMENSION
import com.example.minesweepercompose.engine.MinesweeperEngine.Companion.MINES
import com.example.minesweepercompose.ui.theme.DarkGreen
import com.example.minesweepercompose.ui.theme.GreyBg
import com.example.minesweepercompose.ui.theme.GreyBorder

@Composable
fun MinesweeperLayout(
    state: Field,
    minesCounter: Int,
    isVictory: Boolean,
    isLost: Boolean,
    elapsedTime: Int,
    onClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .drawBorder(
                colorTop = Color.White,
                colorLeft = Color.White,
                colorRight = GreyBorder,
                colorBottom = GreyBorder
            )
            .padding(4.dp)
            .background(GreyBg)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp)
                .drawBorder()
                .padding(top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MinesCounterDigits(minesCounter)

            FaceButton(isLost, isVictory, onReset)

            TimerDigits(elapsedTime = elapsedTime)
        }

        // Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(DIMENSION),
            modifier = Modifier
                .fillMaxWidth()
                .drawBorder()
                .padding(4.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(state.flatten().size) { index ->
                val iRow = index / DIMENSION
                val iCol = index % DIMENSION
                Cell(
                    state[iRow][iCol],
                    { onClick(iRow * DIMENSION + iCol) },
                    { onLongClick(iRow * DIMENSION + iCol) }
                )
            }
        }

        Text(
            modifier = Modifier.wrapContentSize(),
            text = if (isVictory) stringResource(R.string.win_message)
            else if (isLost) stringResource(R.string.fail_message)
            else "",
            color = if (isVictory) DarkGreen else Color.Red
        )

    }
}

@Composable
fun FaceButton(isLost: Boolean, isVictory: Boolean, onReset: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }

    val defaultFace = when {
        isLost -> R.drawable.face_lose
        isVictory -> R.drawable.face_win
        else -> R.drawable.face_unpressed
    }
    val faceImage = if (isPressed) R.drawable.face_pressed else defaultFace

    Image(
        modifier = Modifier
            .size(64.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onReset()
                    }
                )
            },
        painter = painterResource(faceImage),
        contentDescription = "",
        contentScale = ContentScale.Crop
    )
}


@Composable
fun MinesCounterDigits(minesCounter: Int) {
    val tens = minesCounter / MINES
    val units = minesCounter % MINES

    val digitsList = listOf(
        R.drawable.d0,
        R.drawable.d1,
        R.drawable.d2,
        R.drawable.d3,
        R.drawable.d4,
        R.drawable.d5,
        R.drawable.d6,
        R.drawable.d7,
        R.drawable.d8,
        R.drawable.d9,
    )

    Row(modifier = Modifier.wrapContentSize()) {


        Image(
            modifier = Modifier
                .height(64.dp)
                .border(2.dp, Color.Black),
            painter = painterResource(R.drawable.d0),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        Image(
            modifier = Modifier
                .height(64.dp)
                .border(2.dp, Color.Black),
            painter = painterResource(digitsList[tens]),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        Image(
            modifier = Modifier
                .height(64.dp)
                .border(2.dp, Color.Black),
            painter = painterResource(digitsList[units]),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun TimerDigits(elapsedTime: Int) {
    val hundreds = elapsedTime / 100
    val tens = (elapsedTime % 100) / 10
    val units = elapsedTime % 10

    val digitsList = listOf(
        R.drawable.d0,
        R.drawable.d1,
        R.drawable.d2,
        R.drawable.d3,
        R.drawable.d4,
        R.drawable.d5,
        R.drawable.d6,
        R.drawable.d7,
        R.drawable.d8,
        R.drawable.d9,
    )

    Row(modifier = Modifier.wrapContentSize()) {
        Image(
            modifier = Modifier
                .height(64.dp)
                .border(2.dp, Color.Black),
            painter = painterResource(digitsList[hundreds]),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
        Image(
            modifier = Modifier
                .height(64.dp)
                .border(2.dp, Color.Black),
            painter = painterResource(digitsList[tens]),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
        Image(
            modifier = Modifier
                .height(64.dp)
                .border(2.dp, Color.Black),
            painter = painterResource(digitsList[units]),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}


//@Composable
//fun ResponsiveLayout(state: Field, onCLick: (Int) -> Unit, onLongClick: (Int) -> Unit, onReset: () -> Unit) {
//    val configuration = LocalConfiguration.current
//
//    when (configuration.orientation) {
//        Configuration.ORIENTATION_LANDSCAPE -> {
//            LandscapeGrid(state, onCLick, onLongClick)
//        }
//
//        else -> {
//            PortraitGrid(state, onCLick, onLongClick, onReset)
//        }
//    }
//}

//@Composable
//fun LandscapeGrid(
//    state: Field,
//    onClick: (Int) -> Unit,
//    onLongClick: (Int) -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(start = 32.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center
//    ) {
//        for (iCol in 0 until DIMENSION) {
//            Column {
//                for (iRow in 0 until DIMENSION) {
//                    Cell(
//                        state[iRow][iCol],
//                        { onClick(iRow * DIMENSION + iCol) },
//                        { onLongClick(iRow * DIMENSION + iCol) }
//                    )
//                }
//            }
//        }
//    }
//}