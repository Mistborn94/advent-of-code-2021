package day13

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day13KtTest {

    private val day = 13

    @Test
    fun sample1() {
        val lines = """|6,10
            |0,14
            |9,10
            |0,3
            |10,4
            |4,11
            |6,0
            |6,12
            |4,1
            |0,13
            |10,12
            |3,4
            |3,0
            |8,4
            |1,10
            |2,14
            |8,10
            |9,0
            |
            |fold along y=7
            |fold along x=5""".trimMargin().lines()

        val solutionB = """|
            |#####
            |#   #
            |#   #
            |#   #
            |#####
            |""".trimMargin()

        assertEquals(17, solveA(lines))
        assertEquals(solutionB, solveB(lines))
    }


    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim().lines()
        val solutionB = """|
            |#### #  #  ##  #  #  ##  #### #  #  ## 
            |   # # #  #  # #  # #  # #    #  # #  #
            |  #  ##   #  # #  # #    ###  #  # #   
            | #   # #  #### #  # #    #    #  # #   
            |#    # #  #  # #  # #  # #    #  # #  #
            |#### #  # #  #  ##   ##  #     ##   ## 
            |""".trimMargin()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(731, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(solutionB, solveB)
    }
}