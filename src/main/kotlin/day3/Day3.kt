package day3

import kotlin.math.pow

fun solveA(text: String): Int {
    val lines = text.lines()
    val lineCount = lines.size
    val oneCounts = countOnes(lines)

    val mask = 2.pow(lines[0].length) - 1
    val gamma = oneCounts.map { if (it >= lineCount - it) 1 else 0 }.reduce { acc, value -> acc * 2 + value }
    val epsilon = gamma.inv() and mask

    return gamma * epsilon
}

private fun countOnes(lines: List<String>): List<Int> {
    return lines.map { line -> line.map { it.digitToInt() } }
        .reduce { acc, line ->
            acc.zip(line) { a, b -> a + b }
        }
}

fun solveB(text: String): Int {
    val lines = text.lines()
    val oxygenRating = findCandidate(lines, '1', '0')
    val co2Rating = findCandidate(lines, '0', '1')

    return oxygenRating * co2Rating
}

private fun findCandidate(lines: List<String>, highBit: Char, lowBit: Char): Int {
    val candidates = lines.toMutableList()
    var index = 0
    while (candidates.size > 1) {
        val oneCounts = candidates.sumOf { it[index].digitToInt() }
        val comparisonBit = if (oneCounts >= candidates.size - oneCounts) highBit else lowBit
        candidates.removeIf { it[index] != comparisonBit }
        index += 1
    }
    return candidates[0].toInt(2)
}

fun Int.pow(n: Int) = this.toDouble().pow(n).toInt()
