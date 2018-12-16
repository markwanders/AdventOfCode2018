package day16

import java.io.File
import java.util.ArrayList

val before = ArrayList<List<Int>>()
val after = ArrayList<List<Int>>()
val instructions = ArrayList<List<Int>>()
val operations = HashMap<String, (List<Int>, List<Int>) -> List<Int>>()

fun main(args: Array<String>) {
    readFile("src/main/resources/day16.txt")
    solution1()
}

fun readFile(fileName: String) {
    var previousIsBefore = false
    File(fileName).forEachLine {
        when {
            it.startsWith("Before:") -> {
                before.add(it.substringAfter("Before: ").removeSurrounding("[", "]").split(", ").map { it.toInt() })
                previousIsBefore = true
            }
            it.startsWith("After:") -> {
                after.add(it.substringAfter("After: ").removeSurrounding(" [", "]").split(", ").map { it.toInt() })
                previousIsBefore = false
            }
            previousIsBefore -> {
                instructions.add(it.split(" ").map { it.toInt() })
                previousIsBefore = false
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
            val output = op.value.invoke(before[i], instructions[i])
            if(output == after[i]) {
                print(op.key)
                count++
            }
        }
        if (count >= 3) countMoreThanThree++
    }
    println(countMoreThanThree)
}