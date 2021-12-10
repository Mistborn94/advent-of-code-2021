package day10

import java.util.*

val pairs = mapOf(
    '(' to ')',
    '<' to '>',
    '{' to '}',
    '[' to ']'
)
val opening = setOf('(', '{', '<', '[')
val closing = setOf(')', '}', '>', ']')

val pointsA = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)
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
            if (it != pairs[lastOpen]) {
                return it
            }
        }
    }
    return null
}

fun completeLine(line: String): List<Char>? {
    val stack = Stack<Char>()

    for (it in line) {
        if (it in opening) {
            stack.push(it)
        } else if (it in closing) {
            val lastOpen = stack.pop()
            if (it != pairs[lastOpen]) {
                return null
            }
        }
    }
    return stack.mapNotNull { pairs[it] }
}

fun solveB(lines: List<String>): Long {
    val allScores = lines.mapNotNull { completeLine(it) }
        .map { calcPoints(it) }
        .sorted()

    return allScores[allScores.size / 2]
}

val pointsB = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

//The stack needs to be reversed to get the characters in the correct order - so we use foldRight instead of fold
fun calcPoints(it: List<Char>): Long = it.foldRight(0) { c, acc -> acc * 5 + (pointsB[c] ?: 0) }
