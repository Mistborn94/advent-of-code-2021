package day14

import helper.accumulateToMap
import helper.minAndMax

fun solve(lines: List<String>, iterations: Int): Long {
    val template = lines.first()
    val rules = lines.subList(2, lines.size)
        .map { it.split(" -> ") }
        .map { (pattern, insert) -> InsertionRule(pattern[0], pattern[1], insert[0]) }

    val pairs = template.zipWithNext()
        .groupingBy { it }
        .eachCount()
        .mapValues { (_, value) -> value.toLong() }
        .toMutableMap()

    repeat(iterations) {
        rules.map { rule -> rule to (pairs[rule.key] ?: 0) }
            .filter { (_, applyCount) -> applyCount > 0 }
            .forEach { (rule, applyCount) ->
                pairs.compute(rule.key) { _, value -> (value ?: 0) - applyCount }
                pairs.compute(rule.patternA to rule.insert) { _, value -> (value ?: 0) + applyCount }
                pairs.compute(rule.insert to rule.patternB) { _, value -> (value ?: 0) + applyCount }
            }
    }

    val destructuredPairs = pairs.flatMap { (key, value) -> listOf(key.first to value, key.second to value) }
    val counts = (destructuredPairs + (template.first() to 1L) + (template.last() to 1L))
        .accumulateToMap(0L) { acc, pair -> acc + pair.second }
        .mapValues { (_, value) -> value / 2 }

    val (min, max) = counts.values.minAndMax()

    return max - min
}

data class InsertionRule(val patternA: Char, val patternB: Char, val insert: Char) {
    val key = Pair(patternA, patternB)

    override fun toString(): String = "$patternA$patternB -> $insert"
}