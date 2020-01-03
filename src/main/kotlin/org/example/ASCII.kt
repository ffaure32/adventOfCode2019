package org.example

import java.util.*

fun main(args: Array<String>) {
    val input = "/day17.txt".loadFromFile()
    val longQueue = initLongQueue()
    val playAscii = playAscii(input, longQueue) as ASCIIScreen
    println("SCORE FINAL:" + findIntersections(playAscii.scaffolds))
}

fun findIntersections(scaffolds: MutableMap<LongPosition, Int>): Any? {
    return scaffolds.filter {
        it.value == 35 &&  isIntersection(it, scaffolds)
    }.map{ it.key.x * it.key.y }.sum()
}

private fun isIntersection(
    it: Map.Entry<LongPosition, Int>,
    scaffolds: MutableMap<LongPosition, Int>
): Boolean {
    val position = it.key
    val neighbours = findNeighbours(position, scaffolds)
    return neighbours.count { it == 35 } == 4
}

fun findNeighbours(position : LongPosition, scaffolds: MutableMap<LongPosition, Int>) : List<Int> {
    val result = mutableListOf<Int>()
    result.add(scaffolds.getOrDefault(LongPosition(position.x, position.y-1),0))
    result.add(scaffolds.getOrDefault(LongPosition(position.x, position.y+1),0))
    result.add(scaffolds.getOrDefault(LongPosition(position.x-1, position.y),0))
    result.add(scaffolds.getOrDefault(LongPosition(position.x+1, position.y),0))
    return result
}

public fun playAscii(input: String, queue: Queue<Long>): IntCodeInteraction {
    val inputSplit = stringToLongList(input)

    val ascii = ASCIIScreen(queue)
    val intCodeComputer = BigIntCodeComputer(inputSplit, queue, ascii)
    var exit: Boolean
    do {
        exit = intCodeComputer.applyInstructionAtPosition()
    } while (!exit)
    return ascii
}

class ASCIIScreen(val input : Queue<Long>) : IntCodeInteraction {

    val scaffolds = mutableMapOf<LongPosition, Int>()
    var currentPosition = LongPosition(0, 0)
    override fun printScreen() {
        println()
    }

    override fun resultPart1(): Any {
        return scaffolds
    }

    override fun specificStore(input: Queue<Long>) {
        // NOTHING TO DO
    }

    override fun specificOutput(droidResult: Long) {
        val asciiCode = droidResult.toInt()
        scaffolds.put(currentPosition, asciiCode)
        if(asciiCode == 10) {
            currentPosition = LongPosition(0, currentPosition.y+1)
        } else {
            currentPosition = LongPosition(currentPosition.x+1, currentPosition.y)
        }
        print(asciiCode.toChar())
    }
}

