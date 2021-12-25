package day25

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day25KtTest {

    private val day = 25

    @Test
    fun sample1() {
        val lines = readDayFile(day, "sample1.in").readText().trim().lines()

        assertEquals(58, solveA(lines))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(295, solveA)
    }
}