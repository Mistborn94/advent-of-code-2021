package template

import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

@Ignore
internal class DayTKtTest {

    private val day = 0

    @Test
    fun sample1() {
        val text = readDayFile(day, "sample1.in").readText().trim()

        assertEquals(0, solveA(text))
        assertEquals(0, solveB(text))
    }

    @Test
    fun sample2() {
        val text = readDayFile(day, "sample2.in").readText().trim()

        assertEquals(0, solveA(text))
        assertEquals(0, solveB(text))
    }

    @Test
    fun solve() {
        val text = readDayFile(day, "input").readText().trim()

        val solveA = solveA(text)
        println("A: $solveA")
        assertEquals(0, solveA)

        val solveB = solveB(text)
        println("B: $solveB")
        assertEquals(0, solveB)
    }
}