package day2

import java.io.File

fun main(args: Array<String>) {
    val values = day2.readFileLineByLineUsingForEachLine("src/main/resources/day2.txt")
    solution1(values)
}

fun readFileLineByLineUsingForEachLine(fileName: String): Array<String> {
    var values: ArrayList<String> = ArrayList()
    File(fileName).forEachLine {
        values.add(it)
    }
    return values.toTypedArray()
}

fun solution1(input: Array<String>) {
    var two = 0
    var three = 0
    input.forEach {
        val charArray = it.toCharArray()
        val counts = HashMap<Char, Int>()
        for (key in charArray) {
            if (counts.containsKey(key)) {
                counts[key] = (counts[key] ?: 0) + 1
            } else {
                counts[key] = 1
            }
        }
        if (counts.containsValue(2)) two++
        if (counts.containsValue(3)) three++
    }
    val checksum = two * three
    println("Amount of twos: $two, amount of threes: $three, checksum: $checksum")
}