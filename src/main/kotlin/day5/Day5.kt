package day5

import helper.point.Point

fun solveA(lines: List<String>): Int = solve(lines, false)
fun solveB(lines: List<String>): Int = solve(lines, true)

private fun solve(lines: List<String>, withDiagonals: Boolean) = lines
    .map { line -> line.split(" -> ", ",").map { it.toInt() } }
    .map { (a, b, c, d) -> Point(a, b) to Point(c, d) }
    .flatMap { (pointA, pointB) -> pointsBetween(pointA, pointB, withDiagonals) }
    .groupingBy { it }
    .eachCount()
    .count { (_, value) -> value > 1 }

fun pointsBetween(pointA: Point, pointB: Point, withDiagonals: Boolean): List<Point> {
    return if (pointA.x == pointB.x) {
        yRange(pointA, pointB).map { y -> Point(pointA.x, y) }
    } else if (pointA.y == pointB.y) {
        xRange(pointA, pointB).map { x -> Point(x, pointA.y) }
    } else if (withDiagonals) {
        val yRange = yRange(pointA, pointB)
        val xRange = xRange(pointA, pointB)
        xRange.zip(yRange).map { (x, y) -> Point(x, y) }
    } else {
        emptyList()
    }
}

private fun xRange(pointA: Point, pointB: Point): IntProgression = makeRange(pointA.x, pointB.x)
private fun yRange(pointA: Point, pointB: Point): IntProgression = makeRange(pointA.y, pointB.y)

private fun makeRange(a: Int, b: Int) = if (a < b) {
    a..b
} else {
    a downTo b
}

