package day23

import java.io.File
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day23.txt")
    solution1(input)
}

fun readFile(file: String) : ArrayList<List<Int>> {
    val output = arrayListOf<List<Int>>()
    File(file).forEachLine {
        val match = "-?\\d+".toRegex().findAll(it).map { matchResult -> matchResult.groupValues.map { values -> values.toInt() }}.flatten().toList()
        output.add(match)
    }
    return output
}

fun solution1(nanos: ArrayList<List<Int>>) {
    val maxRange = nanos.maxBy { nano -> nano.last() }!!
    val xRange = maxRange[0]
    val yRange = maxRange[1]
    val zRange = maxRange[2]
    var nanosInRange = 0
    nanos.forEach {nano ->
        val manhattan = (nano[0] - xRange).absoluteValue + (nano[1] - yRange).absoluteValue + (nano[2] - zRange).absoluteValue
        if(manhattan <= maxRange.last()) {
            nanosInRange++
        }
    }
    println("Nanomachines in range of nanomachine with largest range: $nanosInRange")
}