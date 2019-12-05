package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.Ignore
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay5Test {
    @Test
    @Ignore
    fun samples() {
        assertEquals("1002,4,3,4,99", calculateDay5v2("1002,4,3,4,33", 1))
        assertEquals("1101,100,-1,4,99", calculateDay5v2("1101,100,-1,4,0", 1))
        assertEquals("3,2,99", calculateDay5v2("3,2,0", 99))
        assertEquals("3,2,99", calculateDay5v2("03,2,0", 99))
        calculateDay5v2("04,2,12", 99)
        calculateDay5v2("104,2,12", 99)
    }

    @Test
    fun real() {
        val input = "/day5.txt".loadFromFile()
        val inputSplit = input.trim().split(",").map { it.toInt() }.toMutableList()
        calculateFinalStateday5Part2(inputSplit, 1)

    }

    @Test
    fun realPart2() {
        val input = "/day5.txt".loadFromFile()
        val inputSplit = input.trim().split(",").map { it.toInt() }.toMutableList()
        calculateFinalStateday5Part2(inputSplit, 5)

    }

    private fun calculateDay5v2(input: String, inputParam: Int): String {
        val inputSplit = input.split(",").map { it.toInt() }.toMutableList()
        org.example.applyInstructionAtPosition(inputSplit, 0, inputParam)
        return inputSplit.joinToString(",")
    }

    private fun calculateFinalStateday5Part2(inputInts: MutableList<Int>, firstInput: Int): MutableList<Int> {
        var instructionPointer = 0
        var instructionSize = -1
        while (instructionSize != 0) {
            val newInstructionPointer = applyInstructionAtPosition(inputInts, instructionPointer, firstInput)
            instructionSize = newInstructionPointer - instructionPointer
            instructionPointer = newInstructionPointer
        }
        return inputInts
    }

    private fun String.loadFromFile(): String {
        return AdventOfCodeDay5Test::class.java.getResource(this).readText()
    }
}
