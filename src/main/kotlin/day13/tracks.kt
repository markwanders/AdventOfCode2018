package day13

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day13.txt")
//    solution1(input)
    solution2(input)
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
    var cars = mutableListOf<Pair<Pair<Char, Int>, Pair<Int, Int>>>()
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
        cars = cars.sortedWith(compareBy({ it.second.second }, { it.second.first })).toMutableList()
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

            val crashes = cars
                .groupingBy { carInCars -> carInCars.second }
                .eachCount()
                .filter { it.value > 1 }
            if (crashes.isNotEmpty()) {
                println(crashes.keys)
                crashed = true
                return
            }
        }


    } while (!crashed)
}

fun solution2(input: ArrayList<CharArray>) {
    var cars = mutableListOf<Pair<Pair<Char, Int>, Pair<Int, Int>>>()
    input.forEachIndexed { y, row ->
        row.forEachIndexed { x, char ->
            when (char) {
                '>' -> {
                    cars.add((char to 0) to (x to y))
                    input[y][x] = '-'
                }
                '<' -> {
                    cars.add((char to 0) to (x to y))
                    input[y][x] = '-'
                }
                'v' -> {
                    cars.add((char to 0) to (x to y))
                    input[y][x] = '|'
                }
                '^' -> {
                    cars.add((char to 0) to (x to y))
                    input[y][x] = '|'
                }
            }
        }
    }
    var crashed = false
    while (!crashed) {
        cars = cars.sortedWith(compareBy({ it.second.second }, { it.second.first })).toMutableList()
        cars.forEachIndexed { carNr, car ->
            val x = car.second.first
            val y = car.second.second
            val currentDirection = car.first.first
            val numberOfTurns = car.first.second
            val nextDirection = nextDirection(currentDirection, numberOfTurns, input[y][x])

            when (nextDirection.first) {
                '>' -> {
                    cars[carNr] = nextDirection to (x + 1 to y)
                }
                '<' -> {
                    cars[carNr] = nextDirection to (x - 1 to y)
                }
                '^' -> {
                    cars[carNr] = nextDirection to (x to y - 1)
                }
                'v' -> {
                    cars[carNr] = nextDirection to (x to y + 1)
                }
            }

            val crashes = cars
                .groupingBy { carInCars -> carInCars.second }
                .eachCount()
                .filter { it.value > 1 }
            if (crashes.isNotEmpty()) {
                cars.mapIndexed{ index, it ->
                    if(crashes.containsKey(it.second)) {
                        cars[index] = '.' to 0 to (0 to 0)
                    }
                }
            }
            val remainingCars = cars.filter { it -> it.second != 0 to 0 }
            if (remainingCars.size == 1) {
                crashed = true
                println(remainingCars)
            }
        }

    }

}

fun nextDirection(currentDirection: Char, turns: Int, track: Char): Pair<Char, Int> {
    when (track) {
        '\\' -> when (currentDirection) {
            '^' -> return '<' to turns
            '>' -> return 'v' to turns
            'v' -> return '>' to turns
            '<' -> return '^' to turns
        }
        '/' -> when (currentDirection) {
            '^' -> return '>' to turns
            '>' -> return '^' to turns
            'v' -> return '<' to turns
            '<' -> return 'v' to turns
        }
        '+' -> when (currentDirection) {
            '^' -> {
                when (turns) {
                    0 -> return '<' to 1
                    1 -> return currentDirection to 2
                    2 -> return '>' to 0
                }
            }
            '>' -> {
                when (turns) {
                    0 -> return '^' to 1
                    1 -> return currentDirection to 2
                    2 -> return 'v' to 0
                }
            }
            'v' -> {
                when (turns) {
                    0 -> return '>' to 1
                    1 -> return currentDirection to 2
                    2 -> return '<' to 0
                }
            }
            '<' -> {
                when (turns) {
                    0 -> return 'v' to 1
                    1 -> return currentDirection to 2
                    2 -> return '^' to 0
                }
            }
        }
    }
    return currentDirection to turns
}