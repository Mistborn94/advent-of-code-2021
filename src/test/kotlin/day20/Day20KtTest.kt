package day20

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day20KtTest {

    private val day = 20

    @Test
    fun sample1() {
        val lines = readDayFile(day, "sample1.in").readText().trim().lines()

        assertEquals(35, solve(lines, 2))
        assertEquals(35, solve(lines, 2))
        assertEquals(3351, solve(lines))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solve(lines, 2)
        println("A: $solveA")
        assertEquals(5044, solveA)
        assertEquals(5044, solve(lines, 2))

        val solveB = solve(lines, 50)
        println("B: $solveB")
        assertEquals(18074, solveB)
    }
}