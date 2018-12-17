package day17

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/test.txt")
    solution1(input)
}

fun readFile(fileName: String): Array<CharArray> {
    val yRegex = "(?<=y=)(\\d*)".toRegex()
    val xRegex = "(?<=x=)(\\d*)".toRegex()
    val yRangeRegex = "(?<=y=)(\\d*)\\.\\.(\\d*)".toRegex()
    val xRangeRegex = "(?<=x=)(\\d*)\\.\\.(\\d*)".toRegex()
    val sand = mutableListOf<Pair<Int, Int>>()

    File(fileName).forEachLine {
        val xRange: List<Int>
        val yRange: List<Int>
        xRange = if(xRangeRegex.containsMatchIn(it)) {
            xRangeRegex.findAll(it).map { it.destructured.toList().map { it->it.toInt() } }.first().toMutableList()
        } else {
            mutableListOf(xRegex.findAll(it).map { it.value.toInt() }.first())
        }
        yRange = if(yRangeRegex.containsMatchIn(it)) {
            yRangeRegex.findAll(it).map { it.destructured.toList().map { it->it.toInt() } }.first().toMutableList()
        } else {
            mutableListOf(yRegex.findAll(it).map { it.value.toInt() }.first())
        }
        for(y in yRange.min()!! until yRange.max()!! + 1) {
            for(x in xRange.min()!! until xRange.max()!! + 1) {
                sand.add(x to y)
            }
        }
    }
    val grid = Array(sand.maxBy { it.second }!!.second + 1) { CharArray(2 * sand.maxBy { it.first }!!.first + 1) { '.'} }
    sand.forEach {
        grid[it.second][it.first] = '#'
    }
    grid[0][500] = '+'
    return grid
}

fun solution1(grid: Array<CharArray>) {
    grid.forEach {
        it.forEach {
            print(it)
        }
        println()
    }
}