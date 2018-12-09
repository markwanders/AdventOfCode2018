package day9

import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day9.txt")
    solution1(input[0], input[1])
    solution2(input[0], input[1])
}

fun readFile(fileName: String): List<Int> {
    val raw = File(fileName).readText()
    return "\\d+".toRegex().findAll(raw).map { it.value.toInt() }.toList()
}

fun solution1(numberOfPlayers: Int, numberOfMarbles: Int) {
    val players = IntArray(numberOfPlayers)
    val playedMarbles = LinkedList<Int>()
    playedMarbles.add(0)
    var currentMarble = 0
    var currentPlayer = 0
    (1 until numberOfMarbles + 1).forEach { marble ->
        var index = currentMarble + 2
        if (index > (playedMarbles.size)) {
            index -= (playedMarbles.size)
        }
        if (marble.rem(23) == 0) {
            var dropIndex = currentMarble - 7
            if (dropIndex < 0) {
                dropIndex += playedMarbles.size
            }
            players[currentPlayer] += marble
            players[currentPlayer] += playedMarbles[dropIndex]
            playedMarbles.removeAt(dropIndex)
            currentMarble = dropIndex
        } else {
            playedMarbles.add(index, marble)
            currentMarble = index
        }

        currentPlayer = if (currentPlayer == players.size - 1) {
            0
        } else {
            currentPlayer + 1
        }
    }
    println("Highscore: ${players.max()} ")
}

fun solution2(numberOfPlayers: Int, numberOfMarbles: Int) {
    val marbles = numberOfMarbles * 100
    val players = LongArray(numberOfPlayers)
    val playedMarbles = LinkedList<Long>()
    playedMarbles.add(0)
    var iterator = playedMarbles.listIterator()
    var currentPlayer = 0
    for (marble in 1 until marbles) {
        when {
            marble.rem(23) == 0 -> {
                (0..7).onEach {
                    if (iterator.hasPrevious()) {
                        iterator.previous()
                    } else {
                        iterator = playedMarbles.listIterator(playedMarbles.size - 1)
                    }
                }
                val drop = iterator.next()
                iterator.remove()
                players[currentPlayer] += (drop + marble)
                iterator.next()
            }
            iterator.hasNext() -> {
                iterator.next()
                iterator.add(marble.toLong())
            }
            else -> {
                iterator = playedMarbles.listIterator(1)
                iterator.add(marble.toLong())
            }
        }
        currentPlayer = if (currentPlayer == players.size - 1) {
            0
        } else {
            currentPlayer + 1
        }
    }
    println("Highscore: ${players.max()} ")
}


