package day4

import helper.Point

fun solveA(lines: List<String>): Int {
    val calledNumbers = lines.first().split(",").map { it.toInt() }.toMutableList()

    val boards = lines.subList(2, lines.size)
        .filter { it.isNotBlank() }
        .chunked(5)
        .map { lists ->
            buildBoard(lists)
        }

    var call: Int = 0
    while (!boards.any { it.hasWon() }) {
        call = calledNumbers.removeFirst();
        boards.forEach { board -> board[call]?.let { it.marked = true } }
    }

    val winningBoard = boards.first { it.hasWon() }
    val unmarkedSum = winningBoard.entries.filter { (key, value) -> !value.marked }
        .sumOf { (key, value) -> key }
    return unmarkedSum * call
}

fun solveB(lines: List<String>): Int {
    val calledNumbers = lines.first().split(",").map { it.toInt() }.toMutableList()

    val boards = lines.subList(2, lines.size)
        .filter { it.isNotBlank() }
        .chunked(5)
        .map { lists ->
            buildBoard(lists)
        }.toMutableList()

    var call: Int = 0
    while (boards.size > 1) {
        call = calledNumbers.removeFirst();
        boards.forEach { board -> board[call]?.let { it.marked = true } }
        boards.removeIf { it.hasWon() }
    }

    val lastBoard = boards.first()
    while (!lastBoard.hasWon()) {
        call = calledNumbers.removeFirst();
        lastBoard[call]?.let { it.marked = true }
    }

    val unmarkedSum = lastBoard.entries.filter { (key, value) -> !value.marked }
        .sumOf { (key, value) -> key }
    return unmarkedSum * call
}

private fun Map<Int, BingoCell>.hasWon(): Boolean {
    return this.entries.groupBy { (_, value) -> value.point.x }.any { (_, column) -> column.all { it.value.marked } }
            || this.entries.groupBy { (_, value) -> value.point.y }.any { (_, row) -> row.all { it.value.marked } }

}

fun buildBoard(lists: List<String>): Map<Int, BingoCell> {
    return lists.flatMapIndexed { y, row ->
        row.trim().split("  ", " ").mapIndexed { x, value -> Pair(value.toInt(), BingoCell(Point(x, y))) }
    }
        .toMap()
}

data class BingoCell(val point: Point, var marked: Boolean = false) {

}
