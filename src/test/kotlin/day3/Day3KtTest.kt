package day3

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day3KtTest {

    private val day = 3

    @Test
    fun sample1() {
        val text = """|00100
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
            |01010""".trimMargin()

        assertEquals(198, solveA(text))
        assertEquals(230, solveB(text))
    }


    @Test
    fun solve() {
        val text = readDayFile(day, "input").readText().trim()

        val solveA = solveA(text)
        println("A: $solveA")
        assertEquals(3895776, solveA)

        val solveB = solveB(text)
        println("B: $solveB")
        assertEquals(7928162, solveB)
    }
}