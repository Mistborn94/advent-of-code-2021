package day9

import helper.point.Point
import helper.point.contains
import helper.point.get
import helper.point.points
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
    return cells.points()
        .filter { point ->
            point.neighbours().filter { it in cells }
                .all { cells[it] > cells[point] }
        }
}

fun findBasin(point: Point, cells: List<List<Int>>): Set<Point> {
    val neighbours = point.neighbours().filter { it in cells && cells[it] != 9 && cells[it] > cells[point] }
    return mutableSetOf(point).apply {
        addAll(neighbours.flatMap { findBasin(it, cells) })
    }
}
