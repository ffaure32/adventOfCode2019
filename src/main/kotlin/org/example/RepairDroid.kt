package org.example

import java.util.*

fun main(args: Array<String>) {
    val input = "/day15.txt".loadFromFile()
    val longQueue = initLongQueue(1)
    println("SCORE FINAL:" + findOxygenSystem(input, longQueue))
}



fun findOxygenSystem(input: String, queue: Queue<Long>): BigIntCodeComputer {
    val inputSplit = stringToLongList(input)

    val droidScreen = DroidScreen()
    val intCodeComputer = BigIntCodeComputer(inputSplit, queue, droidScreen)
   var exit: Boolean
    val fillPath = FillPath(droidScreen)
    do {
        exit = intCodeComputer.applyInstructionAtPosition()
        val findAccessiblePositions = fillPath.findAccessiblePositions()
    } while (!exit)
    return intCodeComputer
}
class FillPath(val computer : DroidScreen) {
    fun findAccessiblePositions(): Set<Position> {
        val alreadyVisited = mutableSetOf<Position>()
        return accessiblePositions(computer.currentPosition, alreadyVisited)
    }

    fun accessiblePositions(position: Position, alreadyVisited: MutableSet<Position>): MutableSet<Position> {
        val result = mutableSetOf<Position>()
        val neighbours = getNeighbours(position).minus(alreadyVisited)
        alreadyVisited.addAll(neighbours)
        val currentFilter = neighbours.filter {
            val char = computer.screenInfo.getOrDefault(it, '#')
            char == 'O' || char == '.' || char == 'D'
        }.toSet()
        result.addAll(currentFilter)
        currentFilter.forEach {
            result.addAll(accessiblePositions(it, alreadyVisited))
        }
        return result
    }
}