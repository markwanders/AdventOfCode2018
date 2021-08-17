package day4

import java.io.File
import java.util.ArrayList

fun main(args: Array<String>) {
    val values = readFileLineByLineUsingForEachLine("src/main/resources/day4.txt")
    val sleepyTimes = getSleepyTimes(values)
    solution1(sleepyTimes)
    solution2(sleepyTimes)
}

fun readFileLineByLineUsingForEachLine(fileName: String): Array<String> {
    val values: ArrayList<String> = ArrayList()
    File(fileName).forEachLine {
        values.add(it)
    }
    return values.toTypedArray()
}

fun solution1(sleepyTime: HashMap<Int, IntArray>) {
    val sleepiestGuard = sleepyTime.maxBy { it.value.sum() }?.key
    val sleepiestMinute = sleepyTime[sleepiestGuard]!!.indexOf(sleepyTime[sleepiestGuard]?.max()!!)
    val solution = sleepiestGuard!!.times(sleepiestMinute)
    println("The sleepiest guard is #${sleepiestGuard} at $sleepiestMinute after midnight, resulting in the solution: $solution")
}

fun solution2(sleepyTime: HashMap<Int, IntArray>) {
    val sleepiestEntry = sleepyTime.maxWith(
        Comparator { a, b -> a.value.max()!!.compareTo(b.value.max()!!)}
    )
    val sleepiestGuard = sleepiestEntry!!.key
    val sleepiestMinute = sleepiestEntry.value.indexOf(sleepiestEntry.value.max()!!)
    val solution = sleepiestGuard.times(sleepiestMinute)
    println("The sleepiest minute is $sleepiestMinute after midnight for guard #${sleepiestGuard}, resulting in the solution: $solution")
}

fun getSleepyTimes(input: Array<String>) : HashMap<Int, IntArray> {
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
                sleepyTime[guardID]!![minute]++
            }
        }
    }

    return sleepyTime
}