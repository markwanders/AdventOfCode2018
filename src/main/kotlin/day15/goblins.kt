package day15

import java.io.File
import java.util.*

val battlefield = readFile("src/main/resources/day15.txt")
var unitList = mutableListOf<Unit>()

fun main(args: Array<String>) {
    solution2()
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
                val unit = Unit(Position(x, y), char)
                if(unit.type == 'E') unit.ap = 10
                unitList.add(unit)
                battlefield[y][x] = '.'
            }
        }
    }

    var combatOver = false
    var rounds = 0
    while(!combatOver) {
        print()
        unitList.forEach { unit -> // Reset distance
            unit.position.distance = Int.MAX_VALUE
            unit.position.shortestPath = LinkedList()
        }

        unitList.sortedWith(compareBy({ unit -> unit.position.y }, {  unit -> unit.position.x })).forEach { unit ->
            if (unit.hp > 0) {
                val targets = unitList
                    .filter { enemy -> enemy.type != unit.type && enemy.hp > 0 } //determine targets for each unit
                var neighbouringTargets =
                    getNeighbours(unit.position)
                        .filter { neighbour -> neighbour in targets.map { enemy -> enemy.position } } // Are we already next to a target?
                if (neighbouringTargets.isEmpty()) { // Move to nearest enemy
                    val tilesAdjacentToAnyTarget = findTilesAdjacentToTargets(targets).toMutableList()
                    tilesAdjacentToAnyTarget.sortedWith(
                        compareBy(
                            { position -> position.y },
                            { position -> position.x })
                    )
                    if (tilesAdjacentToAnyTarget.isNotEmpty()) {
                        val routes = tilesAdjacentToAnyTarget.map { tile -> tile to dijkstra(unit.position, tile) }.filter { it.second != null }
                        if(routes.isNotEmpty()) {
                            val shortestDistance = routes.minBy { route -> route.second!!.size }!!.second!!.size
                            val selected = routes
                                .filter { route -> route.second!!.size == shortestDistance }
                                .map { route -> route.first }
                                .sortedWith(compareBy({ position -> position.y }, { position -> position.x }))
                                .first()
                            val nextPosition = dijkstra(unit.position, selected)!!.first()

                            unitList.first { nextUnit -> nextUnit.position == unit.position && nextUnit.type == unit.type && nextUnit.hp > 0 }.position = nextPosition

                            neighbouringTargets =
                                    getNeighbours(unit.position)
                                        .filter { neighbour -> neighbour in targets.map { enemy -> enemy.position } }
                        }
                    }

                }
                if (neighbouringTargets.isNotEmpty()) { //Target in range, fight!
                    val neighbouringEnemies = unitList.filter { enemy -> enemy.position in neighbouringTargets }.filter { enemy -> enemy.hp > 0 }
                    val enemy = neighbouringEnemies
                        .filter { enemy -> enemy.hp == neighbouringEnemies.minBy { it.hp }!!.hp }
                        .sortedWith(compareBy({ target -> target.position.y }, { target -> target.position.x }))
                        .first()
                    enemy.hp -= unit.ap
                }
            }
            combatOver = unitList.none { elf -> elf.type == 'E' && elf.hp > 0 } ||
                    unitList.none { goblin -> goblin.type == 'G' && goblin.hp > 0 }
        }
        if(!combatOver) rounds ++
    }

    println("Finished after $rounds rounds")
    println("Total remaining hp: ${unitList.filter { it.hp > 0 }.sumBy { it.hp }}")
    println("Solution: ${rounds * unitList.filter { it.hp > 0 }.sumBy { it.hp }}")

}

