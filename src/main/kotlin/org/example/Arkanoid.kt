package org.example

import java.util.*


public fun playArkanoid(input: String, queue: Queue<Long>): BigIntCodeComputer {
    val inputSplit = stringToLongList(input)

    val intCodeComputer = BigIntCodeComputer(inputSplit, queue)
   var exit: Boolean
   do {
        exit = intCodeComputer.applyInstructionAtPosition()
    } while (!exit)
    return intCodeComputer
}

fun stringToLongList(input: String) = input.trim().split(",").map { it.toLong() }.toMutableList()
