package day3

import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val values = readFileLineByLineUsingForEachLine("src/main/resources/day3.txt")
    solution1(values)
    solution2(values)
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
    val fabric = Array(1000, { IntArray(1000) })
    claims.forEach {
        for (x in it.startX until it.startX + it.x) {
            for (y in it.startY until it.startY + it.y) {
                fabric[x][y] += 1
            }
        }
    }
    val flattened = fabric.flatMap { it.asList() }
    val count = flattened.count { it > 1 }
    println("Amount of cells with more than one claim: $count")
}

fun solution2(input: Array<String>) {
    val claims = input.map { s -> Claim(s) }
    val fabric = Array(1000, { IntArray(1000) })
    claims.forEach {
        for (x in it.startX until it.startX + it.x) {
            for (y in it.startY until it.startY + it.y) {
                if (fabric[x][y] == 0) {
                    fabric[x][y] = 1
                } else {
                    fabric[x][y] = -1
                }
            }
        }
    }
    claims.forEach {
        var claimed = 0
        val required = (it.x * it.y)
        for (x in it.startX until it.startX + it.x) {
            for (y in it.startY until it.startY + it.y) {
                if (fabric[x][y] == 1) {
                    claimed++
                }
            }
        }
        if (claimed == required) {
            println("Cells claimed equal to cells required for id: ${it.id}")
        }
    }

}

class Claim(val id: Int, val startX: Int, val startY: Int, val x: Int, val y: Int) {
    constructor(input: String) : this(
        "(?<=#)([0-9]*?)(?= @)".toRegex().find(input)?.value.orEmpty().toInt(),
        "(?<=@ )([0-9]*?)(?=,)".toRegex().find(input)?.value.orEmpty().toInt(),
        "(?<=,)([0-9]*?)(?=:)".toRegex().find(input)?.value.orEmpty().toInt(),
        "(?<=: )([0-9]*?)(?=x)".toRegex().find(input)?.value.orEmpty().toInt(),
        "(?<=x)([0-9]*?)(?=$)".toRegex().find(input)?.value.orEmpty().toInt()
    )
}