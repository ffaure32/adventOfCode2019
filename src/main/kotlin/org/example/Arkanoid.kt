package org.example

import java.awt.EventQueue
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.*
import javax.swing.JFrame

fun main(args: Array<String>) {
    val input = "/day13part2.txt".loadFromFile()
    println("SCORE FINAL:"+calculateDay13(input, initLongQueue()))
}

private fun calculateDay13(input: String, queue : Queue<Long>): Long {
    val inputSplit = stringToLongList(input)

    val intCodeComputer = BigIntCodeComputer(inputSplit, queue)
    var exit = false
    do {
        exit = intCodeComputer.applyInstructionAtPosition()
    } while (!exit)
    return intCodeComputer.screen.currentScore
}

fun stringToLongList(input: String) = input.trim().split(",").map { it.toLong() }.toMutableList()
