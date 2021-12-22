package day22

import helper.cartesianProduct
import helper.point.HyperspacePoint
import kotlin.math.max
import kotlin.math.min

val pattern = "(on|off) x=([-0-9]+)..([-0-9]+),y=([-0-9]+)..([-0-9]+),z=([-0-9]+)..([-0-9]+)".toRegex()

fun solveA(lines: List<String>): Int {


    val instructions = lines.map { parseLine(it) }

//    val (minPoint, maxPoint) = instructions.fold(Point3(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE) to Point3(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)) { (min, max), next ->
//        val min = Point3(min(min.x, next.x.first), min(min.y, next.y.first), min(min.z, next.z.first))
//        val max = Point3(max(max.x, next.x.last), max(max.y, next.y.last), max(max.z, next.z.last))
//        min to max
//    }
    val lowerBound = -50 - instructions.size
    val upperBound = 50 + instructions.size
    val countRange = -50..50
//    val instructionRange = -50..50
//    val range = lowerBound..upperBound
//    val cubes = IntTrie.create(intArrayOf(lowerBound, lowerBound, lowerBound), intArrayOf(upperBound, upperBound, upperBound))
    val cubes = mutableSetOf<HyperspacePoint>()
    instructions
        .map { it.clampTo(countRange) }
        .forEach { instruction ->
            val points = instruction.x.asSequence().cartesianProduct(instruction.y.asSequence()).cartesianProduct(instruction.z.asSequence()) { (x, y), z ->
                HyperspacePoint.of(x, y, z)
            }.filter { it.x in countRange && it.y in countRange && it.z in countRange }

            if (instruction.on) {
                for (point in points) {
                    cubes.add(point)
                }
            } else {
                for (point in points) {
                    cubes.remove(point)
                }
            }
        }

    return cubes.count { it.x in countRange && it.y in countRange && it.z in countRange }
}

fun parseLine(line: String): RebootInstruction {
    val matchResult = pattern.matchEntire(line)!!
    val on = matchResult.groupValues[1] == "on"
    val x = matchResult.groupValues[2].toInt()..matchResult.groupValues[3].toInt()
    val y = matchResult.groupValues[4].toInt()..matchResult.groupValues[5].toInt()
    val z = matchResult.groupValues[6].toInt()..matchResult.groupValues[7].toInt()
    return RebootInstruction(on, x, y, z)
}

data class RebootInstruction(val on: Boolean, val x: IntRange, val y: IntRange, val z: IntRange) {
    fun clampTo(newRange: IntRange): RebootInstruction = RebootInstruction(on, clampRange(x, newRange), clampRange(y, newRange), clampRange(z, newRange))

    private fun clampRange(currentRange: IntRange, newRange: IntRange) = max(currentRange.first, newRange.first)..min(currentRange.last, newRange.last)
}


fun solveB(lines: List<String>): Int {
    return 0
}
