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

    val toVisit = PriorityQueue(listOf(ScoredPoint(start, 0, heuristic(start, end))))
    val seenPoints: MutableMap<Point, Int> = mutableMapOf(start to 0)

    while (!seenPoints.containsKey(end)) {
        val current = toVisit.remove()
        val nextPoints = current.point.neighbours()
            .filter { it in numbers && it !in seenPoints }
            .map { point -> ScoredPoint(point, current.score + numbers[point], heuristic(point, end)) }

        toVisit.addAll(nextPoints)
        seenPoints.putAll(nextPoints.associate { it.point to it.score })
    }

    return seenPoints[end]!!
}

data class ScoredPoint(val point: Point, val score: Int, val heuristic: Int) : Comparable<ScoredPoint> {
    override fun compareTo(other: ScoredPoint): Int = (score + heuristic).compareTo(other.score + heuristic)
}

private fun heuristic(current: Point, end: Point) = (end - current).abs()

private fun parseNumbers(lines: List<String>): NumberGrid = lines.map { it.toCharArray().map { c -> c.digitToInt() } }
private fun increaseByOne(nums: NumberGrid) = nums.map { line -> line.map { it.mod(9) + 1 } }
private fun combineHorizontal(blocks: List<NumberGrid>) = blocks.first().indices.map { row -> createRow(row, blocks) }
private fun createRow(rowIndex: Int, blocks: List<NumberGrid>) = blocks.map { it[rowIndex] }.reduce { acc, list -> acc + list }
private fun combineVertical(rows: List<NumberGrid>) = rows.reduce(NumberGrid::plus)
