package day15

import java.io.File

val battlefield = readFile("src/main/resources/test.txt")
val unitList = mutableListOf<Unit>()

fun main(args: Array<String>) {
    solution1()
}

fun readFile(fileName: String): MutableList<CharArray> {
    val battlefield = mutableListOf<CharArray>()
    File(fileName).forEachLine {
        battlefield.add(it.toCharArray())
    }
    return battlefield
}

fun solution1() {
    battlefield.forEachIndexed { y,  line ->
        line.forEachIndexed { x, char ->
            if (char == 'E' || char == 'G') {
                unitList.add(Unit(x, y, char))
                battlefield[y][x] = '.'
            }
        }
    }

    unitList.sortBy { it.x }

    (0..1).forEach {
        print()
        unitList.forEach {unit ->
            println("Finding targets for ${unit.type} at ${unit.x}, ${unit.y}")
            val targets = unitList.filter { enemy -> enemy.type != unit.type } //determine targets for each unit
            val tilesAdjacentToAnyTarget = findTilesAdjacentToTargets(targets).toMutableList()
            tilesAdjacentToAnyTarget.sortBy { tile-> tile.second }
            println("All possible destinations: $tilesAdjacentToAnyTarget")
        }
    }
}

data class Unit(var x: Int, var y: Int, var type: Char) {
    var hp = 200
    val ap = 3
}

fun print() { //Print battlefield
    battlefield.forEachIndexed { y,  line ->
        line.forEachIndexed { x, char ->
            if(unitList.any { unit -> unit.x == x && unit.y == y }) {
                print(unitList.first {unit -> unit.x == x && unit.y == y  }.type)
            } else {
                print(char)
            }
        }
        println()
    }
}

fun findTilesAdjacentToTargets(targets: List<Unit>) : List<Pair<Int, Int>> {//get adjacent, unoccupied tiles for each target
    val tilesAdjacentToAnyTarget = mutableListOf<Pair<Int, Int>>()
    targets.forEach {target ->
        val adjacentTilesToTarget = mutableListOf<Pair<Int, Int>>()
        (target.y-1..target.y+1).forEach { y ->
            if(battlefield[y][target.x] == '.' && y != target.y) {
                adjacentTilesToTarget.add(Pair(target.x, y))
            }
        }
        (target.x-1..target.x+1).forEach { x ->
            if(battlefield[target.y][x] == '.' && x != target.x) {
                adjacentTilesToTarget.add(Pair(x, target.y))
            }
        }
        println("Found target ${target.type} at ${target.x}, ${target.y} with free adjacent tiles: $adjacentTilesToTarget")
        tilesAdjacentToAnyTarget.addAll(adjacentTilesToTarget)
    }
    return tilesAdjacentToAnyTarget
}