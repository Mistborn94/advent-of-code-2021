package day19

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day19KtTest {

    private val day = 19

    @Test
    fun sample1() {
        val lines = readDayFile(day, "sample1.in").readText().trim()

        assertEquals(79, solveA(lines))
        assertEquals(3621, solveB(lines))
    }

    @Test
    fun sample2() {
        val lines = readDayFile(day, "sample2.in").readText().trim()

        assertEquals(12, solveA(lines))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(400, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(12168, solveB)
    }
}