package day22

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day22KtTest {

    private val day = 22

    @Test
    fun sample1() {
        val lines = readDayFile(day, "sample1.in").readText().trim().lines()

        assertEquals(590784, solveA(lines))
    }

    @Test
    fun sample2() {
        val lines = readDayFile(day, "sample2.in").readText().trim().lines()

        assertEquals(39, solveA(lines))
        assertEquals(39, solveB(lines))
    }

    @Test
    fun sample3() {
        val lines = readDayFile(day, "sample3.in").readText().trim().lines()

        assertEquals(2758514936282235, solveB(lines))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(607573, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(1267133912086024, solveB)
    }
}