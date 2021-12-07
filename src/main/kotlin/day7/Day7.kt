package day7

import kotlin.math.abs

fun solveA(lines: String): Int {
    val positions = lines.split(",").map { it.toInt() }
    val minPos = positions.minOrNull()!!
    val maxPos = positions.maxOrNull()!!

    return (minPos..maxPos).minOf { calculateFuelA(it, positions) }
}

fun calculateFuelA(target: Int, positions: List<Int>): Int = positions.sumOf { abs(target - it) }

fun calculateFuelB(target: Int, positions: List<Int>): Int = positions.sumOf {
    val abs = abs(target - it)
    abs * (abs + 1) / 2
}

fun solveB(lines: String): Int {
    val positions = lines.split(",").map { it.toInt() }
    val minPos = positions.minOrNull()!!
    val maxPos = positions.maxOrNull()!!

    return (minPos..maxPos).minOf { calculateFuelB(it, positions) }
}
