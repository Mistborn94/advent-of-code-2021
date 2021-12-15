package day15

import helper.graph.findShortestPath
import helper.point.Point
import helper.point.contains
import helper.point.get

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
    val end = Point(numbers[0].lastIndex, numbers.lastIndex)
    val shortestPath = findShortestPath(
        start = Point(0, 0),
        end = end,

        neighbours = { point -> point.neighbours().filter { it in numbers } },
        cost = { _, next -> numbers[next] },
        heuristic = { point -> (end - point).abs() },
    )

    return shortestPath.getScore()
}

private fun parseNumbers(lines: List<String>): NumberGrid = lines.map { it.toCharArray().map { c -> c.digitToInt() } }
private fun increaseByOne(nums: NumberGrid) = nums.map { line -> line.map { it.mod(9) + 1 } }
private fun combineHorizontal(blocks: List<NumberGrid>) = blocks.first().indices.map { row -> createRow(row, blocks) }
private fun createRow(rowIndex: Int, blocks: List<NumberGrid>) = blocks.map { it[rowIndex] }.reduce { acc, list -> acc + list }
private fun combineVertical(rows: List<NumberGrid>) = rows.reduce(NumberGrid::plus)
