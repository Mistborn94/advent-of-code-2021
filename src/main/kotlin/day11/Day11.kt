package day11

import helper.point.*


fun solveA(lines: List<String>): Int {
    val octopuses = lines.map { line -> line.toCharArray().map { it.digitToInt() }.toMutableList() }

    return (0 until 100).sumOf { runIteration(octopuses) }
}

private fun runIteration(octopuses: List<MutableList<Int>>): Int {
    val toFlash = mutableSetOf<Point>()
    val flashed = mutableSetOf<Point>()

    fun increaseEnergy(point: Point) {
        octopuses[point] += 1
        if (octopuses[point] > 9) {
            toFlash.add(point)
        }
    }

    octopuses.points().forEach { increaseEnergy(it) }

    while (toFlash.isNotEmpty()) {
        val point = toFlash.first()
        toFlash.remove(point)
        if (flashed.add(point)) {
            (point.neighbours() + point.diagonalNeighbours())
                .filter { it in octopuses }
                .forEach { increaseEnergy(it) }
        }
    }

    return flashed.onEach { octopuses[it] = 0 }.size
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

