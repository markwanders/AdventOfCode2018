package day20

import java.io.File
import java.lang.Integer.min

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day20.txt")
    solution1and2(input)
}

fun readFile(fileName: String) : String {
    return File(fileName).readText()
}

fun solution1and2(input: String) {
    var position = 0 to 0
    val distances = mutableMapOf<Pair<Int, Int>, Int>()
    var currentDistance = 0
    val fallback  = mutableListOf<Pair<Int, Int>>()
    input.toCharArray().forEach { char ->
        when(char) {
            'N' ->  {
                currentDistance = (distances[position] ?: currentDistance) + 1
                position = position.first to position.second + 1
                distances[position] = min(currentDistance, distances[position]?:Integer.MAX_VALUE )
            }
            'E' ->  {
                currentDistance = (distances[position] ?: currentDistance) + 1
                position = position.first + 1 to position.second
                distances[position] = min(currentDistance, distances[position]?:Integer.MAX_VALUE )
            }
            'S' ->  {
                currentDistance = (distances[position] ?: currentDistance) + 1
                position = position.first to position.second - 1
                distances[position] = min(currentDistance, distances[position]?:Integer.MAX_VALUE )
            }
            'W' ->  {
                currentDistance = (distances[position] ?: currentDistance) + 1
                position = position.first - 1 to position.second
                distances[position] = min(currentDistance, distances[position]?:Integer.MAX_VALUE )
            }
            '(' ->  {
                fallback.add(position)
            }
            ')' ->  {
                position = fallback.lastOrNull()!!
                if(fallback.isNotEmpty()) {
                    fallback.removeAt(fallback.size - 1)
                }
            }
            '|' ->  {
                position = fallback[fallback.size - 1]
            }
        }
    }

    println("${distances.maxBy { it.value }!!.value} at ${distances.maxBy { it.value }!!.key}")

    println("${distances.filter { it.value >= 1000}.count()}")
}