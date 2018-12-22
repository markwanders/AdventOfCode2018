package day22

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/test.txt")
    solution1(input)
}

fun readFile(fileName: String): List<Int> {
    val raw = File(fileName).readText()
    return "\\d+".toRegex().findAll(raw).map { it.value.toInt() }.toList()
}

fun solution1(input: List<Int>) {
    val depth = input[0]
    val target = input[1] to input[2]
    val cave = Array(target.second + 1) { Array(target.first + 1) { Region(0, 0, ' ') } }
    for (y in 0 until target.second + 1) {
        for (x in 0 until target.first + 1) {
            val geologicalIndex = if (x == 0 && y == 0) {
                0
            } else if (x == target.first && y == target.second) {
                0
            } else if (y == 0) {
                x.times(16807)
            } else if (x == 0) {
                y.times(48271)
            } else {
                cave[y][x - 1].erosionLevel.times(cave[y - 1][x].erosionLevel)
            }
            val erosionLevel = (geologicalIndex + depth) % 20183
            val type = when {
                erosionLevel % 3 == 0 -> '.'
                erosionLevel % 3 == 1 -> '='
                erosionLevel % 3 == 2 -> '|'
                else -> {
                    'X'
                }
            }
            cave[y][x] = Region(geologicalIndex, erosionLevel, type)
        }
    }
    cave.forEach {
        it.forEach {
            print(it.type)
        }
        println()
    }
    var riskRating = 0
    cave.forEach {
        it.forEach {
            riskRating += when(it.type) {
                '.' -> 0
                '=' -> 1
                '|' -> 2
                else -> {
                    0
                }
            }
        }
    }
    println("Risk rating is $riskRating")
}

data class Region(val geologicalIndex: Int, val erosionLevel: Int, val type: Char)