package day1

import java.io.File

fun main(args : Array<String>) {
    val values = readFileLineByLineUsingForEachLine("src/main/resources/day1.txt")
    solution1(values)
    solution2(values)
}

fun readFileLineByLineUsingForEachLine(fileName: String) : IntArray {
    val values: ArrayList<Int> = ArrayList()
    File(fileName).forEachLine {
       values.add(it.toInt())
    }
    return values.toIntArray()
}

fun solution1(values : IntArray) {
    println(values.sum())
}

fun solution2(values: IntArray) {
    var currentValue = 0
    val historicValues: ArrayList<Int> = ArrayList(currentValue)
    var repeat = false
    while (!repeat) {
        values.forEach {
            currentValue += it
            if (historicValues.contains(currentValue)) {
                println("Found repetition of $currentValue")
                repeat = true
                return
            } else {
                historicValues.add(currentValue)
            }
        }
    }
}