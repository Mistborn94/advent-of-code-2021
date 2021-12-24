package day24

import helper.cartesianProduct
import java.util.*
import kotlin.math.pow

fun runShort(lines: List<String>, inputDigit: Int, initialZ: Long = 0L): Long {
    val chunked = lines.chunked(18)
    val abcs = chunked.map { chunk -> extractAbc(chunk) }

    return abcs.fold(initialZ) { z, (a, b, c) -> runFormula(z, inputDigit, a, b, c) }
}

fun runLong(lines: List<String>, inputDigit: Int): Long {
    val instructions = lines.map { Instruction.parse(it) }
    val alu = Alu()
    repeat(14) {
        alu.supplyInput(inputDigit)
    }
    alu.runAll(instructions)
    return alu.read(3)
}

fun extractAbc(chunk: List<String>): Triple<Int, Int, Int> {
    val a = chunk[4].split(" ")[2].toInt()
    val b = chunk[5].split(" ")[2].toInt()
    val c = chunk[15].split(" ")[2].toInt()
    return Triple(a, b, c)
}

fun runFormula(z: Long, digit: Int, a: Int, b: Int, c: Int): Long {
    return if ((z % 26).toInt() == digit - b) {
        z / a
    } else {
        (z / a * 26) + digit + c
    }
}

fun solvePreviousZ(nz: Long, digit: Int, a: Int, b: Int, c: Int): List<Long> {
    return when (a) {
        1 -> {
            buildList {
                if ((nz % 26).toInt() == digit - b) {
                    add(nz)
                } else {
                    val component = (nz - digit - c)
                    if ((component % 26) == 0L) {
                        add(component / 26)
                    }
                }
            }
        }
        26 -> {
            buildList {
                add(26 * nz + digit - b)
                val component = nz - digit - c
                if (component % 26 == 0L) {
                    add(component)
                }
            }
        }
        else -> {
            throw IllegalStateException("This is impossible with my input")
        }
    }
}

fun solveA(lines: List<String>): Long {
    val results = solve(lines)
    return results.maxOf { it }
}

private fun solve(lines: List<String>): List<Long> {
    val chunked = lines.chunked(18)
    val abcs = chunked.map { chunk -> extractAbc(chunk) }

    val lastDigitPossibles: List<Pair<Long, Long>> = digitRange.flatMap { digit ->
        val (a, b, c) = abcs.last()
        solvePreviousZ(0L, digit, a, b, c).map { it to digit.toLong() }
    }
    val results = abcs.reversed().drop(1).foldIndexed(lastDigitPossibles) { i, acc, (a, b, c) ->
        val power10 = 10.0.pow(i + 1).toLong()
        acc.cartesianProduct(digitRange) { (nz, number), digit ->
            val possibleZs = solvePreviousZ(nz, digit, a, b, c)
            val newNumber = power10 * digit + number
            possibleZs.map { it to newNumber }
        }.flatten()
    }.filter { (nz, _) -> nz == 0L }
        .map { (_, num) -> num }
    return results
}

val digitRange = 9 downTo 1

fun solveB(lines: List<String>): Long {
    val results = solve(lines)
    return results.minOf { it }
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

    fun copy(): Alu = Alu(values.copyOf())
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
