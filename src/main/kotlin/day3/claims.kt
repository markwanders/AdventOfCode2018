package day3

import java.io.File
import java.util.ArrayList

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
    claims.forEach {
        println("Claim: ${it.id}, ${it.startX}, ${it.startY}, ${it.x}, ${it.y}")
    }
}

class Claim(val id: Int, val startX: Int, val startY: Int, val x: Int, val y: Int) {
    constructor(input : String) : this(
        "(?<=#)([0-9]*?)(?= @)".toRegex().find(input)?.value.orEmpty().toInt(),
        "(?<=@ )([0-9]*?)(?=,)".toRegex().find(input)?.value.orEmpty().toInt(),
        "(?<=,)([0-9]*?)(?=:)".toRegex().find(input)?.value.orEmpty().toInt(),
        "(?<=: )([0-9]*?)(?=x)".toRegex().find(input)?.value.orEmpty().toInt(),
        "(?<=x)([0-9]*?)(?=$)".toRegex().find(input)?.value.orEmpty().toInt())
}