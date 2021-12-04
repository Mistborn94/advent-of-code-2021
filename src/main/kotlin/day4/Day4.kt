package day4

fun solveA(lines: List<String>): Int {
    val calledNumbers = lines.first().split(",").map { it.toInt() }.iterator()
    val boards = buildBoards(lines.subList(2, lines.size))

    return getWinningBoard(boards, calledNumbers).score()
}

private tailrec fun getWinningBoard(boards: List<Board>, calledNumbers: Iterator<Int>): Board {
    return if (boards.any { it.hasWon() }) {
        boards.first { it.hasWon() }
    } else {
        val calledNumber = calledNumbers.next()
        getWinningBoard(boards.onEach { it.mark(calledNumber) }, calledNumbers)
    }
}

fun solveB(lines: List<String>): Int {
    val calledNumbers = lines.first().split(",").map { it.toInt() }.iterator()
    val boards = buildBoards(lines.subList(2, lines.size)).toMutableList()

    return getLastBoard(boards, calledNumbers).score()
}

private tailrec fun getLastBoard(boards: List<Board>, calledNumbers: Iterator<Int>): Board {
    return if (boards.size == 1 && boards.first().hasWon()) {
        boards.first()
    } else {
        val calledNumber = calledNumbers.next()
        getLastBoard(boards.filter { !it.hasWon() }.onEach { it.mark(calledNumber) }, calledNumbers)
    }
}

private fun buildBoards(lines: List<String>) = lines
    .filter { it.isNotBlank() }
    .chunked(5)
    .map(::buildBoard)

fun buildBoard(lists: List<String>): Board {
    return lists.flatMapIndexed { y, row -> parseRow(row, y) }
        .toMap()
        .let(::Board)
}

private fun parseRow(row: String, y: Int) = row.chunked(3).mapIndexed { x, value -> Pair(value.trim().toInt(), BingoCell(x, y)) }

class Board(private val cells: Map<Int, BingoCell>) {
    private var lastMarked: Int = 0
    private val rowMarkCount = mutableMapOf<Int, Int>()
    private val columnMarkCount = mutableMapOf<Int, Int>()

    fun hasWon(): Boolean = rowMarkCount.any { it.value == 5 } || columnMarkCount.any { it.value == 5 }

    fun mark(number: Int) {
        cells[number]?.also {
            it.marked = true
            lastMarked = number
            rowMarkCount[it.y] = rowMarkCount.getOrDefault(it.y, 0) + 1
            columnMarkCount[it.x] = columnMarkCount.getOrDefault(it.x, 0) + 1
        }
    }

    fun score(): Int = cells.entries.filter { (_, value) -> !value.marked }
        .sumOf { (key, _) -> key } * lastMarked
}

data class BingoCell(val x: Int, val y: Int, var marked: Boolean = false)
