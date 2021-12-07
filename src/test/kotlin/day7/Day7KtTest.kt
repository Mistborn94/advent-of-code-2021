package day7

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day7KtTest {

    private val day = 7

    @Test
    fun sample1() {
        val lines = "16,1,2,0,4,2,7,1,2,14"

        assertEquals(37, solveA(lines))
        assertEquals(168, solveB(lines))
    }


    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(341534, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(93397632, solveB)
    }
}