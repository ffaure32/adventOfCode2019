package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.lang.RuntimeException
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay2Test {
    @Test
    fun samples() {
        assertEquals("2,0,0,0,99", calculateFinalState("1,0,0,0,99").joinToString(","))
        assertEquals("2,3,0,6,99", calculateFinalState("2,3,0,3,99").joinToString(","))
        assertEquals("2,4,4,5,99,9801", calculateFinalState("2,4,4,5,99,0").joinToString(","))
        assertEquals("30,1,1,4,2,5,6,0,99", calculateFinalState("1,1,1,4,99,5,6,0,99").joinToString(","))

    }

    @Test
    fun `part 1`() {
        val input = "/day2.txt".loadFromFile()
        val instructions = Instructions(input)
        assertEquals(3716250, calculateFinalState(instructions.inputList())[0])

    }

    @Test
    fun `part 2`() {
        val input = "/day2.txt".loadFromFile()
        var result = findOutput(input, 19690720)
        assertEquals(6472, result)
    }

    private fun findOutput(input: String, expectedFinalState: Int): Int {
        for (noun in 0..99) {
            for(verb in 0..99) {
                val instructions = Instructions(input, noun, verb)
                if(expectedFinalState == calculateFinalState(instructions.inputList())[0]) {
                    return 100 * noun + verb
                }
            }
        }
        throw RuntimeException("unreachable code")
    }

    private fun calculateFinalState(input: String): MutableList<Int> {
        val toMutableList = stringToIntList(input)
        return calculateFinalState(toMutableList)
    }

    private fun calculateFinalState(inputInts: MutableList<Int>): MutableList<Int> {
        var instructionPointer = 0
        var instructionSize = -1
        while (instructionSize != 0) {
            val newInstructionPointer = applyInstructionAtPosition(inputInts, instructionPointer, initQueue(0))
            instructionSize = newInstructionPointer - instructionPointer
            instructionPointer = newInstructionPointer
        }
        return inputInts
    }
}

data class Instructions(val input : String, val noun : Int = 12, val verb: Int = 2) {
    fun inputList(): MutableList<Int> {
        val inputInts = stringToIntList(input)
        inputInts[1] = noun
        inputInts[2] = verb
        return inputInts
    }
}

fun stringToIntList(input: String) = input.trim().split(",").map { it.toInt() }.toMutableList()
fun stringToLongList(input: String) = input.trim().split(",").map { it.toLong() }.toMutableList()
