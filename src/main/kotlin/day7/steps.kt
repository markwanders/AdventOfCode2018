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

    var secondsPassed = 0
    var availableWorkers = 5
    val busySteps = mutableMapOf<Char, Int>()
    val stepsTaken = mutableListOf<Char>()

    do {
        with(busySteps.filter { it.value == secondsPassed }.keys.sorted()) {//Handle completion of steps that finish at this second
            forEach { busySteps.remove(it) } //remove from busy steps
            availableWorkers += size //release workers
            stepsTaken += this //Add newly finished steps to the total
        }
        steps
            .filterNot { step -> stepsTaken.contains(step) || busySteps.containsKey(step)} //Filter out steps already taken or being taken
            .filter { a -> !stepsWithPrerequisites.containsKey(a) || stepsWithPrerequisites[a]!!.all { b -> stepsTaken.contains(b) } } //Leave the steps whose prereqs have been met
            .sorted() //Handle them alphabetically
            .take(availableWorkers) //Assign available workers
            .also { currentSteps -> availableWorkers -= currentSteps.size } //And then make them unavailable
            .forEach { busySteps[it] = secondsPassed + (it - 'A' + 61) }//These workers will be busy until then

        if(stepsTaken.size < steps.size) {
            secondsPassed++
        } else {
            break
        }
    } while (true)

    println("It will take $secondsPassed seconds")
}
