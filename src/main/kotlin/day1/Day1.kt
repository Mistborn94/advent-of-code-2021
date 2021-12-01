package day1

fun solveA(text: String): Int {
    return text.lines()
        .map { it.trim().toInt() }
        .zipWithNext()
        .count { (a, b) -> b > a }
}


fun solveB(text: String): Int {
    return text.lines()
        .asSequence()
        .map { it.trim().toInt() }
        .windowed(3)
        .map { it.sum() }
        .zipWithNext()
        .count { (a, b) -> b > a }
}
