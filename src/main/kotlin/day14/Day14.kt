package day14

import helper.minAndMax

fun solve(lines: List<String>, iterations: Int): Long {
    val template = lines.first()
    val rules = lines.subList(2, lines.size)
        .map { it.split(" -> ") }
        .map { (pattern, insert) -> InsertionRule(pattern[0], pattern[1], insert[0]) }
        .associateBy { Pair(it.first, it.second) }

    val initialPairs = template.zipWithNext()
        .groupingBy { it }
        .eachCount()
        .mapValues { (_, value) -> value.toLong() }

    val polymerPairs = (0 until iterations).fold(initialPairs) { pairs, _ ->
        buildMap {
            pairs.forEach { (key, count) ->
                val rule = rules[key]
                if (rule == null) {
                    incrementValue(this, key, count)
                } else {
                    incrementValue(this, rule.first to rule.insert, count)
                    incrementValue(this, rule.insert to rule.second, count)
                }
            }
        }
    }

    val counts = buildMap<Char, Long> {
        put(template.first(), 1)
        put(template.last(), 1)
        polymerPairs.forEach { (pair, count) ->
            incrementValue(this, pair.first, count)
            incrementValue(this, pair.second, count)
        }
    }.mapValues { (_, value) -> value / 2 }

    val (min, max) = counts.values.minAndMax()

    return max - min
}

private fun <K> incrementValue(map: MutableMap<K, Long>, key: K, count: Long) {
    map[key] = (map[key] ?: 0) + count
}

data class InsertionRule(val first: Char, val second: Char, val insert: Char) {
    override fun toString(): String = "$first$second -> $insert"
}