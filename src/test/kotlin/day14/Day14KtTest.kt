package day14

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day14KtTest {

    private val day = 14

    @Test
    fun sample1() {
        val lines = readDayFile(day, "sample1.in").readText().trim().lines()

        assertEquals(1588, solve(lines, 10))
        assertEquals(2188189693529, solve(lines, 40))
    }

    //    @Tes/t
    fun sample2() {
        val lines = readDayFile(day, "sample2.in").readText().trim().lines()

        assertEquals(0, solve(lines, 10))
        assertEquals(0, solve(lines, 40))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solve(lines, 10)
        println("A: $solveA")
        assertEquals(2745, solveA)

        val solveB = solve(lines, 40)
        println("B: $solveB")
        assertEquals(0, solveB)
    }
}