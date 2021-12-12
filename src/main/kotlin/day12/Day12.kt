package day12

fun solveA(lines: List<String>): Int {
    val caveMap = parseInput(lines)

    return findPathCountA(caveMap["start"]!!)
}

private fun parseInput(lines: List<String>): MutableMap<String, Cave> {
    val caveMap = mutableMapOf<String, Cave>()

    lines.map { it.split("-") }.forEach { (from, to) ->
        val fromCave = caveMap.getOrPut(from) { Cave(from) }
        val toCave = caveMap.getOrPut(to) { Cave(to) }

        fromCave.next.add(toCave)
        toCave.next.add(fromCave)
    }
    return caveMap
}

fun findPathCountA(cave: Cave, visited: Set<String> = emptySet()): Int {
    val possibleNext = cave.next.filter { it.big || it.name !in visited }

    return if (cave.name == "end") {
        1
    } else if (possibleNext.isEmpty()) {
        0
    } else {
        val newVisited = visited + cave.name
        possibleNext.sumOf {
            findPathCountA(it, newVisited)
        }
    }
}

fun findPathCountB(cave: Cave, visited: Set<Cave> = emptySet(), twiceVisited: Boolean = false): Int {
    val possibleNext = cave.next.filter { (it.big || !twiceVisited || it !in visited) && it.name != "start" }

    return if (cave.name == "end") {
        1
    } else if (possibleNext.isEmpty()) {
        0
    } else {
        val newVisited = visited + cave
        possibleNext.sumOf {
            val newTwiceVisited = twiceVisited || (!it.big && it in visited)
            findPathCountB(it, newVisited, newTwiceVisited)
        }
    }
}


fun solveB(lines: List<String>): Int {
    val caveMap = parseInput(lines)

    return findPathCountB(caveMap["start"]!!)
}

data class Cave(val name: String) {
    val big = name[0] in 'A'..'Z'
    val next: MutableList<Cave> = mutableListOf()
}