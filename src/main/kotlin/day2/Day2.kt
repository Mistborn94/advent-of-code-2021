package day2

import helper.Point

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
        .fold(Triple(0, 0, 0)) { (x, y, a), (direction, distance) ->
            when (direction) {
                "forward" -> Triple(x + distance.toInt(), y + a * distance.toInt(), a)
                "down" -> Triple(x, y, a + distance.toInt())
                "up" -> Triple(x, y, a - distance.toInt())
                else -> throw IllegalArgumentException("Bad input $direction")
            }
        }.let { (x, y, _) -> x * y }
}

private fun parseInput(text: String) = text
    .lines()
    .map { it.split(" ") }
