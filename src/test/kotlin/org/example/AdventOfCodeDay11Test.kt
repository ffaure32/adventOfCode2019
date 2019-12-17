package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay11Test {
    @Test
    fun samples() {
        assertEquals("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99", calculateDay11("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99", initLongQueue()))
        assertEquals(calculateDay11("1102,34915192,34915192,7,4,7,99,0", initLongQueue()).length, 16)
        assertEquals("1125899906842624", calculateDay11("104,1125899906842624,99", initLongQueue()))
    }

    private fun calculateDay11(input: String, queue : Queue<Long>): String {
        val inputSplit = stringToLongList(input)

        val paintingRobotScreen = PaintingRobotScreen(queue)
        val intCodeComputer = BigIntCodeComputer(inputSplit, queue, paintingRobotScreen)
        var exit = false
        do {
            exit = intCodeComputer.applyInstructionAtPosition()
        } while (!exit)
        return paintingRobotScreen.resultPart1().toString()
    }

    @Test
    fun real() {
        val input = "/day11Max.txt".loadFromFile()
        assertEquals("3100786347", calculateDay11(input, initLongQueue(0)))

    }

    @Test
    fun realPart2() {
        val input = "/day11.txt".loadFromFile()
        assertEquals("3100786347", calculateDay11(input, initLongQueue(1)))

    }

}
