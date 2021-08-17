package day11

import java.io.File

val input = readFile("src/main/resources/day11.txt")

fun main(args: Array<String>) {
//    solution1()
    solution2()
}

fun readFile(fileName: String): Int {
    val raw = File(fileName).readText()
    return "\\d+".toRegex().find(raw)!!.value.toInt()
}

fun solution1() {
    val squares = mutableMapOf<Pair<Int, Int>, Int>()
    for (y in 1 until 301) {
        for (x in 1 until 301) {
            var total = 0
            if (x + 3 <= 300 && y + 3 <= 300) {
                for (ySquare in y until y + 3) {
                    for (xSquare in x until x + 3) {
                        total += power(xSquare, ySquare)
                    }
                }
            }
            squares[Pair(x, y)] = total
        }
    }
    println("3x3 square with max power starts at position: ${squares.maxBy { it.value }!!.key} with a total power of ${squares.maxBy { it.value }!!.value}")
}

fun solution2() {
    val grid = Array(301) { IntArray(301) }
    for (y in 1 until 300) {
        for (x in 1 until 300) {
            grid[x][y] = power(x + 1, y + 1)
        }
    }

    val maximaPerCoordinate = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
    for (y in grid.indices) {
        for (x in grid.indices) {

            val totalsBySizeForThisCoordinate = mutableMapOf<Int, Int>()
            totalsBySizeForThisCoordinate[0] = 0
            for (size in 1 until grid.size) {

                if (grid.size > x + size && grid.size > y + size) {
                    var newTotal = totalsBySizeForThisCoordinate[size - 1]!!

                    for (ySquare in y  until y + size) {
                        newTotal += grid[x + size - 1][ySquare]
                    }
                    for (xSquare in x until x + size) {
                        newTotal += grid[xSquare][y + size - 1]
                    }
                    totalsBySizeForThisCoordinate[size] = newTotal
                }
            }
            val maxForThisCoordinate = totalsBySizeForThisCoordinate.maxBy { it.value }!!
            maximaPerCoordinate[Pair(x, y)] = Pair(maxForThisCoordinate.key, maxForThisCoordinate.value)
        }

    }
    val solution = maximaPerCoordinate.maxBy { it.value.second }!!
    println("Maximum fuel at x=${solution.key.first + 1}, y=${solution.key.second + 1}, ${solution.value.first}x${solution.value.first}")
}

fun power(x: Int, y: Int): Int {
    var total = 0
    val rackID = x + 10
    var power = rackID * y
    power += input
    power *= rackID
    if (power >= 100) {
        val chars = power.toString().toCharArray()
        chars.reverse()
        val hundreds = chars[2].toString().toInt()
        total += hundreds - 5
    } else {
        total -= 5
    }
    return total
}