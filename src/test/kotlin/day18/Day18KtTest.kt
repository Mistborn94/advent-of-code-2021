package day18

import day18.SnailFish.PairFish
import day18.SnailFish.ValueFish
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day18KtTest {

    private val day = 18

    @Test
    fun sample1() {
        val lines = readDayFile(day, "sample1.in").readText().trim().lines()

        assertEquals(4140, solveA(lines))
        assertEquals(3993, solveB(lines))
    }

    @Test
    fun operators() {
        assertEquals("[[[[0,9],2],3],4]", parseAndReduce("[[[[[9,8],1],2],3],4]"))
        assertEquals("[7,[6,[5,[7,0]]]]", parseAndReduce("[7,[6,[5,[4,[3,2]]]]]"))
        assertEquals("[[6,[5,[7,0]]],3]", parseAndReduce("[[6,[5,[4,[3,2]]]],1]"))
        assertEquals("[0,[5,5]]", PairFish(ValueFish(0), ValueFish(10)).reduce().toString())
        assertEquals("[0,[5,6]]", PairFish(ValueFish(0), ValueFish(11)).reduce().toString())
        assertEquals("[0,[6,6]]", PairFish(ValueFish(0), ValueFish(12)).reduce().toString())
        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", parseAndReduce("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]"))

        assertEquals("[[[[7,8],[6,6]],[[6,0],[7,7]]],[[[7,8],[8,8]],[[7,9],[0,6]]]]", parseAndReduce("[[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]],[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]]"))
        assertEquals(3993, magnitude("[[[[7,8],[6,6]],[[6,0],[7,7]]],[[[7,8],[8,8]],[[7,9],[0,6]]]]"))
    }

    private fun magnitude(line: String) = SnailFish.parse(line).magnitude()

    private fun parseAndReduce(line: String) = SnailFish.parse(line).reduce().toString()

    //    @Test
    fun sample2() {
        val lines = readDayFile(day, "sample2.in").readText().trim().lines()

        assertEquals(0, solveA(lines))
        assertEquals(0, solveB(lines))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(3524, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(4656, solveB)
    }
}