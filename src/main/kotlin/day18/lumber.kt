package day18

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day18.txt")
    solution1and2(input)
}

fun readFile(fileName: String): ArrayList<ArrayList<Char>> {
    val output = arrayListOf(ArrayList<Char>())
    File(fileName).forEachLine { line ->
        if (output[0].isEmpty()) {
            output[0] = line.toCharArray().toCollection(ArrayList())
        } else {
            output.add(line.toCharArray().toCollection(ArrayList()))
        }
    }
    return output
}

fun solution1and2(input: ArrayList<ArrayList<Char>>) {

    var yard = input
    val results = HashMap<Int, Int>()
    var it = 0
    var offset = 0
    var patternStart = 0
    var repeat = false
    while (!repeat) {
        val newYard = yard.map { it.filter { true }.toCollection(ArrayList()) }.toCollection(ArrayList())

        yard.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                val surrounding = mutableListOf<Char>()
                if (y > 0 && x > 0) surrounding.add(yard[y - 1][x - 1])
                if (y < line.size - 1 && x < line.size - 1) surrounding.add(yard[y + 1][x + 1])
                if (y > 0 && x < line.size - 1) surrounding.add(yard[y - 1][x + 1])
                if (x > 0 && y < line.size - 1) surrounding.add(yard[y + 1][x - 1])
                if (x > 0) surrounding.add(yard[y][x - 1])
                if (x < line.size - 1) surrounding.add(yard[y][x + 1])
                if (y > 0) surrounding.add(yard[y - 1][x])
                if (y < line.size - 1) surrounding.add(yard[y + 1][x])
                if (char == '.') {
                    if (surrounding.count { it == '|' } >= 3) {
                        newYard[y][x] = '|'
                    }
                }
                if (char == '|') {
                    if (surrounding.count { it == '#' } >= 3) {
                        newYard[y][x] = '#'
                    }
                }
                if (char == '#') {
                    if (!(surrounding.any { it == '#' } && surrounding.any { it == '|' })) {
                        newYard[y][x] = '.'
                    }
                }
            }
        }
        yard = newYard

        val thisResult = yard.flatten().count { it == '#' } * yard.flatten().count { it == '|' }

        if (results.containsValue(thisResult)) {
            val previousIndexOfValue = results.filter { it.value == thisResult }.maxBy { it.key }!!.key
            if (it - previousIndexOfValue == offset && results[it - offset] == thisResult && results[it - 2 * offset] == thisResult) {
                repeat = true
                patternStart = it - 2 * offset
            } else {
                offset = it - previousIndexOfValue
            }
        }
        results[it] = thisResult
        it++
    }
    println("Solution 1: ${results[9]}")
    println("Solution 2: ${results[patternStart + (1000000000 - patternStart) % offset - 1]}")
}