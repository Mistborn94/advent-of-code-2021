package day23

import helper.point.Point
import helper.point.get
import helper.point.set
import helper.size
import java.util.*
import kotlin.math.abs

fun solveA(lines: List<String>): Int {
    val grid = lines.map { it.toList() }

    return solve(grid, 3 downTo 2)
}

fun solveB(lines: List<String>): Int {
    val extraLines = listOf("  #D#C#B#A#  ", "  #D#B#A#C#  ")
    val newLines = lines.subList(0, 3) + extraLines + lines.subList(3, lines.size)

    return solve(newLines.map { it.toList() }, 5 downTo 2)
}

fun solve(startingGrid: List<List<Char>>, roomsYRange: IntProgression): Int {
    val seenStates = mutableMapOf<Grid, Int>()
    val gridDimensions = GridDimensions(roomsYRange)
    val comparator: Comparator<Pair<Grid, Int>> = Comparator.comparing { (_, cost) -> cost }
    val queue = PriorityQueue(comparator)
    queue.add(Pair(Grid(gridDimensions, startingGrid), 0))

    while (queue.isNotEmpty()) {
        val (grid, cumulativeCost) = queue.remove()

        if (grid.isSolved()) {
            return cumulativeCost
        } else if (grid !in seenStates) {
            seenStates[grid] = cumulativeCost
            val possibleMoves = grid.nextMoves()

            val nextGrids = possibleMoves
                .map { Pair(grid.applyPath(it), it.cost + cumulativeCost) }
                .filter { (newGrid, _) -> newGrid !in seenStates }
                .toSet()

            queue.addAll(nextGrids)
        }
    }

    throw IllegalStateException("No solution found")
}

const val hallwayY = 1
val xRange = 1..11
val invalidEnds = setOf(Point(3, hallwayY), Point(5, hallwayY), Point(7, hallwayY), Point(9, hallwayY))
val movementCost = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)
val amphipodNames = setOf('A', 'B', 'C', 'D')

class GridDimensions(roomsY: IntProgression) {
    val targetRooms = mapOf(
        'A' to roomsY.map { Point(3, it) },
        'B' to roomsY.map { Point(5, it) },
        'C' to roomsY.map { Point(7, it) },
        'D' to roomsY.map { Point(9, it) },
    )
    val possiblePoints = targetRooms.values.flatten() + xRange.map { Point(it, hallwayY) }
}

data class Path(val amphi: Char, val first: Point, val last: Point, val stepCount: Int) {
    val cost = movementCost[amphi]!! * stepCount
}

class Grid(private val gridDimensions: GridDimensions, val cells: List<List<Char>>) {
    private val hashCode = cells.hashCode()
    private val amphipodPositions = calcAmphipodPositions()

    private fun calcAmphipodPositions() = gridDimensions.possiblePoints.filter { cells[it] in amphipodNames }
    operator fun get(point: Point): Char = cells[point]

    fun isSolved() = amphipodPositions.all { isCorrect(it) }

    override fun toString(): String = cells.joinToString(separator = "\n") { it.joinToString(separator = "") }
    override fun equals(other: Any?): Boolean = this === other || other is Grid && cells == other.cells
    override fun hashCode(): Int = hashCode

    fun applyPath(path: Path): Grid {
        val newGrid = cells.map { it.toMutableList() }
        newGrid[path.first] = '.'
        newGrid[path.last] = path.amphi
        return Grid(gridDimensions, newGrid)
    }

    fun nextMoves() = pathsToTargets().take(1).ifEmpty { pathsToHallway() }

    private fun pathsToTargets() = amphipodPositions
        .asSequence()
        .filter { !isCorrect(it) }
        .mapNotNull { calculatePathToEnd(it) }

    private fun pathsToHallway() = amphipodPositions
        .asSequence()
        .filter { it.y != hallwayY && !isCorrect(it) }
        .flatMap { pathsToHallway(it) }

    private fun isCorrect(point: Point): Boolean {
        val amphi = this[point]
        val endPositions = gridDimensions.targetRooms[amphi]!!
        val innerRooms = endPositions.filter { it.y > point.y }
        return point in endPositions && innerRooms.all { this[it] == amphi }
    }


    private fun pathsToHallway(start: Point): List<Path> {
        val hallwayPosition = Point(start.x, hallwayY)
        val firstSegment = calculatePath(start, hallwayPosition)

        val leftX = hallwayPosition.x - 1 downTo xRange.first
        val rightX = hallwayPosition.x + 1..xRange.last

        return if (firstSegment != null) {
            buildList {
                addAll(hallwayPaths(leftX, firstSegment))
                addAll(hallwayPaths(rightX, firstSegment))
            }
        } else {
            emptyList()
        }
    }

    private fun hallwayPaths(xRange: IntProgression, firstSegment: Path) = xRange.asSequence()
        .map { x -> Point(x, hallwayY) }
        .filter { it !in invalidEnds }
        .takeWhile { this[it] == '.' }
        .map { Path(firstSegment.amphi, firstSegment.first, it, firstSegment.stepCount + abs(it.x - firstSegment.first.x)) }

    private fun calculatePathToEnd(start: Point) = getOpenEndRoom(start)?.let { end -> calculatePath(start, end) }
    private fun calculatePath(start: Point, end: Point): Path? {
        val hallway = if (start.x < end.x) start.x + 1..end.x else end.x until start.x
        val endColumn = (hallwayY + 1)..end.y
        val startColumn = hallwayY until start.y

        val hallwayClear = hallway.all { this[Point(it, hallwayY)] == '.' }
        val endColumClear = endColumn.all { this[Point(end.x, it)] == '.' }
        val startColumClear = startColumn.all { this[Point(start.x, it)] == '.' }

        val count = startColumn.size() + hallway.size() + endColumn.size()

        return if (hallwayClear && endColumClear && startColumClear) {
            Path(this[start], start, end, count)
        } else {
            null
        }
    }

    private fun getOpenEndRoom(point: Point): Point? {
        val amphi = this[point]
        val targetPositions = gridDimensions.targetRooms[amphi]!!
        return if (targetPositions.all { this[it] == '.' || this[it] == amphi }) {
            targetPositions.firstOrNull { this[it] == '.' }
        } else {
            null
        }
    }

}

