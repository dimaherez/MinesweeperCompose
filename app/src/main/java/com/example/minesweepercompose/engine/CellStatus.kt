package com.example.minesweepercompose.engine

enum class CellStatus() {
    EMPTY,
    M1, M2, M3, M4, M5, M6, M7, M8,
    MINE,
    FLAG,
    EXPLORED
}

val CellStatus.isNumber
    get() =
        this.ordinal in 1..8

val CellStatus.isMine
    get() =
        this.ordinal == 9

val CellStatus.isEmpty
    get() =
        this.ordinal == 0