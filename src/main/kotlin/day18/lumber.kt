package day18

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day18.txt")
    solution1(input)
}

fun readFile(fileName: String): ArrayList<ArrayList<Char>> {
    val output = arrayListOf(ArrayList<Char>())
    File(fileName).forEachLine { line ->
        if (output[0].isEmpty()) {
            output[0] = line.toCharArray().toCollection(ArrayList())
        } else {
            output.add(line.toCharArray().toCollection(ArrayList()))
        }
    }
    return output
}

fun solution1(input: ArrayList<ArrayList<Char>>) {

    var yard = input

    repeat(10) {
        val newYard = yard.map{ it.filter {  true }.toCollection(ArrayList()) }.toCollection(ArrayList())

        yard.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                val surrounding = mutableListOf<Char>()
                if(y > 0 && x > 0) surrounding.add(yard[y - 1][x - 1])
                if(y < line.size - 1 && x < line.size - 1) surrounding.add(yard[y + 1][x + 1])
                if(y > 0 && x < line.size - 1) surrounding.add(yard[y - 1][x + 1])
                if(x > 0 && y < line.size - 1) surrounding.add(yard[y + 1][x - 1])
                if(x > 0) surrounding.add(yard[y][x - 1])
                if(x < line.size - 1) surrounding.add(yard[y][x + 1])
                if(y > 0) surrounding.add(yard[y - 1][x])
                if(y < line.size - 1) surrounding.add(yard[y + 1][x])
                if(char == '.') {
                    if(surrounding.count{ it -> it == '|'} >= 3) {
                        newYard[y][x] = '|'
                    }
                }
                if(char == '|') {
                    if(surrounding.count{ it -> it == '#'} >= 3) {
                        newYard[y][x] = '#'
                    }
                }
                if(char == '#') {
                    if(!(surrounding.count{ it -> it == '#'} >= 1 && surrounding.count{ it -> it == '|'} >= 1)) {
                        newYard[y][x] = '.'
                    }
                }
            }
        }
        newYard.forEach {
            it.forEach { print(it) }
            println()
        }
        yard = newYard
    }
    println(yard.flatten().count {it == '#' } * yard.flatten().count {it == '|' })
}