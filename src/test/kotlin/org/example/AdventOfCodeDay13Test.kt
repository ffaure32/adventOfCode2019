package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay13Test {

    private fun calculateDay13(input: String, queue : Queue<Long>): String {
        val inputSplit = stringToLongList(input)

        val intCodeComputer = BigIntCodeComputer(inputSplit, queue)
        var exit = false
        do {
            exit = intCodeComputer.applyInstructionAtPosition()
        } while (!exit)
        return intCodeComputer.output.map { it.toString()}.joinToString(",")
    }

    @Test
    fun real() {
        val input = "/day13.txt".loadFromFile()
        assertEquals("3100786347", calculateDay13(input, initLongQueue()))

    }
}
