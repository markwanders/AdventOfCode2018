package day10

import java.io.File
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/test.txt")
    solution1(input)
}

fun readFile(fileName: String): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    val values: ArrayList<Pair<Pair<Int, Int>, Pair<Int, Int>>> = ArrayList()
    File(fileName).forEachLine { s ->
        val ints = "-?\\d+".toRegex().findAll(s).map { it.value.toInt() }.toList()
        values.add(Pair(Pair(ints[0], ints[1]), Pair(ints[2], ints[3])))
    }
    return values.toList()
}

fun solution1(input: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>) {
    val state = input.toMutableList()
    val maxX = input.maxBy { it.first.first }!!.first.first + 1
    val minX = input.minBy { it.first.first }!!.first.first.absoluteValue
    val x =  maxX + minX
    val maxY = input.maxBy { it.first.second }!!.first.second + 1
    val minY = input.minBy { it.first.second }!!.first.second.absoluteValue
    val y = maxY + minY
    val grid = Array(y) { CharArray(x) {'.'} }
    repeat(4) {
        val updated = mutableListOf<Pair<Int, Int>>()
        state.forEachIndexed { index, old ->
            if(!updated.contains(Pair(old.first.first + minX, old.first.second + minY))) {
                grid[old.first.second + minY][old.first.first + minX] = '.'
            }
            val new = Pair(Pair((old.first.first + old.second.first), (old.first.second + old.second.second)), old.second)
            state[index] = new
            grid[new.first.second + minY][new.first.first + minX] = '#'
            updated.add(Pair(new.first.first + minX, new.first.second + minY))
        }.also {
            grid.forEach {
                it.forEach {
                    print(it)
                }
                println()
            }
        }
    }

}