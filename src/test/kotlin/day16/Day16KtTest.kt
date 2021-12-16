package day16

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day16KtTest {

    private val day = 16

    @Test
    fun sample1() {

        assertEquals(16, solveA("8A004A801A8002F478"))
        assertEquals(12, solveA("620080001611562C8802118E34"))
        assertEquals(23, solveA("C0015000016115A2E0802F182340"))
        assertEquals(31, solveA("A0016C880162017C3686B18A3D4780"))

        assertEquals(3, solveB("C200B40A82"))
        assertEquals(54, solveB("04005AC33890"))
        assertEquals(7, solveB("880086C3E88112"))
        assertEquals(9, solveB("CE00C43D881120"))
        assertEquals(1, solveB("D8005AC2A8F0"))
        assertEquals(0, solveB("F600BC2D8F"))
        assertEquals(0, solveB("9C005AC2F8F0"))
        assertEquals(1, solveB("9C0141080250320F1802104A08"))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(965, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(116672213160, solveB)
    }
}