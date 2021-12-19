package day19

import helper.point.HyperspacePoint

typealias Point = HyperspacePoint


fun solveA(text: String): Int {

    val (solvedScanners, _) = solve(text)

    return solvedScanners.values.flatten().toSet().size
}

private fun solve(text: String): Pair<Map<Int, Set<Point>>, List<Point>> {
    val scannerBeacons = text.split("\n\n")
        .map { value -> value.split("\n").drop(1).map { line -> buildPoint(line) }.toSet() }

    val scannerZero = scannerBeacons[0]

    val solvedScanners: MutableMap<Int, Set<Point>> = mutableMapOf(0 to scannerZero)
    val solvedScannersAdjusted: MutableMap<Int, Map<Point, Set<Point>>> = mutableMapOf(0 to adjustPoints(scannerZero))
    val unknownScanners = (1 until scannerBeacons.size).toMutableSet()
    val allOrientations = Axis.values().flatMap { it.orientations() }

    val transformedAdjustedScanners = scannerBeacons.drop(1)
        .map { transformForAllOrientations(it, allOrientations) }

    val scannerPositions = Array<Point?>(scannerBeacons.size) { null }
    val scannerOrientations = Array<Orientation?>(scannerBeacons.size) { null }
    scannerPositions[0] = Point(intArrayOf(0, 0, 0))
    scannerOrientations[0] = Orientation(Axis.XP, Axis.YP, Axis.ZP)

    //New Algorithm to try
    //Adjust each scanner (except 0) for all 24 orientations
    //Find constant differences between all points for every scanner
    //Work form there
    while (unknownScanners.isNotEmpty()) {
        val removed = unknownScanners.removeIf { scannerId ->
            val thisScanner = scannerBeacons[scannerId];
            val transformedPoints = transformedAdjustedScanners[scannerId - 1]
            val solution = solvedScannersAdjusted.values.firstNotNullOfOrNull { solvedScanner -> trySolveScanner(transformedPoints, solvedScanner) }

            if (solution != null) {
                val orientation = solution.first
                val offset = solution.second
                val finalPoints = thisScanner.map { orientation.transformPoint(it) - offset }.toSet()
                scannerOrientations[scannerId] = orientation
                scannerPositions[scannerId] = HyperspacePoint.zero(3) - offset
                solvedScanners[scannerId] = finalPoints
                solvedScannersAdjusted[scannerId] = adjustPoints(finalPoints)
            }
            solution != null
        }

        if (!removed && unknownScanners.isNotEmpty()) {
            throw IllegalStateException("infinite loop probably")
        }
    }
    return solvedScanners to scannerPositions.filterNotNull()
}

fun trySolveScanner(transformedPoints: Map<Orientation, Map<Point, Set<Point>>>, solvedScanner: Map<Point, Set<Point>>): Pair<Orientation, Point>? {
    return transformedPoints.entries.firstNotNullOfOrNull { (orientation, points) ->
        val thisPoints = points.keys
        val otherPoints = solvedScanner.keys

        val firstMatchingPair = thisPoints.flatMap { a -> otherPoints.map { b -> a to b } }
            .firstOrNull { (a, b) -> intersectCount(points[a]!!, solvedScanner[b]!!) >= 12 }

        if (firstMatchingPair == null) null else orientation to firstMatchingPair.first - firstMatchingPair.second
    }
}

fun intersectCount(a: Set<Point>, b: Set<Point>): Int = a.count { it in b }

fun transformForAllOrientations(points: Set<Point>, allOrientations: List<Orientation>): Map<Orientation, Map<Point, Set<Point>>> {
    return allOrientations.associateWith { orientation ->
        adjustPoints(points.map { point -> orientation.transformPoint(point) })
    }
}

private fun adjustPoints(allPoints: Collection<Point>): Map<Point, Set<Point>> = allPoints.associateWith { startingPoint ->
    allPoints.map { it - startingPoint }.toSet()
}

data class Orientation(val x: Axis, val y: Axis, val z: Axis) {

    fun transformPoint(point: Point) = Point.of(x.getValue(point), y.getValue(point), z.getValue(point))
}

enum class Axis {
    XP {
        override fun getValue(point: Point) = point.x
        override fun orientations(): List<Orientation> = listOf(
            Orientation(ZN, YP, this),
            Orientation(YN, ZN, this),
            Orientation(ZP, YN, this),
            Orientation(YP, ZP, this)
        )
    },
    XN {
        override fun getValue(point: Point) = -point.x
        override fun orientations(): List<Orientation> = listOf(
            Orientation(YP, ZN, this),
            Orientation(ZN, YN, this),
            Orientation(YN, ZP, this),
            Orientation(ZP, YP, this)
        )
    },
    YP {
        override fun getValue(point: Point) = point.y
        override fun orientations(): List<Orientation> = listOf(
            Orientation(XP, ZN, this),
            Orientation(ZN, XN, this),
            Orientation(XN, ZP, this),
            Orientation(ZP, XP, this),
        )
    },
    YN {
        override fun getValue(point: Point) = -point.y
        override fun orientations(): List<Orientation> = listOf(
            Orientation(XN, ZN, this),
            Orientation(ZN, XP, this),
            Orientation(XP, ZP, this),
            Orientation(ZP, XN, this),
        )
    },
    ZN {
        override fun getValue(point: Point) = -point.z
        override fun orientations(): List<Orientation> = listOf(
            Orientation(XN, YP, this),
            Orientation(YP, XP, this),
            Orientation(XP, YN, this),
            Orientation(YN, XN, this),
        )
    },
    ZP {
        override fun getValue(point: Point) = point.z
        override fun orientations(): List<Orientation> = listOf(
            Orientation(XP, YP, this),
            Orientation(YN, XP, this),
            Orientation(XN, YN, this),
            Orientation(YP, XN, this),
        )
    };

    abstract fun getValue(point: Point): Int
    abstract fun orientations(): List<Orientation>
}

fun buildPoint(it: String): Point {
    return Point(it.trim().split(",")
        .map { it.toInt() }
        .toIntArray()
    )
}

fun solveB(text: String): Int {
    val (_, positions) = solve(text)

    return positions.maxOf { a ->
        positions.maxOf { b ->
            (b - a).abs()
        }
    }
}
