package day14

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day14.txt")
    solution1(input)
    solution2(input)
}

fun readFile(fileName: String): Int {
    val raw = File(fileName).readText()
    return "\\d+".toRegex().find(raw)!!.value.toInt()
}

fun solution1(input: Int) {
    var elf1 = 0
    var elf2 = 1
    val scores = mutableListOf(3, 7)
    do {
        val total = scores[elf1].plus(scores[elf2]).toString().map(Character::getNumericValue)
        scores.addAll(total)
        val steps1 = elf1 + 1 + scores[elf1]
        val steps2 = elf2 + 1 + scores[elf2]
        elf1 = if(steps1 > scores.size - 1) {
            steps1 % scores.size}
        else {
            steps1
        }
        elf2 = if(steps2 > scores.size - 1) {
            steps2 % scores.size}
        else {
            steps2
        }
    } while(scores.size <= input + 10)
    println(scores.subList(input, input+10).joinToString(""))
}

fun solution2(input: Int) {
    var elf1 = 0
    var elf2 = 1
    val scores = mutableListOf(3, 7)
    while(input.toString() !in scores.takeLast(10).joinToString("")) {
        scores.addAll((scores[elf1] + scores[elf2]).toString().map(Character::getNumericValue))
        elf1 = (elf1 + 1 + scores[elf1]) % scores.size
        elf2 = (elf2 + 1 + scores[elf2]) % scores.size
    }
    println(scores.joinToString("").indexOf(input.toString()))
}
