package day2

fun solveA(text: String): Int {
    var depth = 0
    var horizontalDistance = 0
    text.lines().map { it.split(" ") }
        .forEach { (direction, distance) ->
            when (direction) {
                "forward" -> horizontalDistance += distance.toInt()
                "down" -> depth += distance.toInt()
                "up" -> depth -= distance.toInt()
            }
        }
    return depth * horizontalDistance
}

fun solveB(text: String): Int {
    var aim = 0
    var depth = 0
    var horizontalDistance = 0
    text.lines().map { it.split(" ") }
        .forEach { (direction, distance) ->
            when (direction) {
                "forward" -> {
                    horizontalDistance += distance.toInt()
                    depth += aim * distance.toInt()
                }
                "down" -> aim += distance.toInt()
                "up" -> aim -= distance.toInt()
            }
        }
    return depth * horizontalDistance
}
