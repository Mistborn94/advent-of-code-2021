package day19

import java.util.*
import kotlin.math.abs

data class Point3(val x: Int, val y: Int, val z: Int) {
    operator fun minus(other: Point3) = Point3(x - other.x, y - other.y, z - other.z)
    operator fun plus(other: Point3) = Point3(x + other.x, y + other.y, z + other.z)

    fun abs() = abs(x) + abs(y) + abs(z)
}

fun solveA(text: String): Int {
    val (solvedScanners, _) = solve(text)

    return solvedScanners.values.flatten().toSet().size
}

private fun solve(text: String): Pair<Map<Int, Set<Point3>>, List<Point3>> {
    val zeroPoint = Point3(0, 0, 0)
    val scannerBeacons = text.split("\n\n")
        .map { value -> value.split("\n").drop(1).map { line -> buildPoint(line) }.toSet() }

    val scannerZero = scannerBeacons[0]

    val solvedScanners: MutableMap<Int, Set<Point3>> = mutableMapOf(0 to scannerZero)
    val solvedScannersAdjusted: MutableMap<Int, Map<Point3, Set<Point3>>> = mutableMapOf(0 to adjustPoints(scannerZero))
    val allOrientations = Axis.values().flatMap { it.orientations() }

    val transformedAdjustedScanners = scannerBeacons.drop(1)
        .map { transformForAllOrientations(it, allOrientations) }

    val scannerPositions = Array<Point3?>(scannerBeacons.size) { null }
    scannerPositions[0] = zeroPoint

    val solvedScannersQueue = LinkedList(listOf(0))
    val unknownScanners = (1 until scannerBeacons.size).toMutableList()
    while (scannerPositions.any { it == null }) {
        val solvedScannerIndex = solvedScannersQueue.remove()
        unknownScanners.removeIf { scannerId ->
            val thisScanner = scannerBeacons[scannerId]
            val transformedPoints = transformedAdjustedScanners[scannerId - 1]
            val solution = trySolveScanner(transformedPoints, solvedScannersAdjusted[solvedScannerIndex]!!)

            if (solution != null) {
                val orientation = solution.first
                val offset = solution.second
                val finalPoints = thisScanner.map { orientation.transformPoint(it) - offset }.toSet()
                scannerPositions[scannerId] = zeroPoint - offset
                solvedScanners[scannerId] = finalPoints
                solvedScannersAdjusted[scannerId] = adjustPoints(finalPoints)
                solvedScannersQueue.add(scannerId)
            }
            solution != null
        }
    }
    return solvedScanners to scannerPositions.filterNotNull()
}

fun trySolveScanner(transformedPoints: Map<Orientation, Map<Point3, Set<Point3>>>, solvedScanner: Map<Point3, Set<Point3>>): Pair<Orientation, Point3>? {
    return transformedPoints.entries.firstNotNullOfOrNull { (orientation, points) ->
        val thisPoints = points.keys.take(points.size - 11)
        val otherPoints = solvedScanner.keys.take(solvedScanner.size - 11)

        val firstMatchingPair = thisPoints.flatMap { a -> otherPoints.map { b -> a to b } }
            .firstOrNull { (a, b) -> intersectCount(points[a]!!, solvedScanner[b]!!) >= 12 }

        if (firstMatchingPair == null) null else orientation to firstMatchingPair.first - firstMatchingPair.second
    }
}

fun intersectCount(a: Set<Point3>, b: Set<Point3>): Int = a.count { it in b }

fun transformForAllOrientations(points: Set<Point3>, allOrientations: List<Orientation>): Map<Orientation, Map<Point3, Set<Point3>>> {
    return allOrientations.associateWith { orientation ->
        adjustPoints(points.map { point -> orientation.transformPoint(point) })
    }
}

private fun adjustPoints(allPoints: Collection<Point3>): Map<Point3, Set<Point3>> = allPoints.associateWith { startingPoint ->
    allPoints.map { it - startingPoint }.toSet()
}

data class Orientation(val x: Axis, val y: Axis, val z: Axis) {

    fun transformPoint(point: Point3) = Point3(x.getValue(point), y.getValue(point), z.getValue(point))
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

fun buildPoint(it: String): Point3 {
    val (x, y, z) = it.trim().split(",")
        .map { it.toInt() }
    return Point3(x, y, z)
}

fun solveB(text: String): Int {
    val (_, positions) = solve(text)

    return positions.maxOf { a ->
        positions.maxOf { b ->
            (b - a).abs()
        }
    }
}
