package day17

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day17KtTest {

    private val day = 17

    @Test
    fun sample1() {
        val lines = "target area: x=20..30, y=-10..-5".trim()

        assertEquals(45, solveA(lines))
        assertEquals(112, solveB(lines))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(5050, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(2223, solveB)
    }
}