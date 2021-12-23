package day23

import helper.point.*
import java.util.*

/*
Amphipods will never stop on the space immediately outside any room. They can move into that space so long as they immediately continue moving. (Specifically, this refers to the four open spaces in the hallway that are directly above an amphipod starting position.)
Amphipods will never move from the hallway into a room unless that room is their destination room and that room contains no amphipods which do not also have that room as their own destination.
Once an amphipod stops moving in the hallway, it will stay in that spot until it can move into a room.
    (So each amphibot has two turns)

Amber amphipods require 1 energy per step,
Bronze amphipods require 10 energy
Copper amphipods require 100
Desert ones require 1000
 */
fun solveA(lines: List<String>): Int {
    val grid = lines.map { it.toList() }

    return solve(grid, TargetPositionsA)
}

const val hallwayY = 1
val invalidEnds = setOf(Point(3, hallwayY), Point(5, hallwayY), Point(7, hallwayY), Point(9, hallwayY))
val movementCost = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)

sealed interface TargetPositions {
    fun hasCorrectPosition(point: Point, grid: Grid, amphi: Char = grid[point]): Boolean
    fun getReachableEndRoom(grid: Grid, point: Point): Point?
    fun findPossiblePositionsFromRoom(current: Point, grid: Grid, amphi: Char, wipPath: List<Point> = emptyList()): Sequence<List<Point>>
}

object TargetPositionsA : TargetPositions {
    private val targetY = listOf(3, 2)
    private val targetPositions = mapOf(
        'A' to targetY.map { Point(3, it) },
        'B' to targetY.map { Point(5, it) },
        'C' to targetY.map { Point(7, it) },
        'D' to targetY.map { Point(9, it) },
    )

    override fun hasCorrectPosition(point: Point, grid: Grid, amphi: Char): Boolean {
        val targetPositions = targetPositions[amphi]!!

        val innerRoom = targetPositions[0]
        val outerRoom = targetPositions[1]
        return point == innerRoom || (point == outerRoom && grid[innerRoom] == amphi)
    }

    override fun getReachableEndRoom(grid: Grid, point: Point): Point? {
        val amphi = grid[point]
        val targetPositions = targetPositions[amphi]!!
        return if (targetPositions.all { grid[it] == '.' || grid[it] == amphi }) {
            targetPositions.firstOrNull { grid[it] == '.' }
        } else {
            null
        }
    }

    private fun canContinueToNeighbour(amphi: Char, current: Point, neighbour: Point, grid: Grid, wipPath: List<Point>): Boolean {
        val targetPositions = targetPositions[amphi]!!
        val canMoveToTarget = targetPositions.all { targetPos -> grid[targetPos] == '.' || grid[targetPos] == amphi }
        return (neighbour.y == hallwayY || current.y > neighbour.y || neighbour in targetPositions && canMoveToTarget)
                && grid[neighbour] == '.'
                && neighbour !in wipPath
    }

    private fun isValidEnd(end: Point, grid: Grid, amphi: Char): Boolean {
        val roomPosition = targetPositions[amphi]!!.firstOrNull { grid[it] == '.' }
        return end.y == hallwayY && end !in invalidEnds || end == roomPosition
    }

    override fun findPossiblePositionsFromRoom(current: Point, grid: Grid, amphi: Char, wipPath: List<Point>): Sequence<List<Point>> {
        val neighbours = current.neighbours()
            .filter { canContinueToNeighbour(amphi, current, it, grid, wipPath) }

        val currentPath = wipPath + current
        return (sequenceOf(currentPath) + neighbours.asSequence().flatMap { findPossiblePositionsFromRoom(it, grid, amphi, currentPath) })
            .filter { isValidEnd(it.last(), grid, amphi) }
    }

}

object TargetPositionsB : TargetPositions {
    private val targetY = listOf(5, 4, 3, 2)
    private val targetPositions = mapOf(
        'A' to targetY.map { Point(3, it) },
        'B' to targetY.map { Point(5, it) },
        'C' to targetY.map { Point(7, it) },
        'D' to targetY.map { Point(9, it) },
    )

    override fun hasCorrectPosition(point: Point, grid: Grid, amphi: Char): Boolean {
        val targetPositions = targetPositions[amphi]!!
        val innerRooms = targetPositions.filter { it.y > point.y }
        val outerRooms = targetPositions.filter { it.y < point.y }
        return point in targetPositions && innerRooms.all { grid[it] == amphi } && outerRooms.all { grid[it] == amphi || grid[it] == '.' }
    }

    override fun getReachableEndRoom(grid: Grid, point: Point): Point? {
        val amphi = grid[point]
        val targetPositions = targetPositions[amphi]!!
        return if (targetPositions.all { grid[it] == '.' || grid[it] == amphi }) {
            targetPositions.firstOrNull { grid[it] == '.' }
        } else {
            null
        }
    }

    private fun canContinueToNeighbour(amphi: Char, current: Point, neighbour: Point, grid: Grid, wipPath: List<Point>): Boolean {
        val targetPositions = targetPositions[amphi]!!
        val canMoveToTarget = targetPositions.all { targetPos -> grid[targetPos] == '.' || grid[targetPos] == amphi }
        return (neighbour.y == hallwayY || current.y > neighbour.y || neighbour in targetPositions && canMoveToTarget)
                && grid[neighbour] == '.'
                && neighbour !in wipPath
    }

