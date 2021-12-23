package day23

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day23KtTest {

    private val day = 23

    @Test
    fun sample1() {
        val lines = readDayFile(day, "sample1.in").readText().trim().lines()

        assertEquals(12521, solveA(lines))
        assertEquals(44169, solveB(lines))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(13336, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(53308, solveB)
    }
}