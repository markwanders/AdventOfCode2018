package day15

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day15.txt")
    solution1(input)
}

fun readFile(fileName: String): MutableList<CharArray> {
    val battlefield = mutableListOf<CharArray>()
    File(fileName).forEachLine {
        battlefield.add(it.toCharArray())
    }
    return battlefield
}

fun solution1(battlefield: MutableList<CharArray>) {
    battlefield.forEach { line ->
        line.forEach {
            print(it)
        }
        println()
    }
}