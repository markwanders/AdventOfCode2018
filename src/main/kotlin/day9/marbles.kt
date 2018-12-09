package day9

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day9.txt")
    solution1(input[0],input[1])
}

fun readFile(fileName: String): List<Int> {
    val raw = File(fileName).readText()
    return "\\d+".toRegex().findAll(raw).map { it.value.toInt() }.toList()
}

fun solution1(numberOfPlayers: Int, numberOfMarbles: Int) {
    val players = IntArray(numberOfPlayers)
    val playedMarbles = mutableListOf<Int>()
    playedMarbles.add(0)
    var currentMarble = 0
    var currentPlayer = 0
    for(x in 1 until numberOfMarbles + 1) {
        var index = currentMarble + 2
        if(index > (playedMarbles.size)) {
            index -= (playedMarbles.size)
        }
        if(x.rem(23) == 0) {
            var dropIndex = currentMarble - 7
            if(dropIndex < 0) {
                dropIndex += playedMarbles.size
            }
            players[currentPlayer] += x
            players[currentPlayer] += playedMarbles[dropIndex]
            playedMarbles.removeAt(dropIndex)
            currentMarble = dropIndex
        } else {
            playedMarbles.add(index, x)
            currentMarble = index
        }

        currentPlayer = if(currentPlayer == players.size - 1) {
            0
        } else {
            currentPlayer + 1
        }

    }
    println(players.max())
}