    private fun isValidEnd(end: Point, grid: Grid, amphi: Char): Boolean {
        val roomPosition = (targetPositions[amphi] ?: emptyList()).firstOrNull { grid[it] == '.' }
        return end.y == hallwayY && end !in invalidEnds || end == roomPosition
    }

    override fun findPossiblePositionsFromRoom(current: Point, grid: Grid, amphi: Char, wipPath: List<Point>): Sequence<List<Point>> {
        val neighbours = current.neighbours()
            .filter { canContinueToNeighbour(amphi, current, it, grid, wipPath) }

        val currentPath = wipPath + current
        return (sequenceOf(currentPath) + neighbours.asSequence().flatMap { findPossiblePositionsFromRoom(it, grid, amphi, currentPath) })
            .filter { isValidEnd(it.last(), grid, amphi) }
    }
}


class Grid(private val targetPositions: TargetPositions, private val cells: List<List<Char>>) : List<List<Char>> by cells {
    override fun toString(): String = cells.joinToString(separator = "\n") { it.joinToString(separator = "") } + "\n\nUnsolved = $unsolvedScore"

    override fun equals(other: Any?): Boolean = this === other || other is Grid && cells == other.cells
    override fun hashCode(): Int = cells.hashCode()

    val amphipodPositions = cells.points().filter { cells[it] in setOf('A', 'B', 'C', 'D') }

    val unsolvedScore by lazy {
        points()
            .filter { this[it] in setOf('A', 'B', 'C', 'D') && !targetPositions.hasCorrectPosition(it, this) }
            .sumOf { movementCost[this[it]]!! }
    }

    fun isSolved() = amphipodPositions.all { targetPositions.hasCorrectPosition(it, this) }
}

fun solve(startingGrid: List<List<Char>>, targetPositions: TargetPositions): Int {
    val seenStates = mutableMapOf<Grid, Int>()

    val queue: PriorityQueue<Pair<Grid, Int>> = PriorityQueue(Comparator.comparing { (grid, cost) -> cost + grid.unsolvedScore })
    queue.add(Pair(Grid(targetPositions, startingGrid), 0))

    while (queue.isNotEmpty()) {
        val (grid, cumulativeCost) = queue.remove()

        if (grid.isSolved()) {
            return cumulativeCost
        } else if (grid !in seenStates) {
            seenStates[grid] = cumulativeCost

            val canMoveOut = grid.amphipodPositions
                .asSequence()
                .filter { it.y != hallwayY && !targetPositions.hasCorrectPosition(it, grid) }
                .flatMap { targetPositions.findPossiblePositionsFromRoom(it, grid, amphi = grid[it]) }
                .filter { paths -> paths.isNotEmpty() }
                .map { steps -> buildPossiblePath(grid, steps) }
                .toList()

            val canMoveIn = grid.amphipodPositions.asSequence()
                .filter { !targetPositions.hasCorrectPosition(it, grid) }
                .mapNotNull { start ->
                    val target = targetPositions.getReachableEndRoom(grid, start)
                    if (target == null) {
                        null
                    } else {
                        val stepCount = stepCountToTargetPosition(start, grid, target)
                        if (stepCount == null) {
                            null
                        } else {
                            val amphi = grid[start]
                            PossiblePath(amphi, start, target, stepCount * movementCost[amphi]!!)
                        }
                    }
                }.toList()

            val nextGrids = (canMoveIn + canMoveOut)
                .toSet()
                .map {
                    Pair(newGrid(it, grid, targetPositions), it.cost + cumulativeCost)
                }.filter { (newGrid, _) ->
                    newGrid !in seenStates
                }

            queue.addAll(nextGrids)
        }
    }

    throw IllegalStateException("No solution found")
}

fun newGrid(path: PossiblePath, grid: List<List<Char>>, targetPositions: TargetPositions): Grid {
    val newGrid = grid.map { it.toMutableList() }
    newGrid[path.first] = '.'
    newGrid[path.last] = path.amphi
    return Grid(targetPositions, newGrid)
}

private fun buildPossiblePath(grid: Grid, steps: List<Point>): PossiblePath {
    val amphi = grid[steps.first()]
    return PossiblePath(amphi, steps.first(), steps.last(), (steps.size - 1) * movementCost[amphi]!!)
}

fun stepCountToTargetPosition(start: Point, grid: Grid, targetRoom: Point): Int? {
    val seen = mutableMapOf(start to 0)
    val queue = PriorityQueue<Pair<Point, Int>>(Comparator.comparing { (_, second) -> second })
    queue.add(start to 0)
    while (queue.isNotEmpty() && !seen.containsKey(targetRoom)) {
        val (next, stepCount) = queue.remove()
        seen[next] = stepCount

        val neighbours = next.neighbours()
            .filter { it in grid && grid[it] == '.' && it !in seen }
            .map { it to stepCount + 1 }
        queue.addAll(neighbours)
    }

    return seen[targetRoom]
}


data class PossiblePath(val amphi: Char, val first: Point, val last: Point, val cost: Int)

fun solveB(lines: List<String>): Int {
    val extraLines = listOf("  #D#C#B#A#  ", "  #D#B#A#C#  ")
    val newLines = lines.subList(0, 3) + extraLines + lines.subList(3, lines.size)

    return solve(newLines.map { it.toList() }, TargetPositionsB)
}

