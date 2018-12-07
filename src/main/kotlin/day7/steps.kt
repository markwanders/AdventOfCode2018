package day7

import java.io.File
import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    val instructions = readFile("src/main/resources/day7.txt")
    solution1(instructions)
    solution2(instructions)
}

fun readFile(fileName: String): Array<String> {
    val values: ArrayList<String> = ArrayList()
    File(fileName).forEachLine {
        values.add(it)
    }
    return values.toTypedArray()
}

fun solution1(instructions: Array<String>) {
    val steps = instructions
        .flatMap { listOf(it.toCharArray()[5], it.toCharArray()[36]) }
        .distinct()
        .sorted()
    val stepsWithPrerequisites = steps
        .associateBy({ it }, {
            instructions
                .filter { instruction -> instruction.toCharArray()[36] == it }
                .map { instruction -> instruction.toCharArray()[5] }
        })
    val stepsTaken = mutableListOf<Char>()
    while (stepsTaken.size < steps.size) {
        stepsTaken += steps
            .filterNot { stepsTaken.contains(it) }
            .first { a ->
                !stepsWithPrerequisites.containsKey(a) || stepsWithPrerequisites[a]!!.all { b -> stepsTaken.contains(b) }
            }
    }

    println("Correct order for the steps is ${String(stepsTaken.toCharArray())}")
}

fun solution2(instructions: Array<String>) {

}
