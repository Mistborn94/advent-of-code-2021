package day9

import helper.Point
import helper.get
import helper.product

fun solveA(lines: List<String>): Int {
    val cells = lines.map { it.toCharArray().map { digit -> digit.digitToInt() } }

    return findLowPoints(cells).sumOf { cells[it] + 1 }
}

fun solveB(lines: List<String>): Int {
    val cells = lines.map { it.toCharArray().map { digit -> digit.digitToInt() } }

    val lowPoints = findLowPoints(cells)

    val basins = lowPoints.map { point -> findBasin(point, cells).size }

    return basins.sortedDescending().take(3).product()
}

private fun findLowPoints(cells: List<List<Int>>): List<Point> {
    val xRange = cells[0].indices
    val yRange = cells.indices
    return xRange.flatMap { x -> yRange.map { y -> Point(x, y) } }
        .filter { point ->
            point.neighbours().filter { it.x in xRange && it.y in yRange }
                .all { cells[it] > cells[point] }
        }
}

fun findBasin(point: Point, cells: List<List<Int>>): Set<Point> {
    val xRange = cells[0].indices
    val yRange = cells.indices
    val neighbours = point.neighbours().filter { it.x in xRange && it.y in yRange && cells[it] != 9 && cells[it] > cells[point] }
    return mutableSetOf(point).apply {
        addAll(neighbours.flatMap { findBasin(it, cells) })
    }
}
