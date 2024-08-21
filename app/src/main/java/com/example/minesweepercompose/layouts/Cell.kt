package com.example.minesweepercompose.layouts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.minesweepercompose.R
import com.example.minesweepercompose.engine.CellStatus

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Cell(cellStatus: CellStatus, onClick: () -> Unit = {}, onLongClick: () -> Unit = {}) {
    val image = when (cellStatus) {
        CellStatus.FLAG -> R.drawable.flag
        CellStatus.MINE -> R.drawable.mine
        CellStatus.EXPLORED -> R.drawable.pressed
        CellStatus.M1 -> R.drawable.type1
        CellStatus.M2 -> R.drawable.type2
        CellStatus.M3 -> R.drawable.type3
        CellStatus.M4 -> R.drawable.type4
        CellStatus.M5 -> R.drawable.type5
        CellStatus.M6 -> R.drawable.type6
        CellStatus.M7 -> R.drawable.type7
        CellStatus.M8 -> R.drawable.type8
        CellStatus.EMPTY -> R.drawable.closed
    }

    Image(
        painterResource(image),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            )
            .size(35.dp)
    )
}