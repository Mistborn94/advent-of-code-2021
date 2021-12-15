package day15

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day15KtTest {

    private val day = 15

    @Test
    fun sample1() {
        val lines = readDayFile(day, "sample1.in").readText().trim().lines()

        assertEquals(40, solveA(lines))
        assertEquals(315, solveB(lines))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(741, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(2976, solveB)
    }
}