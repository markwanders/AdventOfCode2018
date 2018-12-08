package day8

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day8.txt")
    solution1(input)
    solution2(input)
}

fun readFile(fileName: String): IntArray {
    return File(fileName).readText().split(" ").map { it.toInt() }.toIntArray()
}

fun solution1(input: IntArray) {
    var sum = 0
    val iterator = input.iterator()
    while (iterator.hasNext()) {
        sum += metadata(iterator)
    }
    println("Metadata sum: $sum")
}

fun solution2(input: IntArray) {
    var sum = 0
    val iterator = input.iterator()
    while (iterator.hasNext()) {
        sum += value(iterator)
    }
    println("Root node value: $sum")
}

fun value(input: IntIterator): Int {

    val children = input.nextInt()
    val metadataEntries = input.nextInt()

    val childValues = mutableMapOf<Int, Int>()
    for (c in 0 until children) {
        childValues[c] = value(input)
    }

    val valuesToUse = mutableListOf<Int>()
    for (m in 0 until metadataEntries) {
        valuesToUse.add(input.nextInt())
    }

    return if(children > 0) {
        valuesToUse.map { it -> childValues[it - 1]?: 0  }.sum()
    } else {
        valuesToUse.sum()
    }

}

fun metadata(input: IntIterator): Int {
    var metadata = 0

    val children = input.nextInt()
    val metadataEntries = input.nextInt()

    for (c in 0 until children) {
        metadata += metadata(input)
    }
    for (m in 0 until metadataEntries) {
        metadata += input.nextInt()
    }

    return metadata
}