package day11

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day11KtTest {

    private val day = 11

    @Test
    fun sample1() {
        val lines = """|5483143223
            |2745854711
            |5264556173
            |6141336146
            |6357385478
            |4167524645
            |2176841721
            |6882881134
            |4846848554
            |5283751526""".trimMargin().lines()

        assertEquals(1656, solveA(lines))
        assertEquals(195, solveB(lines))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(1725, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(308, solveB)
    }
}