package day10

import java.util.*

val pointsA = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)
val pointsB = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4
)

val matching = mapOf(
    '(' to ')',
    '<' to '>',
    '{' to '}',
    '[' to ']'
)
val opening = setOf('(', '{', '<', '[')
val closing = setOf(')', '}', '>', ']')

fun solveA(lines: List<String>): Int {
    return lines.mapNotNull { firstIllegalChar(it) }
        .sumOf { pointsA[it] ?: 0 }
}


fun firstIllegalChar(line: String): Char? {
    val stack = Stack<Char>()

    for (it in line) {
        if (it in opening) {
            stack.push(it)
        } else if (it in closing) {
            val lastOpen = stack.pop()
            if (it != matching[lastOpen]) {
                return it
            }
        }
    }
    return null
}

fun completeLine(line: String): String? {
    val stack = Stack<Char>()

    for (it in line) {
        if (it in opening) {
            stack.push(it)
        } else if (it in closing) {
            val lastOpen = stack.pop()
            if (it != matching[lastOpen]) {
                return null
            }
        }
    }
    return stack.mapNotNull { matching[it] }.reversed().joinToString(separator = "")
}


fun solveB(lines: List<String>): Long {
    val allScores = lines.mapNotNull { completeLine(it) }
        .map { calcPoints(it) }
        .sorted()

    return allScores[allScores.size / 2]
}

fun calcPoints(it: String): Long = it.fold(0) { acc, c -> acc * 5 + (pointsB[c] ?: 0) }
