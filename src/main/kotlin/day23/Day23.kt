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

    return solve(Grid(grid))
}

val movementCost = mapOf(
    'A' to 1,
    'B' to 10,
    'C' to 100,
    'D' to 1000
)

const val hallwayY = 1
val amphiYA = listOf(3, 2)
val amphiYB = listOf(5, 4, 3, 2)
val invalidEnds = setOf(Point(3, hallwayY), Point(5, hallwayY), Point(7, hallwayY), Point(9, hallwayY))

val targetPositions = mapOf(
    'A' to amphiYA.map { Point(3, it) },
    'B' to amphiYA.map { Point(5, it) },
    'C' to amphiYA.map { Point(7, it) },
    'D' to amphiYA.map { Point(9, it) },
)
val targetPositionsB = mapOf(
    'A' to amphiYB.map { Point(3, it) },
    'B' to amphiYB.map { Point(5, it) },
    'C' to amphiYB.map { Point(7, it) },
    'D' to amphiYB.map { Point(9, it) },
)

class Grid(private val cells: List<List<Char>>) : List<List<Char>> by cells {
    override fun toString(): String = cells.joinToString(separator = "\n") { it.joinToString(separator = "") } + "\n\nUnsolved = $unsolvedScore"

    override fun equals(other: Any?): Boolean = this === other || other is Grid && cells == other.cells
    override fun hashCode(): Int = cells.hashCode()

    val amphipodPositions = cells.points().filter { cells[it] in setOf('A', 'B', 'C', 'D') }

    val unsolvedScore by lazy {
        points()
            .filter { this[it] in setOf('A', 'B', 'C', 'D') && !hasCorrectPosition(it, this) }
            .sumOf { movementCost[this[it]]!! }
    }

    fun isSolved() = amphipodPositions.all { hasCorrectPosition(it, this) }
}

fun solve(startingGrid: Grid): Int {
    val seenStates = mutableMapOf<Grid, Int>()

    val queue: PriorityQueue<Pair<Grid, Int>> = PriorityQueue(Comparator.comparing { (grid, cost) -> cost + grid.unsolvedScore })
    queue.add(Pair(startingGrid, 0))

    while (queue.isNotEmpty()) {
        val (grid, cumulativeCost) = queue.remove()

        if (grid.isSolved()) {
            return cumulativeCost
        } else if (grid !in seenStates) {
            seenStates[grid] = cumulativeCost

            val canMoveOut = grid.amphipodPositions
                .asSequence()
                .filter { it.y != hallwayY && !hasCorrectPosition(it, grid) }
                .flatMap { findTargetPositionsFromRoom(it, grid, amphi = grid[it]) }
                .filter { paths -> paths.isNotEmpty() }
                .map { steps -> buildPossiblePath(grid, steps) }
                .toList()

            val canMoveIn = grid.amphipodPositions.asSequence()
                .filter { !hasCorrectPosition(it, grid) }
                .mapNotNull { start ->
                    val target = getTargetRoom(grid, start)
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
                    Pair(newGrid(it, grid), it.cost + cumulativeCost)
                }.filter { (newGrid, _) ->
                    newGrid !in seenStates
                }

            queue.addAll(nextGrids)
        }
    }

    throw IllegalStateException("No solution found")
}


fun hasCorrectPosition(point: Point, grid: Grid, amphi: Char = grid[point]): Boolean {
    val targetPositions = targetPositions[amphi]!!

    val innerRoom = targetPositions[0]
    val outerRoom = targetPositions[1]
    return point == innerRoom || (point == outerRoom && grid[innerRoom] == amphi)
}

fun newGrid(path: PossiblePath, grid: List<List<Char>>): Grid {
    val newGrid = grid.map { it.toMutableList() }
    newGrid[path.first] = '.'
    newGrid[path.last] = path.amphi
    return Grid(newGrid)
}

private fun buildPossiblePath(grid: Grid, steps: List<Point>): PossiblePath {
    val amphi = grid[steps.first()]
    return PossiblePath(amphi, steps.first(), steps.last(), (steps.size - 1) * movementCost[amphi]!!)
}

private fun getTargetRoom(grid: Grid, point: Point): Point? {
    val amphi = grid[point]
    val targetPositions = targetPositions[amphi]!!
    return if (targetPositions.all { grid[it] == '.' || grid[it] == amphi }) {
        targetPositions.firstOrNull { grid[it] == '.' }
    } else {
        null
    }
}

fun findTargetPositionsFromRoom(current: Point, grid: Grid, amphi: Char, wipPath: List<Point> = emptyList()): Sequence<List<Point>> {
    val neighbours = current.neighbours()
        .filter { canContinueToNeighbour(amphi, current, it, grid, wipPath) }

    val currentPath = wipPath + current
    return (sequenceOf(currentPath) + neighbours.asSequence().flatMap { findTargetPositionsFromRoom(it, grid, amphi, currentPath) })
        .filter { isValidEndA(it.last(), grid, amphi) }
}

private fun canContinueToNeighbour(amphi: Char, current: Point, neighbour: Point, grid: Grid, wipPath: List<Point>): Boolean {
    val targetPositions = targetPositions[amphi]!!
    val canMoveToTarget = targetPositions.all { targetPos -> grid[targetPos] == '.' || grid[targetPos] == amphi }
    return (neighbour.y == hallwayY || current.y == hallwayY + 2 || neighbour in targetPositions && canMoveToTarget)
            && grid[neighbour] == '.'
            && neighbour !in wipPath
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

fun isValidEndA(end: Point, grid: Grid, amphi: Char): Boolean {
    val roomPosition = (targetPositions[amphi] ?: emptyList()).firstOrNull { grid[it] == '.' }
    return end.y == hallwayY && end !in invalidEnds || end == roomPosition
}

data class PossiblePath(val amphi: Char, val first: Point, val last: Point, val cost: Int)

fun solveB(lines: List<String>): Int {
    val extraLines = listOf("  #D#C#B#A#  ", "  #D#B#A#C#  ")
    return 0
}

