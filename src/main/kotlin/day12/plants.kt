package day12

import java.io.File

fun main(args: Array<String>) {
    val input = readFile("src/main/resources/day12.txt")
    solution1(input)
    solution2(input)
}

fun readFile(fileName: String): Pair<String, Map<String, String>> {
    var init = ""
    val initRegex = "initial state:\\s*([^\\n\\r]*)".toRegex()
    val ruleRegex = ".*(?= => )".toRegex()
    val resultRegex = "(?<= => ).*".toRegex()
    val ruleMap = mutableMapOf<String, String>()
    File(fileName).forEachLine { s ->
        if (initRegex.containsMatchIn(s)) {
            init = initRegex.matchEntire(s)!!.groups[1]!!.value
        } else if (ruleRegex.containsMatchIn(s)) {
            val rule = ruleRegex.find(s)!!.groups[0]!!.value
            val result = resultRegex.find(s)!!.groups[0]!!.value
            ruleMap[rule] = result
        }
    }
    return Pair(init, ruleMap)
}

fun solution1(input: Pair<String, Map<String, String>>) {
    val initial = input.first
    val rules = input.second
    var previous = initial
    var shift = 0
    (0..20).forEach {
        if (!previous.startsWith("...")) {
            previous = "...$previous"
            shift += 3
        }
        if (!previous.endsWith("...")) {
            previous = "$previous..."
        }
        var next = ".".repeat(previous.length)
        rules.keys.forEach { s ->
            if (s in previous) {
                var match = -1
                do {
                    match = previous.indexOf(s, match + 1)
                    next = next.replaceRange(match + 2, match + 3, rules[s]!!)
                    val nextMatch = previous.indexOf(s, match + 1)
                } while (nextMatch > 0)
            }
        }
        if (it < 20) previous = next
    }
    var total = 0
    previous.toCharArray().forEachIndexed { index, pot ->
        if (pot == '#') {
            total += index - shift
        }
    }
    println(total)
}

fun solution2(input: Pair<String, Map<String, String>>) {
    val initial = input.first
    val rules = input.second
    var previous = initial
    var shift: Long = 0
    var stable = false
    val generations: Long = 50000000000
    (0..generations).forEach {
        if (!stable) {
            if (!previous.startsWith("...")) {
                previous = "...$previous"
                shift += 3
            }
            if (previous.startsWith(".....")) {
                previous = previous.substring(1)
                shift -= 1
            }
            if (!previous.endsWith("...")) {
                previous = "$previous..."
            }
            if (previous.endsWith("....")) {
                previous = previous.dropLast(2)
            }
            var next = ".".repeat(previous.length)
            rules.keys.forEach { s ->
                if (s in previous) {
                    var match = -1
                    do {
                        match = previous.indexOf(s, match + 1)
                        next = next.replaceRange(match + 2, match + 3, rules[s]!!)
                        val nextMatch = previous.indexOf(s, match + 1)
                    } while (nextMatch > 0)
                }
            }
            if (previous.trim('.') == next.trim('.')) {
                stable = true
                shift += 1
            }
            if (it < generations) previous = next
        } else {
            shift -= 1
        }
    }
    var total : Long = 0
    previous.toCharArray().forEachIndexed { index, pot ->
        if (pot == '#') {
            total += index - shift
        }
    }
    println(total)
}