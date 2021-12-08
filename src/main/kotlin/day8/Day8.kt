package day8

import helper.digitsToInt
import helper.pivot

/**
 *   0:      1:      2:      3:      4:
 *   aaaa    ....    aaaa    aaaa    ....
 *  b    c  .    c  .    c  .    c  b    c
 *  b    c  .    c  .    c  .    c  b    c
 *  ....    ....    dddd    dddd    dddd
 *  e    f  .    f  e    .  .    f  .    f
 *  e    f  .    f  e    .  .    f  .    f
 *   gggg    ....    gggg    gggg    ....
 *
 *  5:      6:      7:      8:      9:
 *  aaaa    aaaa    aaaa    aaaa    aaaa
 * b    .  b    .  .    c  b    c  b    c
 * b    .  b    .  .    c  b    c  b    c
 * dddd    dddd    ....    dddd    dddd
 * .    f  e    f  .    f  e    f  .    f
 * .    f  e    f  .    f  e    f  .    f
 *  gggg    gggg    ....    gggg    gggg
 */

fun solveA(lines: List<String>): Int {
    return lines.map { it.split(" | ") }
        .flatMap { (_, output) -> output.split(" ") }
        .count { it.length in setOf(2, 3, 4, 7) }
}

fun solveB(lines: List<String>): Int {
    return lines
        .asSequence()
        .map { it.split(" | ") }
        .map { (observedValues, outputValues) -> observedValues.split(" ") to outputValues.split(" ") }
        .sumOf { (observedValues, outputValues) -> solveOutputValues(observedValues.map { it.toSet() }, outputValues.map { it.toSet() }) }
}

fun solveOutputValues(observedValues: List<Set<Char>>, outputValues: List<Set<Char>>): Int {
    val uniqueLengthNumbers = mapOf(1 to 2, 4 to 4, 7 to 3, 8 to 7)
    val observedValuesByLength = observedValues.groupBy { it.size }
    val knownPatterns = mutableMapOf<Int, Set<Char>>()

    uniqueLengthNumbers.forEach { (number, length) ->
        val observedSegment = observedValuesByLength[length]?.first()
        if (observedSegment != null) {
            knownPatterns[number] = observedSegment
        }
    }

    observedValuesByLength[5]?.forEach {
        val pattern7 = knownPatterns[7]!!
        val number = when {
            it.containsAll(pattern7) -> 3
            it.union(knownPatterns[4]!!).size == 7 -> 2
            else -> 5
        }
        knownPatterns[number] = it
    }

    observedValuesByLength[6]?.forEach {
        val pattern1 = knownPatterns[1]!!
        val pattern4 = knownPatterns[4]!!

        val number = when {
            it.containsAll(pattern4) -> 9
            it.containsAll(pattern1) -> 0
            else -> 6
        }

        knownPatterns[number] = it
    }

    val mappings = knownPatterns.pivot()
    return outputValues.map { mappings[it]!! }.digitsToInt(10)
}

