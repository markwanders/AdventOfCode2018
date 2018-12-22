package day15

import java.io.File
import java.util.*

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
    battlefield.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            if (char == 'E' || char == 'G') {
                unitList.add(Unit(Position(x, y), char))
                battlefield[y][x] = '.'
            }
        }
    }

    (0..3).forEach {
        unitList.sortedWith(compareBy({ unit -> unit.position.y }, {  unit -> unit.position.x }))
        unitList.forEach { unit -> // Reset distance
            unit.position.distance = Int.MAX_VALUE
            unit.position.shortestPath = LinkedList()
        }
        print()

        unitList.forEach { unit ->
//            println("Finding targets for ${unit.type} at ${unit.position.x}, ${unit.position.y}")
            val targets = unitList
                .filter { enemy -> enemy.type != unit.type } //determine targets for each unit
            val neighbouringTargets =
                getNeighbours(unit.position)
                    .filter { neighbour -> neighbour in targets.map { enemy -> enemy.position } } // Are we already next to a target?
            if (neighbouringTargets.isEmpty()) { // Move to nearest enemy
                val tilesAdjacentToAnyTarget = findTilesAdjacentToTargets(targets).toMutableList()
                tilesAdjacentToAnyTarget.sortedWith(compareBy({ position -> position.y }, {  position -> position.x }))
//                println("All possible destinations: $tilesAdjacentToAnyTarget")
                if(tilesAdjacentToAnyTarget.isNotEmpty()) {
                    val routes = tilesAdjacentToAnyTarget.map { tile -> tile to dijkstra(unit.position, tile) }
                    val shortestDistance = routes.minBy { route -> route.second!!.size }!!.second!!.size
//                    println("Shortest distance to closest tile next to target: $shortestDistance")
                    val selected = routes
                        .filter { route -> route.second!!.size == shortestDistance }
                        .map { route -> route.first }
                        .sortedWith(compareBy({ position -> position.y }, { position -> position.x }))
                        .first()
                    val nextPosition = dijkstra(unit.position, selected)!!.first()
//                    println("Unit moves to: $nextPosition")

                    unitList.filter { nextUnit -> nextUnit.position == unit.position }.first().position = nextPosition
                }
            }

        }
    }
}

data class Unit(var position: Position, var type: Char) {
    var hp = 200
    val ap = 3
}

fun print() { //Print battlefield
    battlefield.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            if (unitList.any { unit -> unit.position.x == x && unit.position.y == y }) {
//                print(unitList.indexOf(unitList.first { unit -> unit.position.x == x && unit.position.y == y }))
                print(unitList.first { unit -> unit.position.x == x && unit.position.y == y }.type)
            } else {
                print(char)
            }
        }
        println()
    }
}

fun findTilesAdjacentToTargets(targets: List<Unit>): List<Position> {//get adjacent, unoccupied tiles for each target
    val tilesAdjacentToAnyTarget = mutableListOf<Position>()
    targets.forEach { target ->
        val adjacentTilesToTarget = getNeighbours(target.position)
            .filter { battlefield[it.y][it.x] == '.' }
            .filter { it !in unitList.map { unit -> unit.position } }
//        println("Found target ${target.type} at ${target.position} with free adjacent tiles: $adjacentTilesToTarget")
        tilesAdjacentToAnyTarget.addAll(adjacentTilesToTarget)
    }
    return tilesAdjacentToAnyTarget
}

fun getNeighbours(center: Position): List<Position> {
    return listOf(
        Position(center.x, center.y - 1),
        Position(center.x + 1, center.y),
        Position(center.x, center.y + 1),
        Position(center.x - 1, center.y)
    ).sortedWith(compareBy({ position -> position.y }, {  position -> position.x }))
}

fun dijkstra(from: Position, to: Position): List<Position>? {
    val settledNodes = mutableListOf<Position>()
    val unsettledNodes = mutableListOf<Position>()

    unsettledNodes.add(from)

    var currentNode = from
    currentNode.distance = 0

    while (unsettledNodes.size != 0) {

        unsettledNodes += getNeighbours(currentNode)
            .filter { battlefield[it.y][it.x] == '.' }
            .filter { it !in unitList.map { unit -> unit.position } }
            .filter { it !in unsettledNodes }.filter { it !in settledNodes }
            .filter { it != currentNode }

        unsettledNodes.forEach { position ->
            //calculate distances
            val distance = currentNode.distance + 1
            if (distance < position.distance) {
                val shortestPath = LinkedList(currentNode.shortestPath)
                position.distance = distance
                shortestPath.add(currentNode)
                position.shortestPath = shortestPath
            }
        }

        unsettledNodes -= currentNode
        settledNodes += currentNode

        if (currentNode == to) {
            val route = settledNodes.filter { position -> position == to }.first().shortestPath
            route.add(to)
            route.removeFirst()
            return route
        }

        currentNode = unsettledNodes
            .sortedBy { it.shortestPath.sumBy { it.distance } }
            .firstOrNull() ?: break
    }

    return null
}

data class Position(var x: Int, var y: Int) {
    var shortestPath = LinkedList<Position>()
    var distance = Int.MAX_VALUE
}
