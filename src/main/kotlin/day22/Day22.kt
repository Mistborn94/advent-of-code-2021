package day22

import kotlin.math.max
import kotlin.math.min

private val IntRange.size: Int
    get() = max(0, last - first + 1)
val pattern = "(on|off) x=([-0-9]+)..([-0-9]+),y=([-0-9]+)..([-0-9]+),z=([-0-9]+)..([-0-9]+)".toRegex()

fun solveA(lines: List<String>): Long {
    val steps = lines.map { parseLine(it) }
        .map { it.clampTo(-50..50) }
        .filter { !it.cuboid.isEmpty() }
    return doReboot(steps)
}

fun solveB(lines: List<String>): Long {
    val steps = lines.map { parseLine(it) }
    return doReboot(steps)
}

private fun doReboot(steps: List<RebootStep>): Long {
    val turnedOn: List<Cuboid> = steps
        .fold(emptyList()) { acc, instruction ->
            if (instruction.on) {
                acc + instruction.cuboid
            } else {
                acc.flatMap { it.subtract(instruction.cuboid) }
            }
        }

    return deduplicate(turnedOn).sumOf { it.count }
}

fun deduplicate(cuboids: Collection<Cuboid>): Collection<Cuboid> {
    val counted = mutableListOf<Cuboid>()

    for (next in cuboids) {
        val overlaps = counted.asSequence().filter { it.overlaps(next) }
        val new = overlaps.fold(sequenceOf(next)) { new, existing ->
            new.flatMap { it.subtract(existing) }
        }
        counted.addAll(new)
    }

    return counted
}

fun parseLine(line: String): RebootStep {
    val matchResult = pattern.matchEntire(line)!!
    val on = matchResult.groupValues[1] == "on"
    val x = matchResult.groupValues[2].toInt()..matchResult.groupValues[3].toInt()
    val y = matchResult.groupValues[4].toInt()..matchResult.groupValues[5].toInt()
    val z = matchResult.groupValues[6].toInt()..matchResult.groupValues[7].toInt()
    return RebootStep(on, Cuboid(x, y, z))
}

data class RebootStep(val on: Boolean, val cuboid: Cuboid) {
    fun clampTo(newRange: IntRange): RebootStep = RebootStep(on, cuboid.overlap(Cuboid(newRange, newRange, newRange)))
}

data class Cuboid(val x: IntRange, val y: IntRange, val z: IntRange) {

    val count: Long = x.size.toLong() * y.size.toLong() * z.size.toLong()

    fun isEmpty() = x.isEmpty() || y.isEmpty() || z.isEmpty()

    fun overlaps(other: Cuboid): Boolean = overlap(other).count > 0
    fun overlap(other: Cuboid): Cuboid = Cuboid(overlappingRange(x, other.x), overlappingRange(y, other.y), overlappingRange(z, other.z))

    fun subtract(other: Cuboid): List<Cuboid> {
        val overlap = overlap(other)
        return when {
            overlap.isEmpty() -> listOf(this)
            overlap == this -> emptyList()
            else -> {
                val possibleX = splitRange(x, overlap.x)
                val possibleY = splitRange(y, overlap.y)
                val possibleZ = splitRange(z, overlap.z)

                listOf(
                    Cuboid(x, possibleY.first(), z),
                    Cuboid(x, possibleY.last(), z),
                    Cuboid(possibleX.first(), possibleY[1], z),
                    Cuboid(possibleX.last(), possibleY[1], z),
                    Cuboid(possibleX[1], possibleY[1], possibleZ.first()),
                    Cuboid(possibleX[1], possibleY[1], possibleZ.last()),
                ).filter { !it.isEmpty() }
            }
        }
    }

    private fun splitRange(range: IntRange, overlapRange: IntRange): List<IntRange> {
        val before = range.first until overlapRange.first
        val after = overlapRange.last + 1..range.last
        return listOf(before, overlapRange, after)
    }

    private fun overlappingRange(first: IntRange, second: IntRange) = max(first.first, second.first)..min(first.last, second.last)
}
