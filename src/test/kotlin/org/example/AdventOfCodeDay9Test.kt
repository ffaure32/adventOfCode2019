package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay9Test {
    @Test
    fun samples() {
        assertEquals("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99", calculateDay9("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"))
        assertEquals(calculateDay9("1102,34915192,34915192,7,4,7,99,0").length, 16)
        assertEquals("1125899906842624", calculateDay9("104,1125899906842624,99"))
    }

    private fun calculateDay9(input: String, vararg queue : Long): String {
        val inputSplit = stringToLongList(input)
        val intCodeComputer = IntCodeComputer(inputSplit, 0, initLongQueue(queue[0]), 0)
        var exit = false
        do {
            exit = intCodeComputer.applyInstructionAtPosition()
        } while (!exit)
        return intCodeComputer.output.map { it.toString()}.joinToString(",")
    }

    @Test
    fun real() {
        val input = "/day9.txt".loadFromFile()
        print(calculateDay9(input, 1))
        //val inputSplit = input.trim().split(",").map { it.toInt() }.toMutableList()
        //calculateFinalStateday5Part2(inputSplit, initQueue(1))

    }

    @Test
    fun realPart2() {
        val input = "/day5.txt".loadFromFile()
        val inputSplit = input.trim().split(",").map { it.toInt() }.toMutableList()
        calculateFinalStateday5Part2(inputSplit, initQueue(5))

    }

    private fun calculateDay5v2(input: String, inputParam: Queue<Int>): String {
        val inputSplit = stringToIntList(input)
        applyInstructionAtPosition(inputSplit, 0, inputParam)
        return inputSplit.joinToString(",")
    }

    private fun calculateFinalStateday5Part2(inputInts: MutableList<Int>, firstInput: Queue<Int>): MutableList<Int> {
        var instructionPointer = 0
        var instructionSize = -1
        while (instructionSize != 0) {
            val newInstructionPointer = applyInstructionAtPosition(inputInts, instructionPointer, firstInput)
            instructionSize = newInstructionPointer - instructionPointer
            instructionPointer = newInstructionPointer
        }
        return inputInts
    }
}
