package day13

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/test.txt")
    solution1(input)
}

fun readFile(fileName: String): ArrayList<CharArray> {
    val output = arrayListOf(charArrayOf())
    File(fileName).forEachLine { line ->
        if (output[0].isEmpty()) {
            output[0] = line.toCharArray()
        } else {
            output.add(line.toCharArray())
        }
    }
    return output
}

fun solution1(input: ArrayList<CharArray>) {
    val cars = mutableListOf<Pair<Pair<Char, Int>, Pair<Int, Int>>>()
    input.forEachIndexed { y, row ->
        row.forEachIndexed { x, char ->
            when (char) {
                '>' -> {
                    cars.add(Pair(Pair(char, 0), Pair(x, y)))
                    input[y][x] = '-'
                }
                '<' -> {
                    cars.add(Pair(Pair(char, 0), Pair(x, y)))
                    input[y][x] = '-'
                }
                'v' -> {
                    cars.add(Pair(Pair(char, 0), Pair(x, y)))
                    input[y][x] = '|'
                }
                '^' -> {
                    cars.add(Pair(Pair(char, 0), Pair(x, y)))
                    input[y][x] = '|'
                }
            }
        }
    }
    var crashed = false
    do {
        cars.forEachIndexed { carNr, car ->
            val x = car.second.first
            val y = car.second.second
            val currentDirection = car.first.first
            val numberOfTurns = car.first.second
            val nextDirection = nextDirection(currentDirection, numberOfTurns, input[y][x])

            when (nextDirection.first) {
                '>' -> {
                    cars[carNr] = Pair(nextDirection, Pair(x + 1, y))
                }
                '<' -> {
                    cars[carNr] = Pair(nextDirection, Pair(x - 1, y))
                }
                '^' -> {
                    cars[carNr] = Pair(nextDirection, Pair(x, y - 1))
                }
                'v' -> {
                    cars[carNr] = Pair(nextDirection, Pair(x, y + 1))
                }
            }
        }

        val crashes = cars.map { car -> car.second }
            .groupingBy { it }
            .eachCount()
            .filter { it.value > 1 }
            .filter { entry -> entry.value > 1 }
        if (crashes.isNotEmpty()) {
            println(crashes)
            crashed = true
        }
    } while (!crashed)

}

fun nextDirection(currentDirection: Char, turns: Int, track: Char): Pair<Char, Int> {
    when (track) {
        '\\' -> when (currentDirection) {
            '^' -> return Pair('<', turns)
            '>' -> return Pair('v', turns)
            'v' -> return Pair('>', turns)
            '<' -> return Pair('^', turns)
        }
        '/' -> when (currentDirection) {
            '^' -> return Pair('>', turns)
            '>' -> return Pair('^', turns)
            'v' -> return Pair('<', turns)
            '<' -> return Pair('v', turns)
        }
        '+' -> when (currentDirection) {
            '^' -> {
                when (turns) {
                    0 -> return Pair('<', 1)
                    1 -> return Pair(currentDirection, 2)
                    2 -> return Pair('>', 0)
                }
            }
            '>' -> {
                when (turns) {
                    0 -> return Pair('^', 1)
                    1 -> return Pair(currentDirection, 2)
                    2 -> return Pair('v', 0)
                }
            }
            'v' -> {
                when (turns) {
                    0 -> return Pair('>', 1)
                    1 -> return Pair(currentDirection, 2)
                    2 -> return Pair('<', 0)
                }
            }
            '<' -> {
                when (turns) {
                    0 -> return Pair('v', 1)
                    1 -> return Pair(currentDirection, 2)
                    2 -> return Pair('^', 0)
                }
            }
        }
    }
    return Pair(currentDirection, turns)
}