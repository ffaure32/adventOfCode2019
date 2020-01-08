package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay15Test {


    @Test
    fun real() {
        val input = "/day15.txt".loadFromFile()
        val longQueue = initLongQueue(1)
        assertEquals(228, findOxygenSystem(input, longQueue))
    }



    fun findOxygenSystem(input: String, queue: Queue<Long>): Int {
        val inputSplit = stringToLongList(input)

        val droidScreen = DroidScreen()
        val intCodeComputer = BigIntCodeComputer(inputSplit, queue, droidScreen)
        var exit: Boolean
        do {
            exit = intCodeComputer.applyInstructionAtPosition() || droidScreen.isMazeComplete()
        } while (!exit)
        return droidScreen.findPathToExit()
    }

}
