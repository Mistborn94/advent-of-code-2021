package day4

fun solveA(lines: List<String>): Int {
    val calledNumbers = lines.first().split(",").map { it.toInt() }.iterator()
    val boards = buildBoards(lines.subList(2, lines.size))

    var call = 0
    while (!boards.any { it.hasWon() }) {
        call = calledNumbers.next()
        boards.forEach { board -> board.mark(call) }
    }

    return boards.first { it.hasWon() }.score() * call
}

fun solveB(lines: List<String>): Int {
    val calledNumbers = lines.first().split(",").map { it.toInt() }.iterator()
    val boards = buildBoards(lines.subList(2, lines.size)).toMutableList()

    var call = 0
    while (boards.size > 1) {
        call = calledNumbers.next()
        boards.forEach { board -> board.mark(call) }
        boards.removeIf { it.hasWon() }
    }

    val lastBoard = boards.first()
    while (!lastBoard.hasWon()) {
        call = calledNumbers.next();
        lastBoard.mark(call)
    }

    return lastBoard.score() * call
}

private fun buildBoards(lines: List<String>) = lines
    .filter { it.isNotBlank() }
    .chunked(5)
    .map(::buildBoard)

fun buildBoard(lists: List<String>): Board {
    return lists.flatMapIndexed { y, row ->
        row.trim().split("  ", " ").mapIndexed { x, value -> Pair(value.toInt(), BingoCell(x, y)) }
    }.toMap().let(::Board)
}

class Board(private val cells: Map<Int, BingoCell>) {

    private val rowMarkCount = mutableMapOf<Int, Int>()
    private val columnMarkCount = mutableMapOf<Int, Int>()

    fun hasWon(): Boolean = rowMarkCount.any { it.value == 5 } || columnMarkCount.any { it.value == 5 }

    fun mark(number: Int) {
        cells[number]?.also {
            it.marked = true
            rowMarkCount[it.y] = rowMarkCount.getOrDefault(it.y, 0) + 1
            columnMarkCount[it.x] = columnMarkCount.getOrDefault(it.x, 0) + 1
        }
    }

    fun score(): Int = cells.entries.filter { (_, value) -> !value.marked }
        .sumOf { (key, _) -> key }
}

class BingoCell(val x: Int, val y: Int, var marked: Boolean = false)
