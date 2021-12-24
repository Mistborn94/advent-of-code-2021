package day24

import helper.cartesianProduct
import java.util.*
import kotlin.math.pow

fun runFormula(lines: List<String>, serialNumber: String, initialZ: Long = 0L): Long {
    val chunked = lines.chunked(18)
    val abcs = chunked.map { chunk -> extractAbc(chunk) }

    return abcs.foldIndexed(initialZ) { i, z, (a, b, c) -> runFormula(z, serialNumber[i].digitToInt(), a, b, c) }
}

fun runSimulation(lines: List<String>, serialNumber: String): Long {
    val instructions = lines.map { Instruction.parse(it) }
    val alu = Alu()
    serialNumber.forEach { alu.supplyInput(it.digitToInt()) }
    alu.runAll(instructions)
    return alu.read(3)
}

//Extract input values from a chunk of 18 instructions
fun extractAbc(chunk: List<String>): Triple<Int, Int, Int> {
    val a = chunk[4].split(" ")[2].toInt()
    val b = chunk[5].split(" ")[2].toInt()
    val c = chunk[15].split(" ")[2].toInt()
    return Triple(a, b, c)
}

//Formula derived from analyzing repeated instructions in the input
fun runFormula(z: Long, digit: Int, a: Int, b: Int, c: Int): Long {
    return if ((z % 26).toInt() == digit - b) {
        z / a
    } else {
        (z / a * 26) + digit + c
    }
}

//Inverting the above formula to solve Z backwards
fun solvePreviousZ(n: Long, digit: Int, a: Int, b: Int, c: Int): List<Long> {
    return when (a) {
        1 -> {
            buildList {
                //if (z % 26 == digit - b): n = z
                //else: n = z * 26 + digit + c
                if ((n % 26).toInt() == digit - b) {
                    add(n)
                } else {
                    val component = (n - digit - c)
                    if ((component % 26) == 0L) {
                        add(component / 26)
                    }
                }
            }
        }
        26 -> {
            //if (z % 26 == digit - b): n = z / 26
            //else: n = z / 26 * 26 + digit + c
            buildList {
                //There is always a number that can solve z for the first if statement
                //Because of integer division, any z from 26n to 26n + 25 satisfies n = z / 26
                //Of those, we can easily find the one that satisfies z % 26 == digit - b
                //The smallest observed value for b is -16, so there are no cases where (digit - b) > 25
                add(26 * n + digit - b)
                //Even if I remove this second part, the tests still works. Not entirely sure if that is luck or not.
                // We can eliminate the / 26 * 26 part, but because of integer division we have to make sure that (n - digit - c) is divisible by 26
                val component = n - digit - c
                if (component % 26 == 0L) {
                    add(component)
                }
            }
        }
        else -> {
            throw UnsupportedOperationException("This is impossible with my input")
        }
    }
}

fun solveA(lines: List<String>): Long = solve(lines, 9 downTo 1)

fun solveB(lines: List<String>): Long = solve(lines, 1..9)

private fun solve(lines: List<String>, digitRange: IntProgression): Long {
    val chunked = lines.chunked(18)
    val instructionValues = chunked.map { chunk -> extractAbc(chunk) }

    return instructionValues.foldRightIndexed(sequenceOf(Pair(0L, 0L))) { i, (a, b, c), acc ->
        val power10 = 10.0.pow(13 - i).toLong()
        acc.cartesianProduct(digitRange.asSequence()) { (nz, number), digit ->
            val newNumber = power10 * digit + number
            solvePreviousZ(nz, digit, a, b, c).map { it to newNumber }
        }.flatten()
    }.filter { (nz, _) -> nz == 0L }
        .map { (_, num) -> num }
        .first()
}

class Alu(private val values: LongArray = LongArray(4) { 0 }) {
    private val inputQueue: Queue<Int> = LinkedList()

    fun runAll(instructions: List<Instruction>) {
        instructions.forEach { it.run(this) }
    }

    fun supplyInput(input: Int) {
        inputQueue.add(input)
    }

    fun read(index: Int): Long = values[index]
    fun write(index: Int, value: Long) {
        values[index] = value
    }

    fun writeInput(index: Int) {
        values[index] = inputQueue.remove().toLong()
    }
}

sealed class Instruction(val name: String) {

    abstract fun run(alu: Alu)

    class IndexedInstruction(name: String, val indexA: Int, val indexB: Int, val valueTransform: (Long, Long) -> Long) : Instruction(name) {
        override fun run(alu: Alu) {
            val newValue = valueTransform(alu.read(indexA), alu.read(indexB))
            alu.write(indexA, newValue)
        }

        override fun toString(): String = "IndexedInstruction $name"
    }

    class ValueInstruction(name: String, val indexA: Int, val valueB: Int, val valueTransform: (Long, Long) -> Long) : Instruction(name) {
        override fun run(alu: Alu) {
            val newValue = valueTransform(alu.read(indexA), valueB.toLong())
            alu.write(indexA, newValue)
        }

        override fun toString(): String = "ValueInstruction $name"
    }

    class InputInstruction(val index: Int) : Instruction("inp") {
        override fun run(alu: Alu) {
            alu.writeInput(index)
        }

        override fun toString(): String = "InputInstruction $name"
    }

    companion object {
        fun parse(line: String): Instruction {
            val split = line.split(" ")
            return when (split[0]) {
                "inp" -> InputInstruction(index(split[1]))
                "add" -> parseGenericInstruction("add", split[1], split[2]) { a, b -> a + b }
                "mul" -> parseGenericInstruction("mul", split[1], split[2]) { a, b -> a * b }
                "div" -> parseGenericInstruction("div", split[1], split[2]) { a, b -> a / b }
                "mod" -> parseGenericInstruction("mod", split[1], split[2]) { a, b -> a % b }
                "eql" -> parseGenericInstruction("eql", split[1], split[2]) { a, b -> (a == b).toInt().toLong() }
                else -> throw IllegalArgumentException("Unknown Instruction ${split[0]}")
            }
        }

        private fun parseGenericInstruction(name: String, a: String, b: String, valueTransform: (Long, Long) -> Long): Instruction {
            val indexA = index(a)
            val valueB = b.toIntOrNull()
            return if (valueB == null) {
                IndexedInstruction(name, indexA, index(b), valueTransform)
            } else {
                ValueInstruction(name, indexA, valueB, valueTransform)
            }
        }

        private fun index(name: String): Int {
            return when (name) {
                "w" -> 0
                "x" -> 1
                "y" -> 2
                "z" -> 3
                else -> throw IllegalArgumentException("Unknown Variable $name")
            }
        }
    }
}

private fun Boolean.toInt(): Int = if (this) 1 else 0
