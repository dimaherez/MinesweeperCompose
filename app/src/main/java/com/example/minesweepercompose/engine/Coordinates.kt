package com.example.minesweepercompose.engine

import com.example.minesweepercompose.engine.MinesweeperEngine.Companion.DIMENSION

data class Coordinates(val row: Int, val col: Int)

val Coordinates.asIndex get() = this.row * DIMENSION + this.col // Coordinates to flatten index

fun Int.toCoordinates() =
    Coordinates(this / DIMENSION, this % DIMENSION) // Index to Coordinates