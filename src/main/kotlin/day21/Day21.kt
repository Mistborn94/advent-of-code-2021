package day21

import helper.cartesianProduct
import kotlin.math.max
import kotlin.math.min

fun solveA(lines: List<String>): Int {
    var player1 = lines.first().substringAfter(": ").toInt()
    var player2 = lines.last().substringAfter(": ").toInt()

    val die = CircularIterator(1..100)

    var score1 = 0
    var score2 = 0
    var dieRolls = 0

    while (score1 < 1_000 && score2 < 1_000) {
        dieRolls += 3
        val roll1 = (1..3).sumOf { die.next() }
        player1 = (player1 + roll1 - 1) % 10 + 1
        score1 += player1

        if (score1 < 1_000) {
            val roll2 = (1..3).sumOf { die.next() }
            dieRolls += 3
            player2 = (player2 + roll2 - 1) % 10 + 1
            score2 += player2
        }
    }
    return min(score1, score2) * dieRolls
}


fun solveB(lines: List<String>): Long {
    val player1 = lines.first().substringAfter(": ").toInt()
    val player2 = lines.last().substringAfter(": ").toInt()

    val (p1WinCount, p2WinCount) = getWinCount(GameState(player1, 0, player2, 0))

    return max(p1WinCount, p2WinCount)
}

val possibleRolls = (1..3).cartesianProduct(1..3) { a, b -> a + b }.cartesianProduct(1..3) { a, b -> a + b }
val cache = mutableMapOf<GameState, WinCount>()

private fun getWinCount(currentState: GameState): WinCount {
    val cachedState = cache[currentState]
    if (cachedState != null) {
        return cachedState
    } else if (currentState.score1 >= 21) {
        return WinCount(1, 0)
    } else if (currentState.score2 >= 21) {
        return WinCount(0, 1)
    } else {
        // We can't just evaluate all possible combinations of roll1 and roll2, that inflates the win count for player 1
        // If player 1's score hits 21 the game stops without player 2 even rolling the die.
        // The inversion trick handles that and ensure we benefit more from caching
        val finalWinCount = possibleRolls.map { roll1 ->
            val pos1 = (currentState.pos1 + roll1 - 1) % 10 + 1
            val score1 = currentState.score1 + pos1
            val nextState = GameState(currentState.pos2, currentState.score2, pos1, score1)
            getWinCount(nextState).inverted()
        }.reduce { acc, result -> acc + result }
        cache[currentState] = finalWinCount
        return finalWinCount
    }
}

data class WinCount(val p1: Long, val p2: Long) {

    operator fun plus(other: WinCount) = WinCount(p1 + other.p1, p2 + other.p2)

    fun inverted() = WinCount(p2, p1)
}

data class GameState(val pos1: Int, val score1: Int, val pos2: Int, val score2: Int)

class CircularIterator<T>(private val iterable: Iterable<T>) : Iterator<T> {
    var iterator = iterable.iterator()
    override fun hasNext(): Boolean {
        return true
    }

    override fun next(): T {
        if (!iterator.hasNext()) {
            iterator = iterable.iterator()
        }
        return iterator.next()
    }
}
