package day15

import helper.point.Point
import helper.point.contains
import helper.point.get
import java.util.*

typealias NumberGrid = List<List<Int>>

fun solveA(lines: List<String>): Int {
    val nums = parseNumbers(lines)
    return search(nums)
}

fun solveB(lines: List<String>): Int {

    val inputNumbers = parseNumbers(lines)

    val firstRowBlocks = (0 until 4).runningFold(inputNumbers) { acc, _ -> increaseByOne(acc) }
    val firstRow = combineHorizontal(firstRowBlocks)

    val rows = (0 until 4).runningFold(firstRow) { acc, _ -> increaseByOne(acc) }
    val finalNumbers = combineVertical(rows)

    return search(finalNumbers)
}

private fun search(numbers: NumberGrid): Int {
    val start = Point(0, 0)
    val end = Point(numbers[0].lastIndex, numbers.lastIndex)
    val paths = PriorityQueue<Path>()
    var current = Path(0, start, (end - start).abs())

    val bestPaths: MutableMap<Point, Int> = mutableMapOf(start to 0)
    while (current.end != end) {
        val newPaths = current.end.neighbours()
            .filter { it in numbers }
            .map { point -> buildNewPath(current, numbers, point, end) }
            .filter { it.score < (bestPaths[it.end] ?: Int.MAX_VALUE) }

        paths.addAll(newPaths)
        bestPaths.putAll(newPaths.associate { it.end to it.score })
        current = paths.remove()
    }

    return current.score
}

data class Path(val score: Int, val end: Point, val heuristic: Int) : Comparable<Path> {
    override fun compareTo(other: Path): Int = (score + heuristic).compareTo(other.score + heuristic)
}

private fun buildNewPath(current: Path, numbers: NumberGrid, next: Point, end: Point) =
    Path(current.score + numbers[next], next, (end - next).abs())

private fun parseNumbers(lines: List<String>): NumberGrid = lines.map { it.toCharArray().map { c -> c.digitToInt() } }
private fun increaseByOne(nums: NumberGrid) = nums.map { line -> line.map { (it.mod(9)) + 1 } }
private fun combineHorizontal(blocks: List<NumberGrid>) = blocks.first().indices.map { row -> createRow(row, blocks) }
private fun createRow(rowIndex: Int, blocks: List<NumberGrid>) = blocks.map { it[rowIndex] }.reduce { acc, list -> acc + list }
private fun combineVertical(rows: List<NumberGrid>) = rows.reduce(NumberGrid::plus)
