package day11

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day11.txt")
    solution1(input)
}

fun readFile(fileName: String):Int {
    val raw = File(fileName).readText()
    return "\\d+".toRegex().find(raw)!!.value.toInt()
}

fun solution1(input: Int) {
    val squares = mutableMapOf<Pair<Int, Int>, Int>()
    for(y in 1 until 301) {
        for(x in 1 until 301) {
            var total = 0
            if(x + 3 <= 300 && y + 3 <= 300) {
                for (ySquare in y until y + 3) {
                    for (xSquare in x until x + 3) {
                        val rackID = xSquare + 10
                        var power = rackID * ySquare
                        power += input
                        power *= rackID
                        if(power >= 100) {
                            val chars = power.toString().toCharArray()
                            chars.reverse()
                            val hundreds = chars[2].toString().toInt()
                            total += hundreds - 5
                        } else {
                            total -= 5
                        }
                    }
                }
            }
            squares[Pair(x, y)] = total
        }
    }
    println("3x3 square with max power starts at position: ${squares.maxBy { it.value }!!.key} with a total power of ${squares.maxBy { it.value }!!.value}")
}