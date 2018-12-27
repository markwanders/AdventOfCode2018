package day25

import java.io.File
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day25.txt")
    solution1(input)
}

fun readFile(file: String): List<List<Int>> {
    val output = mutableListOf<List<Int>>()
    File(file).forEachLine {
        val match =
            "-?\\d+".toRegex().findAll(it)
                .map { matchResult -> matchResult.groupValues.map { values -> values.toInt() } }
                .flatten().toList()
        output.add(match)
    }
    return output
}

fun solution1(points: List<List<Int>>) {
    val constellations = hashMapOf<List<Int>, MutableSet<List<Int>>>()
    for (first in points) {
        for (second in points) {
            if (manhattan(first, second) <= 3) { // Are we in range?
                val firstConstellation = constellations.getOrPut(first) { mutableSetOf(first) } // Get existing constellation or begin a new one
                val secondConstellation = constellations.getOrPut(second) { mutableSetOf(second) } // Get existing constellation or begin a new one
                if(firstConstellation != secondConstellation) {
                    firstConstellation.addAll(secondConstellation)
                    for (point in secondConstellation) { // Merge constellations if applicable
                        constellations[point] = firstConstellation
                    }
                }
            }
        }
    }
    println(constellations.values.toSet().size)
}

fun manhattan(first: List<Int>, second: List<Int>): Int {
    return (first[0] - second[0]).absoluteValue + (first[1] - second[1]).absoluteValue + (first[2] - second[2]).absoluteValue + (first[3] - second[3]).absoluteValue
}