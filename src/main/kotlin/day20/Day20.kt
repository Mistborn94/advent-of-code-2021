package day20

import helper.digitsToInt
import helper.point.Point
import helper.point.contains
import helper.point.get
import helper.point.points

fun solve(lines: List<String>, iterations: Int = 50): Int {
    val algorithm = lines.first().map { if (it == '.') 0 else 1 }

    val image = lines.drop(2)
        .map { line -> line.map { if (it == '.') 0 else 1 } }

    return (0 until iterations).fold(image to 0) { (image, colour), _ ->
        runAlgorithm(algorithm, pad(image, colour), colour)
    }.first.sumOf { it.sum() }
}

fun runAlgorithm(algorithm: List<Int>, image: List<List<Int>>, infiniteColour: Int): Pair<List<List<Int>>, Int> {
    val rows = image.points()
        .groupBy { it.y }

    val nextInfiniteColour = if (infiniteColour == 0) algorithm.first() else algorithm.last()

    return buildList {
        for (row in rows.values) {
            add(buildRow(row, image, algorithm, infiniteColour))
        }
    } to nextInfiniteColour
}

val pointComparator: Comparator<Point> = Comparator.comparing<Point?, Int?> { point -> point.y }.thenComparing { point -> point.x }
fun buildRow(row: List<Point>, image: List<List<Int>>, algorithm: List<Int>, infiniteColor: Int): List<Int> = buildList {
    addAll(row.map { point -> algorithm[transformPoint(point, image, infiniteColor)] })
}

private fun transformPoint(point: Point, image: List<List<Int>>, infiniteColor: Int) =
    (point.neighbours() + point.diagonalNeighbours() + point).sortedWith(pointComparator).map { neighbour -> if (neighbour in image) image[neighbour] else infiniteColor }.digitsToInt(2)

fun pad(image: List<List<Int>>, padValue: Int): List<List<Int>> {
    val padAmount = 1
    val newWidth = image.first().size + padAmount * 2

    val blankRow = Array(newWidth) { padValue }.toList()
    val blankSegment = Array(padAmount) { padValue }.toList()

    return buildList {
        repeat(padAmount) {
            add(blankRow)
        }
        for (row in image) {
            add(blankSegment + row + blankSegment)
        }
        repeat(padAmount) {
            add(blankRow)
        }

    }
}
