package day9

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day9KtTest {

    private val day = 9

    @Test
    fun sample1() {
        val lines =
            """|2199943210
               |3987894921
               |9856789892
               |8767896789
               |9899965678""".trimMargin().lines()

        assertEquals(15, solveA(lines))
        assertEquals(1134, solveB(lines))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(539, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(736920, solveB)
    }
}