package day10

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day10KtTest {

    private val day = 10

    @Test
    fun sample1() {
        val lines = """|[({(<(())[]>[[{[]{<()<>>
            |[(()[<>])]({[<{<<[]>>(
            |{([(<{}[<>[]}>{[]{[(<()>
            |(((({<>}<{<{<>}{[]{[]{}
            |[[<[([]))<([[{}[[()]]]
            |[{[{({}]{}}([{[{{{}}([]
            |{<[[]]>}<{[{[{[]{()[[[]
            |[<(<(<(<{}))><([]([]()
            |<{([([[(<>()){}]>(<<{{
            |<{([{{}}[<[[[<>{}]]]>[]]""".trimMargin().lines()

        assertEquals(26397, solveA(lines))
        assertEquals(288957, solveB(lines))
    }

    @Test
    fun sample2() {
        val lines = """|[<>({}){}[([])<>]]
            |([]){()()()}<([{}])>
            |(((((((((())))))))))
            |[({(<(())[]>[[{[]{<()<>>
            |[(()[<>])]({[<{<<[]>>(
        """.trimMargin().lines()

        assertEquals(0, solveA(lines))
        assertEquals(288957, solveB("[({(<(())[]>[[{[]{<()<>>".lines()))
        assertEquals(5566, solveB("[(()[<>])]({[<{<<[]>>(".lines()))
        assertEquals(1480781, solveB("(((({<>}<{<{<>}{[]{[]{}".lines()))
        assertEquals(995444, solveB("{<[[]]>}<{[{[{[]{()[[[]".lines()))
        assertEquals(294, solveB("<{([{{}}[<[[[<>{}]]]>[]]".lines()))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(387363, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(4330777059, solveB)
    }
}