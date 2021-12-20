package day19

import helper.cartesianProduct
import java.util.*
import kotlin.math.abs

fun solveA(text: String): Int {
    val solvedScanners = solve(text)

    return solvedScanners.values.flatMap { it.second }.toSet().size
}

fun solveB(text: String): Int {
    val positions = solve(text).values.map { it.first }

    return positions.cartesianProduct(positions) { a, b -> (a - b).abs() }.maxOrNull()!!
}

val allOrientations = Axis.values().flatMap { it.orientations() }
val zeroPoint = Point3(0, 0, 0)

private fun solve(text: String): Map<Int, Pair<Point3, Collection<Point3>>> {
    val scannerBeacons = text.split("\n\n")
        .map { value -> value.split("\n").drop(1).map { line -> buildPoint(line) } }

    val solvedScanners: MutableMap<Int, Pair<Point3, Collection<Point3>>> = mutableMapOf(0 to (zeroPoint to scannerBeacons[0]))
    val rotatedScanners = scannerBeacons.drop(1)
        .map { points -> allOrientations.associateWith { orientation -> rotateAll(orientation, points) } }

    val solvedScannersQueue = LinkedList(listOf(0))
    val unknownScanners = (1 until scannerBeacons.size).toMutableList()
    while (unknownScanners.isNotEmpty()) {
        val solvedScannerIndex = solvedScannersQueue.remove()
        unknownScanners.removeIf { scannerId ->
            val thisScanner = scannerBeacons[scannerId]
            val solution = trySolveScanner(rotatedScanners[scannerId - 1], solvedScanners[solvedScannerIndex]!!.second)

            if (solution != null) {
                val (orientation, position) = solution
                solvedScanners[scannerId] = position to thisScanner.map { orientation.rotate(it) + position }.toSet()
                solvedScannersQueue.add(scannerId)
            }
            solution != null
        }
    }
    return solvedScanners
}

fun buildPoint(it: String): Point3 {
    val (x, y, z) = it.trim()
        .split(",")
        .map { it.toInt() }
    return Point3(x, y, z)
}

fun rotateAll(orientation: Orientation, points: Collection<Point3>) = points.map { point -> orientation.rotate(point) }

fun trySolveScanner(thisScanner: Map<Orientation, Collection<Point3>>, solvedScanner: Collection<Point3>): Pair<Orientation, Point3>? {
    return thisScanner.entries.firstNotNullOfOrNull { (orientation, beacons) ->
        val position = beacons.cartesianProduct(solvedScanner) { a, b -> b - a }
            .groupingBy { it }
            .eachCount()
            .entries
            .firstOrNull { (_, value) -> value >= 12 }?.key

        if (position == null) null else orientation to position
    }
}

data class Point3(val x: Int, val y: Int, val z: Int) {
    operator fun minus(other: Point3) = Point3(x - other.x, y - other.y, z - other.z)
    operator fun plus(other: Point3) = Point3(x + other.x, y + other.y, z + other.z)

    fun abs() = abs(x) + abs(y) + abs(z)
}

data class Orientation(val x: Axis, val y: Axis, val z: Axis) {
    fun rotate(point: Point3) = Point3(x.getValue(point), y.getValue(point), z.getValue(point))
}

enum class Axis {
    XP {
        override fun getValue(point: Point3) = point.x
        override fun orientations(): List<Orientation> = listOf(
            Orientation(ZN, YP, this),
            Orientation(YN, ZN, this),
            Orientation(ZP, YN, this),
            Orientation(YP, ZP, this)
        )
    },
    XN {
        override fun getValue(point: Point3) = -point.x
        override fun orientations(): List<Orientation> = listOf(
            Orientation(YP, ZN, this),
            Orientation(ZN, YN, this),
            Orientation(YN, ZP, this),
            Orientation(ZP, YP, this)
        )
    },
    YP {
        override fun getValue(point: Point3) = point.y
        override fun orientations(): List<Orientation> = listOf(
            Orientation(XP, ZN, this),
            Orientation(ZN, XN, this),
            Orientation(XN, ZP, this),
            Orientation(ZP, XP, this),
        )
    },
    YN {
        override fun getValue(point: Point3) = -point.y
        override fun orientations(): List<Orientation> = listOf(
            Orientation(XN, ZN, this),
            Orientation(ZN, XP, this),
            Orientation(XP, ZP, this),
            Orientation(ZP, XN, this),
        )
    },
    ZN {
        override fun getValue(point: Point3) = -point.z
        override fun orientations(): List<Orientation> = listOf(
            Orientation(XN, YP, this),
            Orientation(YP, XP, this),
            Orientation(XP, YN, this),
            Orientation(YN, XN, this),
        )
    },
    ZP {
        override fun getValue(point: Point3) = point.z
        override fun orientations(): List<Orientation> = listOf(
            Orientation(XP, YP, this),
            Orientation(YN, XP, this),
            Orientation(XN, YN, this),
            Orientation(YP, XN, this),
        )
    };

    abstract fun getValue(point: Point3): Int
    abstract fun orientations(): List<Orientation>
}
