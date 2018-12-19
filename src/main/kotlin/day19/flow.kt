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
    println(ip)
    program.forEach {
        println(it)
    }
}

fun readFile(fileName: String) {
    File(fileName).forEachLine {
        if(ip < 0) {
            ip = it.substringAfter("#ip ").toInt()
        } else {
            program.add(it.substring(0, 4) to it.substring(5, it.length).split(" ").map { it.toInt() })
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