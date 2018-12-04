package day4

import java.io.File
import java.util.ArrayList

fun main(args: Array<String>) {
    val values = readFileLineByLineUsingForEachLine("src/main/resources/day4.txt")
    solution1(values)
}

fun readFileLineByLineUsingForEachLine(fileName: String): Array<String> {
    val values: ArrayList<String> = ArrayList()
    File(fileName).forEachLine {
        values.add(it)
    }
    return values.toTypedArray()
}

fun solution1(input: Array<String>) {
    val idRegex = "(?<=#)([0-9]*?)(?= begins shift)".toRegex()
    val sleepRegex = "(?<=:)([0-9]*?)(?=] falls asleep)".toRegex()
    val wakeRegex = "(?<=:)([0-9]*?)(?=] wakes up)".toRegex()
    val sleepyTime: HashMap<Int, IntArray> = HashMap()

    var guardID = 0
    var sleep = 0

    input.sort()
    input.forEach {
        if (idRegex.containsMatchIn(it)) {
            guardID = idRegex.find(it)!!.value.toInt()
            if (!sleepyTime.containsKey(guardID)) {
                sleepyTime[guardID] = IntArray(60)
            }
        } else if (sleepRegex.containsMatchIn(it)) {
            sleep = sleepRegex.find(it)!!.value.toInt()
        } else if (wakeRegex.containsMatchIn(it)) {
            val wake = wakeRegex.find(it)!!.value.toInt()
            for (minute in sleep until wake) {
                sleepyTime.get(guardID)!![minute]++
            }
        }
    }

    val sleepiestGuard = sleepyTime.maxBy { it.value.sum() }?.key
    val sleepiestMinute = sleepyTime.get(sleepiestGuard)!!.indexOf(sleepyTime.get(sleepiestGuard)?.max()!!)
    val solution = sleepiestGuard!!.times(sleepiestMinute)
    println("The sleepiest guard is #${sleepiestGuard} at ${sleepiestMinute} after midnight, resulting in the solution: ${solution}")
}