package day22

import helper.size
import kotlin.math.max
import kotlin.math.min

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
    val (turnedOn, turnedOff) = steps
        .fold(Pair(emptyList<Cuboid>(), emptyList<Cuboid>())) { (turnedOn, turnedOff), instruction ->
            if (instruction.on) {
                val newOn = turnedOn + instruction.cuboid + turnedOff.overlapAll(instruction.cuboid)
                val newOff = turnedOff + turnedOn.overlapAll(instruction.cuboid)
                newOn to newOff
            } else {
                val newOn = turnedOn + turnedOff.overlapAll(instruction.cuboid)
                val newOff = turnedOff + turnedOn.overlapAll(instruction.cuboid)
                newOn to newOff
            }
        }

    return turnedOn.sumOf { it.volume } - turnedOff.sumOf { it.volume }
}

private fun List<Cuboid>.overlapAll(cuboid: Cuboid) = map { it.overlap(cuboid) }.filter { !it.isEmpty() }

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

    val volume: Long = x.size().toLong() * y.size().toLong() * z.size().toLong()

    fun isEmpty() = x.isEmpty() || y.isEmpty() || z.isEmpty()

    fun overlap(other: Cuboid): Cuboid = Cuboid(overlappingRange(x, other.x), overlappingRange(y, other.y), overlappingRange(z, other.z))

    private fun overlappingRange(first: IntRange, second: IntRange) = max(first.first, second.first)..min(first.last, second.last)
}
