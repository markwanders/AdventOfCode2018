package day6

import java.io.File
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue


fun main(args: Array<String>) {
    val coords = readFile("src/main/resources/day6.txt")
    solution1(coords)
    solution2(coords)
}

fun readFile(fileName: String): Array<Pair<Int, Int>> {
    val values: ArrayList<Pair<Int, Int>> = ArrayList()
    File(fileName).forEachLine {
        values.add(Pair(it.split(", ").first().toInt(), it.split(", ")[1].toInt()))
    }
    return values.toTypedArray()
}

fun solution1(coords: Array<Pair<Int, Int>>) {
    val grid = Array(coords.maxBy { it.first }!!.first) { IntArray(coords.maxBy { it.second }!!.second) }
    val regions = HashMap<Int, Int>()
    grid.forEachIndexed { x, ints ->
        ints.forEachIndexed { y, _ ->
            val closest: Int
            val map = coords.map { pair -> coords.indexOf(pair) to manhattan(pair, Pair(x, y)) }
            val min = map.minBy { it.second }!!.second
            closest = if (map.count { it.second == min } == 1) {
                map.minBy { it.second }!!.first
            } else {
                -1
            }
            grid[x][y] = closest
            regions[closest] = regions[closest]?.inc() ?: 1
        }
    }
    grid.forEach {
        regions.remove(it[0])
        regions.remove(it[grid[0].size - 1])
    }
    grid[0].forEach {
        regions.remove(it)
    }
    grid[grid.size - 1].forEach {
        regions.remove(it)
    }
    val biggest = regions.maxBy { it.value }!!.value
    println("Largest area region is $biggest")
}

fun solution2(coords: Array<Pair<Int, Int>>) {
    val grid = Array(coords.maxBy { it.first }!!.first) { IntArray(coords.maxBy { it.second }!!.second) }
    val region = ArrayList<Pair<Int, Int>>()
    grid.forEachIndexed { x, ints ->
        ints.forEachIndexed { y, _ ->
            if(coords.map { pair -> manhattan(pair, Pair(x, y)) }.sum() < 10000) {
                region.add(Pair(x, y))
            }
        }
    }
    println("Area of region within 10000 units of all coordinates has size ${region.size}")

}

fun manhattan(a: Pair<Int, Int>, b: Pair<Int, Int>): Int {
    return (a.first - b.first).absoluteValue + (a.second - b.second).absoluteValue
}