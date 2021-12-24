package day24

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

internal class Day24KtTest {

    private val day = 24

    @Test
    fun testShortFormula() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        for (digit in 1..9) {
            val serialNumber = "$digit".repeat(14)
            assertEquals(runSimulation(lines, serialNumber), runFormula(lines, serialNumber), "Result must be equal for digit $digit")
        }
    }

    @Test
    fun testBackwardsSolve() {
        val lines = readDayFile(day, "input").readText().trim().lines()
        val lastChunk = lines.chunked(18).last()
        val (a, b, c) = extractAbc(lastChunk)

        for (digit in 1..9) {
            val targetZ = solvePreviousZ(0L, digit, a, b, c)
            assertFalse(targetZ.isEmpty())
            targetZ.forEach {
                assertEquals(0, runFormula(lastChunk, digit.toString(), it))
            }
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