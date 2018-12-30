package day23

import java.io.File
import java.util.*
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day23.txt")
    solution1(input)
    solution2(input)
}

fun readFile(file: String): ArrayList<List<Int>> {
    val output = arrayListOf<List<Int>>()
    File(file).forEachLine {
        val match = "-?\\d+".toRegex().findAll(it)
            .map { matchResult -> matchResult.groupValues.map { values -> values.toInt() } }.flatten().toList()
        output.add(match)
    }
    return output
}

fun solution1(nanos: ArrayList<List<Int>>) {
    val maxRange = nanos.maxBy { nano -> nano.last() }!!
    var nanosInRange = 0
    nanos.forEach { nano ->
        if (manhattan(nano, maxRange) <= maxRange.last()) {
            nanosInRange++
        }
    }
    println("Nanomachines in range of nanomachine with largest range: $nanosInRange")
}

fun solution2(nanos: ArrayList<List<Int>>) {
    // Define a cube large enough to encompass all nanomachines
    val origin = listOf(0, 0, 0)
    val maxX = nanos.map { nano -> nano[0] }.max()!!
    val minX = nanos.map { nano -> nano[0] }.min()!!
    val center = (maxX + minX) / 2
    var size = (maxX - minX)
    val initialCube = Cube(center, center, center)

    val queue = PriorityQueue<Cube>(compareByDescending<Cube> { cube ->
        // Sort a queue based on number of intersecting nanomachines and then by distance from origin
        nanos.filter { nano ->
            intersects(
                nano,
                Cube(cube.x - size / 2 , cube.y - size / 2, cube.z - size / 2),
                Cube(cube.x + size / 2, cube.y + size / 2, cube.z + size / 2)
            )
        }.size
    }.thenBy { cube -> manhattan(origin, listOf(cube.x, cube.y, cube.z)) })


    queue.add(initialCube)
    while (queue.isNotEmpty() && size > 1) { // Stop when we converge on a point
        val cube = queue.poll()
        size /= 2
        val nextCubes = listOf( //split existing cube into eight and add new cubes to queue
            Cube(cube.x + size, cube.y + size, cube.z + size),
            Cube(cube.x - size, cube.y - size, cube.z - size),
            Cube(cube.x + size, cube.y + size, cube.z - size),
            Cube(cube.x + size, cube.y - size, cube.z - size),
            Cube(cube.x + size, cube.y - size, cube.z + size),
            Cube(cube.x - size, cube.y - size, cube.z + size),
            Cube(cube.x - size, cube.y + size, cube.z + size),
            Cube(cube.x - size, cube.y + size, cube.z - size)
        )
        queue.addAll(nextCubes)
    }

    val solution = queue.poll()
    println("Point (x=${solution.x}, y=${solution.y}, z=${solution.z}) has the most nanaomachines in range at a distance of ${manhattan(origin, listOf(solution.x, solution.y, solution.z))} from the origin")
}

fun manhattan(first: List<Int>, second: List<Int>): Int {
    return (first[0] - second[0]).absoluteValue + (first[1] - second[1]).absoluteValue + (first[2] - second[2]).absoluteValue
}

fun intersects(nano: List<Int>, cubeMin: Cube, cubeMax: Cube): Boolean {
    var dist = nano[3]
    if (nano[0] < cubeMin.x) {
        dist -= (nano[0] - cubeMin.x).absoluteValue
    } else if (nano[0] > cubeMax.x) {
        dist -= (nano[0] - cubeMax.x).absoluteValue
    }
    if (nano[1] < cubeMin.y) {
        dist -= (nano[1] - cubeMin.y).absoluteValue
    } else if (nano[1] > cubeMax.y) {
        dist -= (nano[1] - cubeMax.y).absoluteValue
    }
    if (nano[2] < cubeMin.z) {
        dist -= (nano[2] - cubeMin.z).absoluteValue
    } else if (nano[2] > cubeMax.z) {
        dist -= (nano[2] - cubeMax.z).absoluteValue
    }
    return dist >= 0
}

data class Cube(val x: Int, val y: Int, val z: Int)