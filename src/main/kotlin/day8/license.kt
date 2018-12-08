package day8

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day8.txt")
    solution1(input)
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
    println(sum)
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