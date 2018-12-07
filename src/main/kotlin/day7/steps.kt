package day7

import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val instructions = readFile("src/main/resources/day7.txt")
}

fun readFile(fileName: String): Array<String> {
    val values: ArrayList<String> = ArrayList()
    File(fileName).forEachLine {
        values.add(it)
    }
    return values.toTypedArray()
}