package day6

fun solve(lines: String, days: Int): Long {
    var fish = lines.trim().split(",").map { it.toInt() }
        .groupingBy { it }
        .eachCount()
        .mapValues { (_, value) -> value.toLong() }

    repeat(days) {
        val newCount = fish[0] ?: 0
        val newFish = fish.mapKeys { (key, _) -> key - 1 }.toMutableMap()

        newFish[8] = newCount
        newFish[6] = newFish.getOrDefault(6, 0) + newFish.getOrDefault(-1, 0)
        newFish.remove(-1)

        fish = newFish
    }
    return fish.values.sumOf { it }
}

