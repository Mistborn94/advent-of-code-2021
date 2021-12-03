package day3

import kotlin.math.pow

fun solveA(lines: List<String>): Int {
    return lines
        .map { line -> line.map(Char::digitToInt) }
        .reduce(List<Int>::sumWith)
        .map { if (it >= lines.size - it) 1 else 0 }
        .digitsToInt(2)
        .let { gamma -> gamma * (gamma.inv() and bitmask(lines.first().length)) }
}

fun solveB(lines: List<String>): Int = lines
    .map { line -> line.map(Char::digitToInt) }
    .run { calculateRating(1) * calculateRating(0) }

private tailrec fun List<List<Int>>.calculateRating(highBit: Int, index: Int = 0): Int {
    return if (size == 1) {
        first().digitsToInt(2)
    } else {
        val oneCounts = sumOf { it[index] }
        val comparisonBit = if (oneCounts >= size - oneCounts) highBit else 1 - highBit
        this.filter { it[index] == comparisonBit }.calculateRating(highBit, index + 1)
    }
}

fun List<Int>.sumWith(other: List<Int>) = zip(other, Int::plus)
fun List<Int>.digitsToInt(radix: Int) = reduce { acc, digit -> acc * radix + digit }
fun Int.pow(n: Int) = this.toDouble().pow(n).toInt()
fun bitmask(size: Int) = 2.pow(size) - 1
