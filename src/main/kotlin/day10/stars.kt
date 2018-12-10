package day10

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day10.txt")
    input.forEach {
        println(it)
    }
}

fun readFile(fileName: String): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    val values: ArrayList<Pair<Pair<Int, Int>, Pair<Int, Int>>> = ArrayList()
    File(fileName).forEachLine { s ->
        val ints = "-?\\d+".toRegex().findAll(s).map { it.value.toInt() }.toList()
        values.add(Pair(Pair(ints[0], ints[1]), Pair(ints[2], ints[3])))
    }
    return values.toList()
}