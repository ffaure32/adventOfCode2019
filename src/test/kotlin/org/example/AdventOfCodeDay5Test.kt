package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay5Test {
    @Test
    fun samples() {
        assertEquals("1002,4,3,4,99", calculateDay5v2("1002,4,3,4,33", initQueue(1)))
        assertEquals("1101,100,-1,4,99", calculateDay5v2("1101,100,-1,4,0", initQueue(1)))
        assertEquals("3,2,99", calculateDay5v2("3,2,0", initQueue(99)))
        assertEquals("3,2,99", calculateDay5v2("03,2,0", initQueue(99)))
        calculateDay5v2("04,2,12", initQueue(99))
        calculateDay5v2("104,2,12", initQueue(99))
    }

    @Test
    fun real() {
        val input = "/day5.txt".loadFromFile()
        val inputSplit = input.trim().split(",").map { it.toInt() }.toMutableList()
        calculateFinalStateday5Part2(inputSplit, initQueue(1))

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
