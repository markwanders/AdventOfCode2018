package day2

import java.io.File

fun main(args: Array<String>) {
    val values = readFileLineByLineUsingForEachLine("src/main/resources/day2.txt")
    solution1(values)
    solution2(values)
}

fun readFileLineByLineUsingForEachLine(fileName: String): Array<String> {
    val values: ArrayList<String> = ArrayList()
    File(fileName).forEachLine {
        values.add(it)
    }
    return values.toTypedArray()
}

fun solution1(input: Array<String>) {
    var two = 0
    var three = 0
    input.forEach {
        val counts = it.toCharArray().groupBy { c -> c }.values.map { c -> c.size }
        if (counts.contains(2)) two++
        if (counts.contains(3)) three++
    }
    val checksum = two * three
    println("Amount of twos: $two, amount of threes: $three, checksum: $checksum")
}

fun solution2(input: Array<String>) {
    val charArrays = input.map { n -> n.toCharArray() }
    var string1 = ""
    var string2 = ""
    var common = ""
    for(charArray in charArrays) {
        charArrays.forEach {
            var i = 0
            var diff = 0
            while(i < charArray.size) {
                if (charArray[i] != it[i]) {
                    diff++
                }
                if(diff > 1) {
                    break
                }
                i++
            }
            if(diff == 1) {
                val commonList = ArrayList<Char>()
                charArray.toCollection(commonList).retainAll(it.toCollection(ArrayList()))
                string1 = String(charArray)
                string2 = String(it)
                common = String(commonList.toCharArray())
            }
        }
    }
    println("IDs which only differ one character: $string1 and $string2, common string: $common")
}