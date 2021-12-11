package day11

import helper.point.Point
import helper.point.contains
import helper.point.get
import helper.point.set


fun solveA(lines: List<String>): Int {
    val octopuses = lines.map { line -> line.toCharArray().map { it.digitToInt() }.toMutableList() }

    return (0 until 100).sumOf { runIteration(octopuses) }
}

private fun runIteration(octopuses: List<MutableList<Int>>): Int {
    val toFlash = mutableSetOf<Point>()
    val flashed = mutableSetOf<Point>()

    for (x in 0 until 10) {
        for (y in 0 until 10) {
            val point = Point(x, y)
            increaseValue(octopuses, point, toFlash)
        }
    }

    while (toFlash.isNotEmpty()) {
        val point = toFlash.first()
        toFlash.remove(point)
        if (flashed.add(point)) {
            (point.neighbours() + point.diagonalNeighbours())
                .filter { it in octopuses }
                .forEach { neighbour -> increaseValue(octopuses, neighbour, toFlash) }

        }
    }
    flashed.forEach { point ->
        octopuses[point] = 0
    }
    return flashed.size
}


fun solveB(lines: List<String>): Int {
    val octopuses = lines.map { line -> line.toCharArray().map { it.digitToInt() }.toMutableList() }
    var step = 0

    do {
        step += 1
        val flashCount = runIteration(octopuses)
    } while (flashCount != 100)

    return step
}

fun increaseValue(octopuses: List<MutableList<Int>>, point: Point, toFlash: MutableSet<Point>) {
    octopuses[point] += 1
    if (octopuses[point] > 9) {
        toFlash.add(point)
    }
}
