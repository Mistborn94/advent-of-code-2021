package helper

import java.io.File

fun readDayFile(day: Int, file: String) = File("src/main/kotlin/day$day/$file")