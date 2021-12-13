package day13

import helper.point.Point

val foldPattern = """fold along ([xy])=(\d+)""".toRegex()

fun solveA(lines: List<String>): Int {
    val blank = lines.indexOf("");
    val points = lines.subList(0, blank)
        .map { it.split(",") }
        .map { (x, y) -> Point(x.toInt(), y.toInt()) }
        .toSet()

    val fold = lines[blank + 1].let {
        val (axis, value) = foldPattern.matchEntire(it)!!.destructured
        Fold(axis[0], value.toInt())
    }

    return fold.foldPoints(points).size
}

fun solveB(lines: List<String>): String {
    val blank = lines.indexOf("")
    val initialPoints = lines.subList(0, blank)
        .map { it.split(",") }
        .map { (x, y) -> Point(x.toInt(), y.toInt()) }
        .toSet()

    val folds = lines.subList(blank + 1, lines.size).map {
        val (axis, value) = foldPattern.matchEntire(it)!!.destructured
        Fold(axis[0], value.toInt())
    }

    val finalPoints = folds.fold(initialPoints) { points, fold -> fold.foldPoints(points) }

    return printPoints(finalPoints)
}

private fun printPoints(finalPoints: Set<Point>): String {
    val maxX = finalPoints.maxOf { it.x }
    val maxY = finalPoints.maxOf { it.y }

    val message = Array(maxY + 1) {
        CharArray(maxX + 1) { ' ' }
    }

    finalPoints.forEach { (x, y) -> message[y][x] = '#' }
    return message.joinToString(prefix = "\n", separator = "\n", postfix = "\n") {
        it.joinToString(separator = "")
    }
}

data class Fold(val axis: Char, val value: Int) {

    fun foldPoints(points: Set<Point>): Set<Point> {
        return if (axis == 'x') {
            points.map { this.foldLeft(it) }.toSet()
        } else {
            points.map { this.foldUp(it) }.toSet()
        }
    }

    private fun foldUp(point: Point): Point {
        return if (point.y <= value) {
            point
        } else {
            Point(point.x, 2 * value - point.y)
        }
    }

    private fun foldLeft(point: Point): Point {
        return if (point.x <= value) {
            point
        } else {
            Point(2 * value - point.x, point.y)
        }
    }
}