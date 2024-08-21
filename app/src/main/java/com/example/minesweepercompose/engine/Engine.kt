package com.example.minesweepercompose.engine

import java.util.Stack

typealias Field = List<List<CellStatus>>

interface MinesweeperEngine {
    fun generateField(): Field
    fun isWin(maskedField: Field): Boolean
    fun isLost(maskedField: Field): Boolean
    fun gameInProgress(maskedField: Field): Boolean
    fun Field.countFlags(): Int
    fun handleCommand(maskedField: Field, ix: Int, command: Commands): Field

    companion object : MinesweeperEngine {
        private var actualField = getActualField()

        const val DIMENSION = 9
        const val MINES = 10

        override fun generateField(): Field {
            actualField = getActualField()
            return MutableList(DIMENSION * DIMENSION) { CellStatus.EMPTY }.chunked(DIMENSION)
        }

        override fun isWin(maskedField: Field): Boolean {
            for (row in 0 until DIMENSION) {
                for (col in 0 until DIMENSION) {
                    if (maskedField[row][col] == CellStatus.FLAG && actualField[row][col] != CellStatus.MINE) return false
                    if (maskedField[row][col] != CellStatus.FLAG && actualField[row][col] == CellStatus.MINE) return false
                }
            }
            return true
        }

        override fun isLost(maskedField: Field): Boolean {
            return maskedField.flatten().contains(CellStatus.MINE)
        }

        override fun gameInProgress(maskedField: Field): Boolean =
            !isWin(maskedField) && !isLost(maskedField)


        override fun Field.countFlags(): Int =
            this.flatten().count { it == CellStatus.FLAG }

        override fun handleCommand(maskedField: Field, ix: Int, command: Commands): Field {
            val coordinates = ix.toCoordinates()
            return when (command) {
                Commands.FREE -> handleFreeCommand(maskedField, coordinates)
                Commands.MINE -> maskedField.toggleFlag(coordinates)
            }
        }

        private fun handleFreeCommand(maskedField: Field, coordinates: Coordinates): Field {
            val cellActual = actualField[coordinates]
            val cellMasked = maskedField[coordinates]
            return when {
                cellMasked == CellStatus.FLAG -> maskedField

                cellActual == CellStatus.MINE -> revealMines(maskedField)
                cellActual.isNumber -> maskedField.updated(coordinates, cellActual)
                cellActual == CellStatus.EMPTY -> maskedField.explore(actualField, coordinates)
                else -> maskedField
            }
        }

        private fun Field.toggleFlag(coordinates: Coordinates): Field {
            val newCell = when (val currentCell = this[coordinates]) {
                CellStatus.FLAG -> CellStatus.EMPTY
                CellStatus.EMPTY -> CellStatus.FLAG
                else -> currentCell
            }
            return this.updated(coordinates, newCell)
        }

        private fun Field.countMines(): Field {
            return asSequence().flatten().mapIndexed { index, cell ->
                if (cell == CellStatus.MINE) {
                    cell
                } else {
                    val n =
                        neighbours(index.toCoordinates()).count { this[it.row][it.col] == CellStatus.MINE }
                    CellStatus.entries[n]
                }
            }.chunked(DIMENSION).toList()
        }


        private fun placeMines(): Field {
            val field = MutableList(DIMENSION * DIMENSION) { CellStatus.EMPTY }
            field.subList(0, MINES).fill(CellStatus.MINE)
            return field.shuffled().chunked(DIMENSION)
        }


        private fun getActualField(): Field {
            val initField = placeMines()
            return initField.countMines()
        }


        private fun neighbours(coordinates: Coordinates) = sequence {
            val (row, col) = coordinates
            for (dr in -1..1) {
                for (dc in -1..1) {
                    if (dr == 0 && dc == 0) continue
                    if (row + dr in 0 until DIMENSION
                        && col + dc in 0 until DIMENSION
                    ) {
                        yield(Coordinates(row + dr, col + dc))
                    }
                }
            }
        }

        operator fun Field.get(coordinates: Coordinates) =
            this[coordinates.row][coordinates.col]

        private fun Field.updated(coordinates: Coordinates, newValue: CellStatus): Field =
            this.flatten().mapIndexed { index, cellStatus ->
                if (coordinates.asIndex == index) newValue else cellStatus
            }.chunked(DIMENSION)

        private fun revealMines(maskedField: Field) =
            maskedField.flatten()
                .mapIndexed { index, cellStatus ->
                    if (actualField[index.toCoordinates()] == CellStatus.MINE)
                        CellStatus.MINE else cellStatus
                }.chunked(DIMENSION)


        private fun Field.explore(actualField: Field, coordinates: Coordinates): Field {
            val stack = Stack<Coordinates>().apply { push(coordinates) }
            var updatedField = this

            while (!stack.isEmpty()) {
                stack.pop().let { current ->
                    if (updatedField[current] != CellStatus.EXPLORED) {
                        updatedField = updatedField.updated(
                            current,
                            actualField[current].takeIf { it.isNumber } ?: CellStatus.EXPLORED
                        )
                        if (actualField[current] in listOf(CellStatus.EMPTY, CellStatus.FLAG)) {
                            neighbours(current).forEach(stack::push)
                        }
                    }
                }
            }

            return updatedField
        }
    }
}
