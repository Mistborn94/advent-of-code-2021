package day2

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day2KtTest {

    private val day = 2

    @Test
    fun sample1() {
        val text = """|forward 5
                      |down 5
                      |forward 8
                      |up 3
                      |down 8
                      |forward 2""".trimMargin()

        assertEquals(150, solveA(text))
        assertEquals(900, solveB(text))
    }

    @Test
    fun solve() {
        val text = readDayFile(day, "input").readText().trim()

        val solveA = solveA(text)
        println("A: $solveA")
        assertEquals(2117664, solveA)

        val solveB = solveB(text)
        println("B: $solveB")
        assertEquals(2073416724, solveB)
    }
}