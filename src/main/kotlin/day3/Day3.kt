package day3

fun solveA(text: String): Int {
    val lines = text.lines()
    val lineCount = lines.size
    val oneCounts = countOnes(lines)

    val gamma = oneCounts.map { if (it >= lineCount / 2) 1 else 0 }.joinToString(separator = "").toInt(2)
    val epsilon = oneCounts.map { if (it >= lineCount / 2) 0 else 1 }.joinToString(separator = "").toInt(2)

    return gamma * epsilon
}

private fun countOnes(lines: List<String>): Array<Int> {
    val oneCounts = Array(lines[0].length) { 0 }

    lines.forEach { line ->
        line.forEachIndexed { index, c ->
            oneCounts[index] += c.digitToInt()
        }
    }
    return oneCounts
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
