package day3

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day3KtTest {

    private val day = 3

    @Test
    fun sample1() {
        val lines = """|00100
            |11110
            |10110
            |10111
            |10101
            |01111
            |00111
            |11100
            |10000
            |11001
            |00010
            |01010""".trimMargin().lines()

        assertEquals(198, solveA(lines))
        assertEquals(230, solveB(lines))
    }


    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(3895776, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(7928162, solveB)
    }
}