package day5

import java.io.File

fun main(args: Array<String>) {
    val polymer = readFile("src/main/resources/day5.txt")
    solution1(polymer)
    solution2(polymer)
}

fun readFile(fileName: String): String {
    return File(fileName).readText(Charsets.UTF_8)
}

fun solution1(polymer: String){
    println("Length of polymer after fully reacting is: ${react(polymer).length}")
}

fun solution2(polymer: String){
    val polymerArray = polymer.toCharArray()
    val minEntry = polymerArray
        .map { c -> c.toLowerCase() }
        .distinct()
        .sorted()
        .map { it to react(String(polymerArray.filterNot { c -> c.toLowerCase() == it}.toCharArray()))}
        .minBy { it.second.length }
    println("Removing letter ${minEntry!!.first} results in the shortest reacted polymer with length ${minEntry.second.length}.")
}


fun react(polymer: String) : String {
    var polymerArray = polymer.toCharArray()
    do {
        var matched = false
        for(x in polymerArray.size - 1 downTo 1) {
            if(polymerArray[x] != polymerArray[x - 1] && (polymerArray[x] == polymerArray[x - 1].toLowerCase() || polymerArray[x] == polymerArray[x - 1].toUpperCase())) {
                polymerArray[x] = ' '
                polymerArray[x - 1] = ' '
                matched = true
            }
        }
        polymerArray = polymerArray.filterNot { c -> c == ' ' }.toCharArray()
    } while (matched)

    return String(polymerArray)
}