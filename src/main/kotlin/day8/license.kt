package day8

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/test.txt")
    solution1(input)
}

fun readFile(fileName: String): IntArray {
    return File(fileName).readText().split(" ").map { it.toInt() }.toIntArray()
}

fun solution1(input: IntArray) {
    println(metadata(input))
}

fun metadata(input: IntArray):Int {
    var metadata = 0

    val children = input[0]
    val metadataEntries = input[1]
    val newInput: IntArray
    if (children == 0) {
        metadata += input.slice(2 until 2 + metadataEntries).sum()
        if (2 + metadataEntries < input.size) {
            newInput = input.drop(2 + metadataEntries).toIntArray()
            metadata += metadata(newInput)
        }
    } else {
            metadata += input.takeLast(metadataEntries).sum()
            newInput = input.slice(2 until input.size - metadataEntries).toIntArray()
            metadata += metadata(newInput)

    }

    return metadata
}