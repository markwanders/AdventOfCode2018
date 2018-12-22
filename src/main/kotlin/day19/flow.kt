package day19

import java.io.File
import java.util.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set

var ip = -1
val operations = HashMap<String, (List<Int>, List<Int>) -> List<Int>>()
val program = ArrayList<Pair<String, List<Int>>>()

fun main(args: Array<String>) {
    readFile("src/main/resources/day19.txt")
    solution1()
    solution2()
}

fun readFile(fileName: String) {
    File(fileName).forEachLine {
        if (ip < 0) {
            ip = it.substringAfter("#ip ").toInt()
        } else {
            program.add(it.substring(0, 4) to it.substring(5, it.length).split(" ").map { it.toInt() })
        }
    }
    operations["addr"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = input[opcode[0]] + input[opcode[1]]
        return output
    }
    operations["addi"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = input[opcode[0]] + opcode[1]
        return output
    }
    operations["mulr"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = input[opcode[0]] * input[opcode[1]]
        return output
    }
    operations["muli"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = input[opcode[0]] * opcode[1]
        return output
    }
    operations["banr"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = input[opcode[0]] and input[opcode[1]]
        return output
    }
    operations["bani"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = input[opcode[0]] and opcode[1]
        return output
    }
    operations["borr"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = input[opcode[0]] or input[opcode[1]]
        return output
    }
    operations["bori"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = input[opcode[0]] or opcode[1]
        return output
    }
    operations["setr"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = input[opcode[0]]
        return output
    }
    operations["seti"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = opcode[0]
        return output
    }
    operations["gtir"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = when {
            opcode[0] > input[opcode[1]] -> 1
            else -> 0
        }
        return output
    }
    operations["gtri"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = when {
            input[opcode[0]] > opcode[1] -> 1
            else -> 0
        }
        return output
    }
    operations["gtrr"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = when {
            input[opcode[0]] > input[opcode[1]] -> 1
            else -> 0
        }
        return output
    }
    operations["eqir"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = when {
            opcode[0] == input[opcode[1]] -> 1
            else -> 0
        }
        return output
    }
    operations["eqri"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = when {
            input[opcode[0]] == opcode[1] -> 1
            else -> 0
        }
        return output
    }
    operations["eqrr"] = fun(input: List<Int>, opcode: List<Int>): List<Int> {
        val output = input.toMutableList()
        output[opcode[2]] = when {
            input[opcode[0]] == input[opcode[1]] -> 1
            else -> 0
        }
        return output
    }
}

fun solution1() {
    var input = mutableListOf(0, 0, 0, 0, 0, 0)
    while (input[ip] < program.size) {
        input = operations[program[input[ip]].first]!!(input, program[input[ip]].second).toMutableList()
        input[ip]++
    }
    println(input)
}

fun solution2() {
    var input = mutableListOf(1, 0, 0, 0, 0, 0)
    val list = arrayListOf<Pair<String, List<Int>>>()
    var repeat = true
    var factor = 0
    while (repeat) {

        input = operations[program[input[ip]].first]!!(input, program[input[ip]].second).toMutableList()

        list.add(program[input[ip]].first to program[input[ip]].second)
        val size = list.size
        if (size >= 16) {
            if (list.subList(size - 16, size - 9) == list.subList(size - 8, size - 1)) {
                println("The loop consists of operations ${list.subList(size - 16, size - 8)}")
                println("This comes down to finding the sum of factors of ${input[4]}")
                factor = input[4]
                repeat = false
            }
        }
        input[ip]++
    }

    val max = Math.sqrt(factor.toDouble()).toInt()
    var sum = 1
    for (i in 2..max) {
        if (factor % i == 0) {
            sum += i
            val result = factor / i
            if (result != i)
                sum += result
        }
    }

    println("Sum of factors of $factor is ${sum + factor}")
}