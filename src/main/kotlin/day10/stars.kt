package day10

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day10.txt")
    solution1and2(input)
}

fun readFile(fileName: String): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    val values: ArrayList<Pair<Pair<Int, Int>, Pair<Int, Int>>> = ArrayList()
    File(fileName).forEachLine { s ->
        val ints = "-?\\d+".toRegex().findAll(s).map { it.value.toInt() }.toList()
        values.add(Pair(Pair(ints[0], ints[1]), Pair(ints[2], ints[3])))
    }
    return values.toList()
}

fun solution1and2(input: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>) {
    var state = input.toMutableList()
    var seconds = 0
    do {
        val oldXSize = state.maxBy { it.first.first }!!.first.first - state.minBy { it.first.first }!!.first.first
        val oldYSize = state.maxBy { it.first.second }!!.first.second - state.minBy { it.first.second }!!.first.second

        val newState = state.toMutableList()
        newState.forEachIndexed { index, old ->
            val new =
                Pair(Pair((old.first.first + old.second.first), (old.first.second + old.second.second)), old.second)
            newState[index] = new
        }

        val xSize = newState.maxBy { it.first.first }!!.first.first - newState.minBy { it.first.first }!!.first.first
        val ySize = newState.maxBy { it.first.second }!!.first.second - newState.minBy { it.first.second }!!.first.second

        if (oldXSize < xSize || oldYSize < ySize) {
            val maxX = state.maxBy { it.first.first }!!.first.first + 1
            val minX = state.minBy { it.first.first }!!.first.first
            val maxY = state.maxBy { it.first.second }!!.first.second + 1
            val minY = state.minBy { it.first.second }!!.first.second
            val coords = state.map { it -> it.first }
            for (y in minY until maxY) {
                for (x in minX until maxX) {
                    if (coords.contains(Pair(x, y))) {
                        print('#')
                    } else {
                        print(' ')
                    }
                }
                println()
            }
            println("Read message after $seconds seconds")
        }
        state = newState
        seconds++
    } while (xSize <= oldXSize && ySize <= oldYSize)
}