package day17

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/test.txt")
    solution1(input)
}

fun readFile(fileName: String): Array<Array<Char>> {
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
    val grid = Array(sand.maxBy { it.second }!!.second + 1) { Array(2 * sand.maxBy { it.first }!!.first + 1) { '.'} }
    sand.forEach {
        grid[it.second][it.first] = '#'
    }
    grid[0][500] = '+'
    return grid
}

fun solution1(grid: Array<Array<Char>>) {
    grid.forEach {
        it.forEachIndexed { x, it ->
            if (x in 491..509) print(it)
        }
        println()
    }
    var water = mutableListOf<Pair<Int, Int>>()
    water.add(500 to 0)
    repeat(10) {

        val addedWater = mutableListOf<Pair<Int, Int>>()
        water.forEach {source ->
            var y = source.second
            while (y < grid.size - 1 && (grid[y + 1][source.first] == '.' || grid[y + 1][source.first] == '|')) {
                y++
                grid[y][source.first] = '|'
            }
            var x = source.first
            while (x < grid[y].size - 1 && grid[y][x + 1] != '#') {
                x++
            }
            val maxX = x
            x = source.first
            while (grid[y][x - 1] != '#' && x - 1 > 0) {
                x--
            }
            val minX = x
            if(y < grid.size - 1 && grid[y + 1].slice(minX..maxX).filterNot { tile -> tile == '#' || tile == '~' }.isEmpty()) { // fill with water
                for(i in minX..maxX) {
                    grid[y][i] = '~'
                }
            } else {
                var xBottom = source.first
                while (y < grid.size - 1 && grid[y][xBottom + 1] == '.' && grid[y + 1][xBottom] != '.') {
                    xBottom++
                    grid[y][xBottom] = '|'
                }
                addedWater.add(xBottom to y)
                xBottom = source.first
                while (y < grid.size - 1 && grid[y][xBottom - 1] == '.' && grid[y + 1][xBottom] != '.') {
                    xBottom--
                    grid[y][xBottom] = '|'
                }
                addedWater.add(xBottom to y)
            }
        }
        water.addAll(addedWater)
        water = water.distinct().toMutableList()
        grid.forEach {line ->
            line.forEachIndexed { x, char ->
                if (x in 491..509) print(char)
            }
            println()
        }
        println()
    }
    println(grid.flatten().count { it == '~' }.plus(grid.flatten().count { it == '|' }))
}