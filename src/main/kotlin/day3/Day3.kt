package day3

import helper.digitsToInt
import kotlin.math.pow

fun solveA(lines: List<String>): Int {
    val gamma = lines
        .map { line -> line.map(Char::digitToInt) }
        .reduce(List<Int>::sumWith)
        .map { if (it >= lines.size - it) 1 else 0 }
        .digitsToInt(2)

    return gamma * (gamma.inv() and bitmask(lines.first().length))
}

fun solveB(lines: List<String>): Int {
    val lineDigits = lines.map { line -> line.map(Char::digitToInt) }

    return calculateRating(lineDigits, 1) * calculateRating(lineDigits, 0)
}

private tailrec fun calculateRating(lists: List<List<Int>>, highBit: Int, index: Int = 0): Int {
    return if (lists.size == 1) {
        lists.first().digitsToInt(2)
    } else {
        val oneCounts = lists.sumOf { it[index] }
        val comparisonBit = if (oneCounts >= lists.size - oneCounts) highBit else 1 - highBit
        calculateRating(lists.filter { it[index] == comparisonBit }, highBit, index + 1)
    }
}

fun List<Int>.sumWith(other: List<Int>) = zip(other, Int::plus)
fun Int.pow(n: Int) = this.toDouble().pow(n).toInt()
fun bitmask(size: Int) = 2.pow(size) - 1
