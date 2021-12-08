package day8

import day8.Segment.*
import helper.digitsToInt
import helper.pivot
import java.util.*

enum class Segment { A, B, C, D, E, F, G }

val uniqueLengthNumbers = setOf(1, 4, 7, 8)
val numberToSegmentMapping = mapOf(
    0 to setOf(A, B, C, E, F, G),
    1 to setOf(C, F),
    2 to setOf(A, C, D, E, G),
    3 to setOf(A, C, D, F, G),
    4 to setOf(B, C, D, F),
    5 to setOf(A, B, D, F, G),
    6 to setOf(A, B, D, E, F, G),
    7 to setOf(A, C, F),
    8 to setOf(A, B, C, D, E, F, G),
    9 to setOf(A, B, C, D, F, G)
)
val segmentToNumberMapping = numberToSegmentMapping.pivot()

fun solveA(lines: List<String>): Int {
    val segmentLengths = numberToSegmentMapping.mapValues { (_, value) -> value.size }
    return lines.map { it.split(" | ") }
        .flatMap { (_, output) -> output.split(" ") }
        .count { it.length in setOf(segmentLengths[4], segmentLengths[1], segmentLengths[7], segmentLengths[8]) }
}

fun solveB(lines: List<String>): Int {
    return lines.map { it.split(" | ") }
        .map { (observedValues, outputValues) -> observedValues.split(" ") to outputValues.split(" ") }
        .sumOf { (observedValues, outputValues) -> solveOutputValues(observedValues.map { it.toSegments() }, outputValues.map { it.toSegments() }) }
}

fun solveOutputValues(observedValues: List<Set<Segment>>, outputValues: List<Set<Segment>>): Int {
    val possibleSegmentMappings = values().associateWith { values().toMutableList() }

    val observedValuesByLength = observedValues.groupBy { it.size }
    val knownPatterns = mutableMapOf<Int, Set<Segment>>()

    uniqueLengthNumbers.forEach { number ->
        val expectedSegment = numberToSegmentMapping[number]!!
        val observedSegment = observedValuesByLength[expectedSegment.size]?.first()
        if (observedSegment != null) {
            knownPatterns[number] = observedSegment
            handleSolvedNumber(possibleSegmentMappings, expectedSegment, observedSegment)
        }
    }

    return makePossibleMappings(possibleSegmentMappings)
        .first { mapping -> solvesAllNumbers(observedValues + outputValues, mapping) }
        .let { mapping -> calculateOutputValue(outputValues, mapping) }
}

private fun calculateOutputValue(outputValues: List<Set<Segment>>, mapping: Map<Segment, Segment>) = outputValues.map { displaySegment -> convertMapping(displaySegment, mapping) }
    .map { segmentToNumberMapping[it]!! }
    .digitsToInt(10)

fun convertMapping(displaySegment: Set<Segment>, mapping: Map<Segment, Segment>) = displaySegment.map { mapping[it]!! }.toSet()
fun solvesAllNumbers(actualSegments: List<Set<Segment>>, mapping: Map<Segment, Segment>): Boolean = actualSegments.all { convertMapping(it, mapping) in segmentToNumberMapping }

fun makePossibleMappings(possibleMappings: Map<Segment, List<Segment>>, current: Map<Segment, Segment> = emptyMap()): List<Map<Segment, Segment>> {
    return if (possibleMappings.isEmpty()) {
        listOf(current)
    } else {
        val (key, values) = possibleMappings.entries.first()
        val newPossible = possibleMappings.toMutableMap()
        val newCurrent = current.toMutableMap()
        newPossible.remove(key)
        values.filter { it !in current.values }
            .flatMap { value ->
                newCurrent[key] = value
                makePossibleMappings(newPossible, newCurrent)
            }
    }
}

private fun handleSolvedNumber(possibleSegmentMappings: Map<Segment, MutableList<Segment>>, expectedSegments: Set<Segment>, observedSegments: Set<Segment>) {
    possibleSegmentMappings.forEach { (key, value) ->
        if (key in observedSegments) {
            value.removeIf { it !in expectedSegments }
        } else {
            value.removeIf { it in expectedSegments }
        }
    }
}

private fun String.toSegments() = uppercase(Locale.getDefault()).map { valueOf(it.toString()) }.toSet()
