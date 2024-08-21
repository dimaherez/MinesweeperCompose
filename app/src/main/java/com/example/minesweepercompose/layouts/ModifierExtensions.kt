package com.example.minesweepercompose.layouts

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.minesweepercompose.ui.theme.GreyBorder

fun Modifier.drawBorder(
    weight: Dp = 4.dp,
    colorTop: Color = GreyBorder,
    colorBottom: Color = Color.White,
    colorRight: Color = Color.White,
    colorLeft: Color = GreyBorder
) =
    this.drawBehind {
        val strokeWidth = weight.toPx()
        val halfStrokeWidth = strokeWidth / 2

        // Top border
        drawLine(
            color = colorTop,
            start = Offset(halfStrokeWidth, halfStrokeWidth),
            end = Offset(size.width - halfStrokeWidth, halfStrokeWidth),
            strokeWidth = strokeWidth
        )

        // Bottom border
        drawLine(
            color = colorBottom,
            start = Offset(halfStrokeWidth, size.height - halfStrokeWidth),
            end = Offset(size.width - halfStrokeWidth, size.height - halfStrokeWidth),
            strokeWidth = strokeWidth
        )

        // Left border
        drawLine(
            color = colorLeft,
            start = Offset(halfStrokeWidth, halfStrokeWidth),
            end = Offset(halfStrokeWidth, size.height - halfStrokeWidth),
            strokeWidth = strokeWidth
        )

        // Right border
        drawLine(
            color = colorRight,
            start = Offset(size.width - halfStrokeWidth, halfStrokeWidth),
            end = Offset(size.width - halfStrokeWidth, size.height - halfStrokeWidth),
            strokeWidth = strokeWidth
        )
    }