package day1

import java.io.File

fun main(args : Array<String>) {
    val values = readFileLineByLineUsingForEachLine("src/main/resources/day1.txt")
    println(values.sum())
}

fun readFileLineByLineUsingForEachLine(fileName: String) : IntArray {
    var values: ArrayList<Int> = ArrayList()
    File(fileName).forEachLine {
       values.add(it.toInt())
    }
    return values.toIntArray()
}