package day12

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day12KtTest {

    private val day = 12

    @Test
    fun sample1() {
        val lines = """|start-A
            |start-b
            |A-c
            |A-b
            |b-d
            |A-end
            |b-end""".trimMargin().lines()

        assertEquals(10, solveA(lines))
        assertEquals(36, solveB(lines))
    }

    @Test
    fun sample2() {
        val lines = """|dc-end
            |HN-start
            |start-kj
            |dc-start
            |dc-HN
            |LN-dc
            |HN-end
            |kj-sa
            |kj-HN
            |kj-dc""".trimMargin().lines()

        assertEquals(19, solveA(lines))
        assertEquals(103, solveB(lines))
    }

    @Test
    fun sample3() {
        val lines = """|fs-end
            |he-DX
            |fs-he
            |start-DX
            |pj-DX
            |end-zg
            |zg-sl
            |zg-pj
            |pj-he
            |RW-he
            |fs-DX
            |pj-RW
            |zg-RW
            |start-pj
            |he-WI
            |zg-he
            |pj-fs
            |start-RW""".trimMargin().lines()

        assertEquals(226, solveA(lines))
        assertEquals(3509, solveB(lines))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(4549, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(120535, solveB)
    }
}