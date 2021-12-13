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

    return processFold(points, fold).size
}

fun processFold(points: Set<Point>, fold: Fold): Set<Point> {
    return if (fold.axis == 'x') {
        points.map { foldLeft(it, fold) }.toSet()
    } else {
        points.map { foldUp(it, fold) }.toSet()
    }
}

private fun foldUp(it: Point, fold: Fold): Point {
    return if (it.y <= fold.value) {
        it
    } else {
        val newY = fold.value - (it.y - fold.value)
        Point(it.x, newY)
    }
}

private fun foldLeft(it: Point, fold: Fold): Point {
    return if (it.x <= fold.value) {
        it
    } else {
        val newX = fold.value - (it.x - fold.value)
        Point(newX, it.y)
    }
}

fun solveB(lines: List<String>): String {
    val blank = lines.indexOf("")
    val points = lines.subList(0, blank)
        .map { it.split(",") }
        .map { (x, y) -> Point(x.toInt(), y.toInt()) }
        .toSet()

    val folds = lines.subList(blank + 1, lines.size).map {
        val (axis, value) = foldPattern.matchEntire(it)!!.destructured
        Fold(axis[0], value.toInt())
    }

    val finalPoints = folds.fold(points) { points, fold -> processFold(points, fold) }

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

data class Fold(val axis: Char, val value: Int)