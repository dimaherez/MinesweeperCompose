package com.example.minesweepercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.minesweepercompose.engine.Commands
import com.example.minesweepercompose.engine.Field
import com.example.minesweepercompose.engine.MinesweeperEngine
import com.example.minesweepercompose.engine.MinesweeperEngine.Companion.MINES
import com.example.minesweepercompose.engine.MinesweeperEngine.Companion.countFlags
import com.example.minesweepercompose.layouts.MinesweeperLayout
import com.example.minesweepercompose.ui.theme.Typography
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(typography = Typography) {
                MinesweeperStateHolder()
            }
        }
    }
}

@Composable
fun MinesweeperStateHolder(engine: MinesweeperEngine = MinesweeperEngine) {
    var state by remember { mutableStateOf(engine.generateField()) }
    var elapsedTime by remember { mutableIntStateOf(0) }
    var gameStarted by remember { mutableStateOf(false) }

    // Timer
    LaunchedEffect(gameStarted && engine.gameInProgress(state)) {
        if (gameStarted && engine.gameInProgress(state)) {
            while (gameStarted && engine.gameInProgress(state)) {
                delay(1000L)
                elapsedTime++
            }
        }
    }

    val onCLick: (Int) -> Unit = { index ->
        gameStarted = true
        if (engine.gameInProgress(state))
            state = engine.handleCommand(state, index, Commands.FREE)
    }

    val onLongClick: (Int) -> Unit = { index ->
        gameStarted = true
        if (engine.gameInProgress(state))
            state = engine.handleCommand(state, index, Commands.MINE)
    }

    val onReset: () -> Unit = {
        state = engine.generateField()
        elapsedTime = 0
        gameStarted = false
    }

    Minesweeper(
        state,
        minesCounter = MINES - state.countFlags(),
        isVictory = engine.isWin(state),
        isLost = engine.isLost(state),
        elapsedTime = elapsedTime,
        onCLick = onCLick,
        onLongClick = onLongClick,
        onReset = onReset
    )
}

@Composable
fun Minesweeper(
    state: Field,
    minesCounter: Int,
    isVictory: Boolean,
    isLost: Boolean,
    elapsedTime: Int,
    onCLick: (Int) -> Unit,
    onLongClick: (Int) -> Unit,
    onReset: () -> Unit
) {

    Box(
        modifier = with(Modifier) {
            fillMaxSize()
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .padding(10.dp),
            verticalArrangement = Arrangement.SpaceAround)
        {
            MinesweeperLayout(state, minesCounter, isVictory, isLost, elapsedTime, onCLick, onLongClick, onReset)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MinesweeperLayoutPreview() {
    MinesweeperStateHolder()
}