package day5

import java.io.File

fun main(args: Array<String>) {
    val polymer = readFile("src/main/resources/day5.txt")
    solution1(polymer)
}

fun readFile(fileName: String): String {
    return File(fileName).readText(Charsets.UTF_8)
}

fun solution1(polymer: String){
    val polymerArray = polymer.toMutableList()
    do {
        var matched = false
        for(x in polymerArray.size - 1 downTo 1) {
            if(polymerArray[x] != polymerArray[x - 1] && (polymerArray[x] == polymerArray[x - 1].toLowerCase() || polymerArray[x] == polymerArray[x - 1].toUpperCase())) {
                polymerArray.removeAt(x)
                polymerArray.removeAt(x - 1)
                matched = true
            }
        }
    } while (matched)

    println("Length of polymer after fully reacting is: ${polymerArray.size}")
}