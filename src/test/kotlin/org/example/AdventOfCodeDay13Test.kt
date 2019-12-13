package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.*
import javax.swing.JFrame
import javax.swing.SwingUtilities
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay13Test {

    private fun calculateDay13(input: String, queue : Queue<Long>): Int {
        val inputSplit = stringToLongList(input)

        val intCodeComputer = BigIntCodeComputer(inputSplit, queue)
        var exit = false
        do {
            exit = intCodeComputer.applyInstructionAtPosition()
        } while (!exit)
        return intCodeComputer.screen.countTiles(2L)
    }

    @Test
    fun real() {
        val input = "/day13.txt".loadFromFile()
        assertEquals(363, calculateDay13(input, initLongQueue()))

    }

    @Test
    fun realPart2() {
        val input = "/day13part2.txt".loadFromFile()
        assertEquals(363, calculateDay13(input, initLongQueue()))

    }
}
