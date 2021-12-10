package day2

import helper.point.Point

fun solveA(text: String): Int {
    return parseInput(text)
        .fold(Point(0, 0)) { (x, y), (direction, distance) ->
            when (direction) {
                "forward" -> Point(x + distance.toInt(), y)
                "down" -> Point(x, y + distance.toInt())
                "up" -> Point(x, y - distance.toInt())
                else -> throw IllegalArgumentException("Bad input $direction")
            }
        }.let { (x, y) -> x * y }
}

fun solveB(text: String): Int {
    return parseInput(text)
        .fold(SubmarinePosition(0, 0, 0)) { (x, y, aim), (direction, distance) ->
            when (direction) {
                "forward" -> SubmarinePosition(x + distance.toInt(), y + aim * distance.toInt(), aim)
                "down" -> SubmarinePosition(x, y, aim + distance.toInt())
                "up" -> SubmarinePosition(x, y, aim - distance.toInt())
                else -> throw IllegalArgumentException("Bad input $direction")
            }
        }.let { (x, y, _) -> x * y }
}

data class SubmarinePosition(val x: Int, val y: Int, val aim: Int)

private fun parseInput(text: String) = text
    .lines()
    .map { it.split(" ") }
