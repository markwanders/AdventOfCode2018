package day24

import java.io.File
import kotlin.math.truncate

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day24.txt")
    solution1(input)
}

fun readFile(file: String) : List<Group> {
    val groups = mutableListOf<Group>()
    var addToInfection = false
    File(file).forEachLine {
        if(it.contains("Infection:")) {
            addToInfection = true
        } else {
            val match = "\\d+".toRegex().findAll(it).map { matchResult -> matchResult.groupValues.map { values -> values.toInt() } }.flatten().toList()
            if (match.isNotEmpty()) {
                val damageType = "([a-zA-Z]*)(?= damage)".toRegex().find(it)!!.groups[0]!!.value
                val group = Group(if(addToInfection) "infection" else "immune", match[0], match[1], match[2], damageType, match[3])
                if("(?<=immune to )(.*)(?=;)".toRegex().containsMatchIn(it)) {
                    group.immunities.addAll("(?<=immune to )(.*)(?=;)".toRegex().find(it)!!.groups[0]!!.value.split(", "))
                }
                if("(?<=weak to )(.*)(?=\\))".toRegex().containsMatchIn(it)) {
                    group.weaknesses.addAll("(?<=weak to )(.*)(?=\\))".toRegex().find(it)!!.groups[0]!!.value.split(", "))
                }
                groups.add(group)
            }
        }
    }
    return groups
}

data class Group(val type: String, var units: Int, var hitPoints: Int, val attackDamage: Int, val attackType: String, val initiative: Int) {
    val weaknesses = mutableListOf<String>()
    val immunities = mutableListOf<String>()
}

fun solution1(groups: List<Group>) {
    while(groups.any { it.type == "infection" && it.units > 0 } && groups.any {  it.type == "immune" && it.units > 0 }) {
//    repeat(1) {
        // do targeting
        val targetingOrder = groups
            .filter { group -> group.units > 0 }
            .sortedWith(compareByDescending<Group> {group -> group.attackDamage*group.units }.thenByDescending { group -> group.initiative })

        val targets = hashMapOf<Int, Int>()
        targetingOrder.forEach { attacker ->
            val possibleTargets = groups.filter { target -> target.type != attacker.type && target.initiative !in targets.values && target.units > 0 }
//            println(targets)
            if(possibleTargets.isNotEmpty()) {
                val damages = possibleTargets.map { target -> target to calculateDamage(attacker, target) }.sortedWith(
                    compareByDescending<Pair<Group, Int>> { target -> target.second }
                        .thenByDescending { target -> target.first.attackDamage * target.first.units }
                        .thenByDescending { target -> target.first.initiative })
                val target = damages.first()
                if(target.second > 0) {
                    targets[attacker.initiative] = target.first.initiative
                }
            }
        }

        // do attacking
        val attackingOrder = groups
            .filter { attacker -> attacker.units > 0 }
            .sortedByDescending { group -> group.initiative }

        attackingOrder.forEach { attacker ->
            val defenderList = groups.filter { defender -> defender.initiative == targets[attacker.initiative]}
            if (defenderList.isNotEmpty()) {
                val defender = defenderList.first()
                val casualties = truncate(calculateDamage(attacker, defender).toDouble()/defender.hitPoints.toDouble()).toInt()
//                println("$attacker attacks $defender for $casualties casualties")
                defender.units -= casualties
            }
        }
        println("Remaining infection: ${groups.filter { it.type == "infection" && it.units > 0}.count()}")
        println("Remaining immune: ${groups.filter { it.type == "immune" && it.units > 0}.count()}")
    }
    val winner =  groups.first { it.units >0 }.type
    val solution = groups.filter { it.units >0 }.sumBy { it.units }
    println("$winner has $solution units remaining")
}

fun calculateDamage(attacker: Group, defender: Group) : Int {
    val baseDamage = attacker.units * attacker.attackDamage
    val modifier = when {
        attacker.attackType in defender.immunities -> 0
        attacker.attackType in defender.weaknesses -> 2
        else -> 1
    }
    return baseDamage * modifier
}