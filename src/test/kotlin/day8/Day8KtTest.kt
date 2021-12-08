package day8

import helper.readDayFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day8KtTest {
    private val day = 8

    @Test
    fun sample1() {
        val lines = """be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce""".lines()

        assertEquals(26, solveA(lines))
        assertEquals(61229, solveB(lines))
    }

    @Test
    fun sample2() {
        val lines = """acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf""".lines()

        assertEquals(5353, solveB(lines))
    }

    @Test
//    @RepeatedTest(1000)
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(288, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(940724, solveB)
    }
}