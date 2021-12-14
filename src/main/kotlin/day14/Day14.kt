package day14

import helper.minAndMax

fun solve(lines: List<String>, steps: Int): Long {
    val template = lines.first()
    val rules = lines.subList(2, lines.size)
        .map { it.split(" -> ") }
        .map { (pattern, insert) -> InsertionRule(pattern[0], pattern[1], insert[0]) }

    val runningCounts = template.groupingBy { it }
        .eachCount()
        .mapValues { (_, value) -> value.toLong() }
        .toMutableMap()

    val pairs = template.zipWithNext()
        .groupingBy { it }
        .eachCount()
        .mapValues { (_, value) -> value.toLong() }
        .toMutableMap()

    repeat(steps) {
        rules.map { rule -> rule to (pairs[rule.key] ?: 0) }
            .filter { (_, applyCount) -> applyCount > 0 }
            .forEach { (rule, applyCount) ->
                runningCounts.compute(rule.insert) { _, value -> (value ?: 0) + applyCount }
                pairs.compute(rule.key) { _, value -> (value ?: 0) - applyCount }
                pairs.compute(rule.patternA to rule.insert) { _, value -> (value ?: 0) + applyCount }
                pairs.compute(rule.insert to rule.patternB) { _, value -> (value ?: 0) + applyCount }
            }
    }

    val (min, max) = runningCounts.values.minAndMax()

    return max - min
}

data class InsertionRule(val patternA: Char, val patternB: Char, val insert: Char) {
    val key = Pair(patternA, patternB)

    override fun toString(): String {
        return "$patternA$patternB -> $insert"
    }
}