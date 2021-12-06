package day6

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day6KtTest {

    private val day = 6

    @Test
    fun sample1() {
        val lines = """3,4,3,1,2"""

        assertEquals(6, solve(lines, 2))
        assertEquals(7, solve(lines, 3))
        assertEquals(9, solve(lines, 4))
        assertEquals(26, solve(lines, 18))
        assertEquals(5934, solve(lines, 80))
        assertEquals(26984457539L, solve(lines, 256))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText()

        val solveA = solve(lines, 80)
        println("A: $solveA")
        assertEquals(354564, solveA)

        val solveB = solve(lines, 256)
        println("B: $solveB")
        assertEquals(1609058859115, solveB)
    }
}