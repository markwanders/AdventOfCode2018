package day15

import java.io.File
import kotlin.math.absoluteValue

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
    unitList.sortBy { it.x }

    (0..5).forEach {
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
        unitList.forEach { unit -> moveOneStepNearer(unit, findNearestEnemy(unit), battlefield) }
    }
}

data class Unit(var x: Int, var y: Int, var type: Char) {
    var hp = 200
    val ap = 3
}

fun findNearestEnemy(unit: Unit) : Unit {
    var grid = battlefield.toMutableList()
    var simulatedUnit = unit.copy()
    return unitList.filter { enemy -> enemy.type != simulatedUnit.type }.map { enemy ->
        enemy to moveToEnemy(simulatedUnit, enemy, grid)
    }.minBy { it.second }!!.first
}

fun moveToEnemy(unit: Unit, enemy: Unit, grid: MutableList<CharArray>) : Int {
    var steps = 0
    while ((unit.x - enemy.x).absoluteValue > 1 || (unit.y - enemy.y).absoluteValue > 1) {
        moveOneStepNearer(unit, enemy, grid)
        steps++
    }
    return steps
}

fun moveOneStepNearer(unit: Unit, enemy: Unit, grid: MutableList<CharArray>) {
    grid[unit.x][unit.y] = '.'
    val difX = unit.x - enemy.x
    val difY = unit.y - enemy.y
    if(difX.absoluteValue >= difY.absoluteValue) {
        unit.x = if(difX > 0) {
            var char = grid[unit.x -1][unit.y]
            if(char == '.') {
                unit.x - 1
            } else {
                unit.x
            }
        } else {
            var char = grid[unit.x +1][unit.y]
            if(char == '.') {
                unit.x + 1
            } else {
                unit.x
            }
        }
    } else {
        unit.y = if(difY > 0) {
            unit.y - 1
        } else {
            unit.y + 1
        }
    }
    grid[unit.x][unit.y] = unit.type
}