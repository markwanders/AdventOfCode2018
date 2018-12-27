package day22

import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day22.txt")
    solution1(input)
}

fun readFile(fileName: String): List<Int> {
    val raw = File(fileName).readText()
    return "\\d+".toRegex().findAll(raw).map { it.value.toInt() }.toList()
}

fun solution1(input: List<Int>) {
    val depth = input[0]
    val target = input[1] to input[2]
    val caveSize = 1000 to 1000
    val cave = Array(caveSize.second) { Array(caveSize.first) { Region(0, 0, ' ') } }
    for (y in 0 until caveSize.second) {
        for (x in 0 until caveSize.first) {
            val geologicalIndex = if (x == 0 && y == 0) {
                0
            } else if (x == target.first && y == target.second) {
                0
            } else if (y == 0) {
                x.times(16807)
            } else if (x == 0) {
                y.times(48271)
            } else {
                cave[y][x - 1].erosionLevel.times(cave[y - 1][x].erosionLevel)
            }
            val erosionLevel = (geologicalIndex + depth) % 20183
            val type = when {
                erosionLevel % 3 == 0 -> '.'
                erosionLevel % 3 == 1 -> '='
                erosionLevel % 3 == 2 -> '|'
                else -> {
                    'X'
                }
            }
            cave[y][x] = Region(geologicalIndex, erosionLevel, type)
        }
    }

    var riskRating = 0
    cave.forEach {
        it.forEach {
            riskRating += when(it.type) {
                '.' -> 0
                '=' -> 1
                '|' -> 2
                else -> {
                    0
                }
            }
        }
    }
    println("Risk rating is $riskRating")

    val targetNode = Position(target.first, target.second, 0, false, true)
    val solution = dijkstra(cave, targetNode)
    println("Time it took to reach target: $solution")

}

fun dijkstra(cave: Array<Array<Region>>, targetNode: Position) : Int {
    val queue = PriorityQueue<Position>(Comparator.comparing(Position::time))

    queue.add(Position(0,0, 0,false, true))

    val times = hashMapOf<Position, Int>()

    while (queue.isNotEmpty()) {

        val currentNode = queue.poll()

        if(currentNode in times.keys && times[currentNode]!! <= currentNode.time) {
            continue
        }

        times[currentNode] = currentNode.time

        if (currentNode.x == targetNode.x && currentNode.y == targetNode.y && currentNode.torch == targetNode.torch) {
            return currentNode.time
        }

        val type = cave[currentNode.y][currentNode.x].type
        if(type == '.') {
            if(!currentNode.torch) {
                queue.add(Position(currentNode.x, currentNode.y, currentNode.time + 7, false, true))
            }
            if(!currentNode.climbingGear) {
                queue.add(Position(currentNode.x, currentNode.y, currentNode.time + 7,true, false))
            }
        }
        if(type == '=') {
            queue.add(Position(currentNode.x, currentNode.y, currentNode.time + 7, !currentNode.climbingGear, currentNode.torch))
        }
        if(type == '|') {
            queue.add(Position(currentNode.x, currentNode.y, currentNode.time + 7, currentNode.climbingGear, !currentNode.torch))
        }

        for (x in currentNode.x - 1..currentNode.x + 1) {
            if (x >= 0 && x < cave.first().size && x != currentNode.x) {
                when(cave[currentNode.y][x].type) {
                    '.' ->
                        if((currentNode.torch && !currentNode.climbingGear) || (!currentNode.torch && currentNode.climbingGear)) {
                            queue.add(Position(x, currentNode.y, currentNode.time + 1, currentNode.climbingGear, currentNode.torch))
                        }

                    '=' ->
                        if(!currentNode.torch) {
                            queue.add(Position(x, currentNode.y, currentNode.time + 1, currentNode.climbingGear, currentNode.torch))
                        }

                    '|' ->
                        if(!currentNode.climbingGear) {
                            queue.add(Position(x, currentNode.y, currentNode.time + 1, currentNode.climbingGear, currentNode.torch))
                        }
                }
            }
        }

        for (y in currentNode.y - 1..currentNode.y + 1) {
            if (y >= 0 && y < cave.size &&  y != currentNode.y) {
                when(cave[y][currentNode.x].type) {
                    '.' ->
                        if((currentNode.torch && !currentNode.climbingGear) || (!currentNode.torch && currentNode.climbingGear)) {
                            queue.add(Position(currentNode.x, y, currentNode.time + 1, currentNode.climbingGear, currentNode.torch))
                        }

                    '=' ->
                        if(!currentNode.torch) {
                            queue.add(Position(currentNode.x, y, currentNode.time + 1, currentNode.climbingGear, currentNode.torch))
                        }

                    '|' ->
                        if(!currentNode.climbingGear) {
                            queue.add(Position(currentNode.x, y, currentNode.time + 1, currentNode.climbingGear, currentNode.torch))
                        }
                }
            }
        }
    }

    return 0
}

data class Position(var x: Int, var y: Int, var time: Int, var climbingGear: Boolean, var torch: Boolean) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false
        if (climbingGear != other.climbingGear) return false
        if (torch != other.torch) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + climbingGear.hashCode()
        result = 31 * result + torch.hashCode()
        return result
    }
}


data class Region(val geologicalIndex: Int, val erosionLevel: Int, val type: Char)