package org.example

import java.util.*

fun main(args: Array<String>) {
    val input = "/day15.txt".loadFromFile()
    val longQueue = initLongQueue(1)
    println("SCORE FINAL:" + findOxygenSystem(input, longQueue))
}

public fun findOxygenSystem(input: String, queue: Queue<Long>): BigIntCodeComputer {
    val inputSplit = stringToLongList(input)

    val intCodeComputer = BigIntCodeComputer(inputSplit, queue)
   var exit: Boolean
   do {
        exit = intCodeComputer.applyInstructionAtPosition()
    } while (!exit)
    return intCodeComputer
}