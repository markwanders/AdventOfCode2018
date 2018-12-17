package day16

import java.io.File
import java.util.ArrayList

var before = ArrayList<List<Int>>()
var after = ArrayList<List<Int>>()
var instructions = ArrayList<List<Int>>()
val operations = HashMap<String, (List<Int>, List<Int>) -> List<Int>>()
val program = ArrayList<List<Int>>()

fun main(args: Array<String>) {
    readFile("src/main/resources/day16.txt")
    solution1()
    solution2()
}

fun readFile(fileName: String) {
    var previousIsBefore = false
    var startProgram = 0
    File(fileName).forEachLine {
        when {
            startProgram == 3 -> {
                program.add(it.split(" ").map { it.toInt() })
            }
            it == "" -> startProgram++
            it.startsWith("Before:") -> {
                before.add(it.substringAfter("Before: ").removeSurrounding("[", "]").split(", ").map { it.toInt() })
                previousIsBefore = true
                startProgram = 0
            }
            it.startsWith("After:") -> {
                after.add(it.substringAfter("After: ").removeSurrounding(" [", "]").split(", ").map { it.toInt() })
                previousIsBefore = false
                startProgram = 0
            }
            previousIsBefore -> {
                instructions.add(it.split(" ").map { it.toInt() })
                previousIsBefore = false
                startProgram = 0
            }
        }
    }
    operations["addr"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = input[opcode[1]] + input[opcode[2]]
        return output
    }
    operations["addi"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = input[opcode[1]] + opcode[2]
        return output
    }
    operations["mulr"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = input[opcode[1]] * input[opcode[2]]
        return output
    }
    operations["muli"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = input[opcode[1]] * opcode[2]
        return output
    }
    operations["banr"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = input[opcode[1]] and input[opcode[2]]
        return output
    }
    operations["bani"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = input[opcode[1]] and opcode[2]
        return output
    }
    operations["borr"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = input[opcode[1]] or input[opcode[2]]
        return output
    }
    operations["bori"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = input[opcode[1]] or opcode[2]
        return output
    }
    operations["setr"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = input[opcode[1]]
        return output
    }
    operations["seti"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = opcode[1]
        return output
    }
    operations["gtir"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = when {
            opcode[1] > input[opcode[2]] -> 1
            else -> 0
        }
        return output
    }
    operations["gtri"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = when {
            input[opcode[1]] > opcode[2] -> 1
            else -> 0
        }
        return output
    }
    operations["gtrr"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = when {
            input[opcode[1]] > input[opcode[2]] -> 1
            else -> 0
        }
        return output
    }
    operations["eqir"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = when {
            opcode[1] == input[opcode[2]] -> 1
            else -> 0
        }
        return output
    }
    operations["eqri"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = when {
            input[opcode[1]] == opcode[2] -> 1
            else -> 0
        }
        return output
    }
    operations["eqrr"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[3]] = when {
            input[opcode[1]] == input[opcode[2]] -> 1
            else -> 0
        }
        return output
    }
}

fun solution1() {
    var countMoreThanThree = 0
    (0 until instructions.size).forEach { i ->
        var count = 0
        operations.forEach { op ->
            val output = op.value(before[i], instructions[i])
            if(output == after[i]) {
                count++
            }
        }
        if (count >= 3) countMoreThanThree++
    }
    println(countMoreThanThree)
}

fun solution2() {
    val solutions = HashMap<Int, List<String>>()
    val uniqueSolutions = HashMap<Int, String>()
    do {
        (0 until instructions.size).forEach { i ->
            val solutionsForOp = mutableListOf<String>()
            operations.forEach { op ->
                val output = op.value(before[i], instructions[i])
                if (output == after[i] && !uniqueSolutions.containsValue(op.key)) {
                    solutionsForOp.add(op.key)
                }
            }
            val opcode = instructions[i][0]
            solutions[opcode] = when {
                solutions[opcode] == null -> solutionsForOp
                else -> solutions[opcode]!!.filter { solutionsForOp.contains(it) }
            }
        }
        solutions.forEach { solution ->
            if (solution.value.size == 1) {
                uniqueSolutions[solution.key] = solution.value[0]
                val indicesToBeRemoved = mutableListOf<Int>()
                instructions.forEachIndexed {index, list ->
                    if (list[0] == solution.key) {
                        indicesToBeRemoved.add(index)
                    }
                }
                indicesToBeRemoved.sortDescending()
                indicesToBeRemoved.forEach {removeIndex ->
                    instructions.removeAt(removeIndex)
                    before.removeAt(removeIndex)
                    after.removeAt(removeIndex)
                }
            }
        }
    } while(solutions.filter { solution -> solution.value.size > 1 }.isNotEmpty())
    var output = listOf(0, 0, 0, 0)
    program.forEach {line ->
        output = operations[uniqueSolutions[line[0]]]!!(output, line)
    }
    println(output[0])
}