fun solution2() {
    var elvesWin = false
    var attackPower = 4
    while(!elvesWin) {
        println("Beginning simulation with attack power $attackPower")
        unitList = mutableListOf()
        battlefield.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char == 'E' || char == 'G') {
                    val unit = Unit(Position(x, y), char)
                    if(unit.type == 'E') unit.ap = attackPower
                    unitList.add(unit)
                }
            }
        }

        var combatOver = false
        var rounds = 0
        while (!combatOver) {

            unitList.forEach { unit ->
                // Reset distance
                unit.position.distance = Int.MAX_VALUE
                unit.position.shortestPath = LinkedList()
            }
            unitList.sortedWith(compareBy({ unit -> unit.position.y }, { unit -> unit.position.x })).forEach { unit ->
                if (!combatOver) {
                    if (unit.hp > 0) {
                        val targets = unitList
                            .filter { enemy -> enemy.type != unit.type && enemy.hp > 0 } //determine targets for each unit
                        var neighbouringTargets = // Are we already next to a target?
                            getNeighbours(unit.position)
                                .filter { neighbour -> neighbour in targets.map { enemy -> enemy.position } }

                        if (neighbouringTargets.isEmpty()) { // Move to nearest enemy
                            val tilesAdjacentToAnyTarget = findTilesAdjacentToTargets(targets)
                            if (tilesAdjacentToAnyTarget.isNotEmpty()) {
                                val routes =
                                    tilesAdjacentToAnyTarget.map { tile -> tile to dijkstra(unit.position, tile) }
                                        .filter { it.second != null }
                                if (routes.isNotEmpty()) {
                                    val shortestDistance = routes.minBy { route -> route.second!!.size }!!.second!!.size
                                    val selected = routes
                                        .filter { route -> route.second!!.size == shortestDistance }
                                        .map { route -> route.first }
                                        .sortedWith(compareBy({ position -> position.y }, { position -> position.x }))
                                        .first()
                                    val nextPosition = dijkstra(unit.position, selected)!!.first()

                                    unitList.filter { nextUnit -> nextUnit.position == unit.position && nextUnit.type == unit.type && nextUnit.hp > 0 }.first().position = nextPosition

                                    neighbouringTargets = getNeighbours(unit.position)
                                                .filter { neighbour -> neighbour in targets.map { enemy -> enemy.position } }
                                }
                            }

                        }
                        neighbouringTargets = neighbouringTargets.sortedWith(compareBy({ it.y }, { it.x }))
                        if (neighbouringTargets.isNotEmpty()) { //Target in range, fight!
                            val neighbouringEnemies = unitList.filter { enemy -> enemy.position in neighbouringTargets }
                                .filter { enemy -> enemy.hp > 0 }
                            val enemy = neighbouringEnemies
                                .filter { enemy -> enemy.hp == neighbouringEnemies.minBy { it.hp }!!.hp }
                                .sortedWith(compareBy({ enemy -> enemy.position.y }, { enemy -> enemy.position.x }))
                                .first()
                            enemy.hp -= unit.ap
                            if (enemy.hp <= 0 && enemy.type == 'E') {
                                combatOver = true
                                return@forEach
                            }
                        }
                    }

                    combatOver = unitList.none { elf -> elf.type == 'E' && elf.hp > 0 } || unitList.none { goblin -> goblin.type == 'G' && goblin.hp > 0 }
                }
            }
            if (!combatOver) rounds++
        }

        elvesWin = unitList.none { goblin -> goblin.type == 'G' && goblin.hp > 0 } && unitList.none { elf -> elf.type == 'E' && elf.hp <= 0 }
        println("With attack power $attackPower, elves win: $elvesWin")
        if(!elvesWin) {
            attackPower++
        } else {
            print()
            println("Finished after $rounds rounds")
            println("Total remaining hp: ${unitList.filter { it.hp > 0 }.sumBy { it.hp }}")
            println("Solution: ${rounds * unitList.filter { it.hp > 0 }.sumBy { it.hp }}")
        }
    }
}

data class Unit(var position: Position, var type: Char) {
    var hp = 200
    var ap = 3
}

fun print() { //Print battlefield
    battlefield.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            if (unitList.any { unit -> unit.position.x == x && unit.position.y == y && unit.hp > 0}) {
                print(unitList.first { unit -> unit.position.x == x && unit.position.y == y && unit.hp > 0 }.type)
            } else if(char == 'E' || char == 'G'){
                print('.')
            } else {
                print(char)
            }
        }
        println(unitList.filter { it.position.y == y }.map { it -> it.type to it.hp })
    }
}

fun findTilesAdjacentToTargets(targets: List<Unit>): List<Position> {//get adjacent, unoccupied tiles for each target
    val tilesAdjacentToAnyTarget = mutableListOf<Position>()
    targets.forEach { target ->
        val adjacentTilesToTarget = getNeighbours(target.position)
            .filterNot { battlefield[it.y][it.x] == '#' }
            .filter { it !in unitList.filter { unit -> unit.hp > 0 }.map { unit -> unit.position } }
        tilesAdjacentToAnyTarget.addAll(adjacentTilesToTarget)
    }
    return tilesAdjacentToAnyTarget.sortedWith(
        compareBy(
            { position -> position.y },
            { position -> position.x })
    )
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
            .filterNot { battlefield[it.y][it.x] == '#' }
            .filter { it !in unitList.filter { unit -> unit.hp > 0 }.map { unit -> unit.position } }
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
