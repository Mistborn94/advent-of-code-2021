package day17

import helper.point.Point
import kotlin.math.abs
import kotlin.math.max

val pattern = """target area: x=(\d+)..(\d+), y=(-?\d+)..(-?\d+)""".toRegex()
fun solveA(line: String): Int {
    val (_, _, y1, y2) = parsePattern(line)

    val targetY = y1..y2

    val yVelocityUpwards = 0 until 500

    return yVelocityUpwards.asSequence()
        .map { solveY(Point(0, it), targetY) }
        .filter { (landed, _) -> landed }
        .maxOf { (_, highY) -> highY }
}

fun solveY(startVelocity: Point, targetY: IntRange): Pair<Boolean, Int> {
    var highY = 0
    var currentPosition = Point.ZERO
    var currentVelocity = startVelocity

    while (currentPosition.y > targetY.last) {
        currentPosition += currentVelocity
        currentVelocity += Point(0, -1)
        highY = max(highY, currentPosition.y)
    }

    return (currentPosition.y in targetY) to highY
}

fun solveBoth(startVelocity: Point, targetX: IntRange, targetY: IntRange): Boolean {
    var currentPosition = Point.ZERO
    var currentVelocity = startVelocity

    //x target is positive and x keeps increasing
    //y target is negative and y keeps decreasing
    while (!(currentPosition.x in targetX && currentPosition.y in targetY)
        && (currentPosition.x < targetX.last && currentPosition.y > targetY.first)
    ) {
        currentPosition += currentVelocity

        currentVelocity += if (currentVelocity.x > 0) {
            Point(-1, -1)
        } else if (currentVelocity.x < 0) {
            Point(1, -1)
        } else {
            Point(0, -1)
        }
    }

    return currentPosition.x in targetX && currentPosition.y in targetY
}


fun solveB(line: String): Int {
    val (x1, x2, y1, y2) = parsePattern(line)

    val targetX = x1..x2
    val targetY = y1..y2

    val ySearchRange = y1..abs(y1)
    val xSearchRange = 0..x2

    return ySearchRange.asSequence()
        .filter { solveY(Point(0, it), targetY).first }
        .flatMap { y -> xSearchRange.map { x -> Point(x, y) } }
        .filter { solveBoth(it, targetX, targetY) }
        .count()
}

private fun parsePattern(line: String) = pattern.matchEntire(line)!!
    .groupValues
    .drop(1)
    .map { it.toInt() }
