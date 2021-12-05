package day5

import helper.readDayFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day5KtTest {

    private val day = 5

    @Test
    fun sample1() {
        val lines = """0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2""".lines()

        assertEquals(5, solveA(lines))
        assertEquals(12, solveB(lines))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(4745, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(18442, solveB)
    }
}