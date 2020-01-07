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
    do {
        exit = intCodeComputer.applyInstructionAtPosition()
    } while (!exit)
    return intCodeComputer
}
