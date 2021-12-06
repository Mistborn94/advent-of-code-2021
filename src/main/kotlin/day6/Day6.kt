package day6

fun solve(lines: String, days: Int): Long {
    val initialFish = lines.trim().split(",").map { it.toInt() }
        .groupingBy { it }
        .eachCount()
        .mapValues { (_, value) -> value.toLong() }

    val fish = (1..days).fold(initialFish) { fish, _ ->
        val newFish = fish.mapKeys { (key, _) -> key - 1 }.toMutableMap()

        newFish[8] = newFish.getOrDefault(-1, 0)
        newFish[6] = newFish.getOrDefault(6, 0) + newFish.getOrDefault(-1, 0)
        newFish.remove(-1)

        newFish
    }
    return fish.values.sumOf { it }
}

