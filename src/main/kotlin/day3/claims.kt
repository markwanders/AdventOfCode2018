package day3

import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val values = readFileLineByLineUsingForEachLine("src/main/resources/day3.txt")
    solution1(values)
}

fun readFileLineByLineUsingForEachLine(fileName: String): Array<String> {
    val values: ArrayList<String> = ArrayList()
    File(fileName).forEachLine {
        values.add(it)
    }
    return values.toTypedArray()
}

fun solution1(input: Array<String>) {
    val claims = input.map { s -> Claim(s) }
    var fabric = Array(1000, { IntArray(1000) })
    claims.forEach {
        for(x in it.startX until it.startX + it.x) {
            for(y in it.startY until it.startY + it.y) {
                fabric[x][y] += 1
            }
        }
    }
    var flattened = fabric.flatMap { it.asList() }
    var count = flattened.count { it > 1 }
    println("Amount of cells with more than one claim: $count")
}

class Claim(val id: Int, val startX: Int, val startY: Int, val x: Int, val y: Int) {
    constructor(input : String) : this(
        "(?<=#)([0-9]*?)(?= @)".toRegex().find(input)?.value.orEmpty().toInt(),
        "(?<=@ )([0-9]*?)(?=,)".toRegex().find(input)?.value.orEmpty().toInt(),
        "(?<=,)([0-9]*?)(?=:)".toRegex().find(input)?.value.orEmpty().toInt(),
        "(?<=: )([0-9]*?)(?=x)".toRegex().find(input)?.value.orEmpty().toInt(),
        "(?<=x)([0-9]*?)(?=$)".toRegex().find(input)?.value.orEmpty().toInt())
}