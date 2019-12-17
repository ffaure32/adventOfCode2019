package org.example

import java.util.*


public fun playArkanoid(input: String, queue: Queue<Long>): IntCodeInteraction {
    val inputSplit = stringToLongList(input)

    val arkanoidScreen = ArkanoidScreen()
    val intCodeComputer = BigIntCodeComputer(inputSplit, queue, arkanoidScreen)
   var exit: Boolean
   do {
        exit = intCodeComputer.applyInstructionAtPosition()
    } while (!exit)
    return arkanoidScreen
}

fun stringToLongList(input: String) = input.trim().split(",").map { it.toLong() }.toMutableList()
