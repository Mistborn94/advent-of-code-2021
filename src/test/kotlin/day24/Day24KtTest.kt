package day24

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day24KtTest {

    private val day = 24

    @Test
    fun testShortFormula() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        for (digit in 1..9) {
            assertEquals(runOnceLong(lines, digit), runOnceShort(lines, digit), "Result must be equal for digit $digit")
        }
    }

    @Test
    fun testBackwardsSolve() {
        val lines = readDayFile(day, "input").readText().trim().lines()
        val lastChunk = lines.chunked(18).last()
        val (a, b, c) = extractAbc(lastChunk)

        for (digit in 1..9) {
            val targetZ = solvePreviousZ(0L, digit, a, b, c)
            println("Targets found for digit $digit: ${targetZ}")

            assertEquals(0, runOnceShort(lastChunk, digit, targetZ!!))
        }
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(94992992796199, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(11931881141161, solveB)
    }
}