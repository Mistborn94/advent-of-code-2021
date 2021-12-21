package day21

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day21KtTest {

    private val day = 21

    @Test
    fun sample1() {
        val lines = readDayFile(day, "sample1.in").readText().trim().lines()

        assertEquals(739785, solveA(lines))
        assertEquals(444356092776315, solveB(lines))
    }


    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(605070, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(218433063958910, solveB)
    }
}