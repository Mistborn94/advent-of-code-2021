package day25

import helper.point.*

//Every step, the sea cucumbers in the east-facing herd attempt to move forward one location, then the sea cucumbers in the south-facing herd attempt to move forward one location.
//During a single step, the east-facing herd moves first, then the south-facing herd moves.
fun solveA(lines: List<String>): Int {
    var map = Map(lines.map { it.toList() })
    var step = 0
    do {
        step += 1
        val (eastCount, eastMap) = map.moveEast()
        val (southCount, southMap) = eastMap.moveSouth()
        map = southMap
    } while (eastCount + southCount > 0)
    return step
}


class Map(val chars: List<List<Char>>) : List<List<Char>> by chars {

    fun normalizePoint(point: Point): Point {
        return if (point in chars) {
            point
        } else {
            val x = (point.x + chars.first().size) % chars.first().size
            val y = (point.y + chars.size) % chars.size
            Point(x, y)
        }
    }

    fun moveEast(): Pair<Int, Map> {
        val newMap = chars.map { it.toMutableList() }
        val points = chars.points().map { it to normalizePoint(Point(it.x + 1, it.y)) }
            .filter { (origin, target) ->
                chars[origin] == '>' && chars[target] == '.'
            }.onEach { (origin, target) ->
                newMap[origin] = '.'
                newMap[target] = '>'
            }
        return points.size to Map(newMap)
    }

    fun moveSouth(): Pair<Int, Map> {
        val newMap = chars.map { it.toMutableList() }
        val points = chars.points().map { it to normalizePoint(Point(it.x, it.y + 1)) }
            .filter { (origin, target) ->
                chars[origin] == 'v' && chars[target] == '.'
            }.onEach { (origin, target) ->
                newMap[origin] = '.'
                newMap[target] = 'v'
            }
        return points.size to Map(newMap)
    }

}