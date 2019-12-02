package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay2Test {
    @Test
    fun `samples`() {
        assertEquals("2,0,0,0,99", calculateFinalState("1,0,0,0,99"))
        assertEquals("2,3,0,6,99", calculateFinalState("2,3,0,3,99"))
        assertEquals("2,4,4,5,99,9801", calculateFinalState("2,4,4,5,99,0"))
        assertEquals("30,1,1,4,2,5,6,0,99", calculateFinalState("1,1,1,4,99,5,6,0,99"))

    }

    @Test
    fun `part 1`() {
        val input = loadFromFile("/day2.txt")
        val instructions = Instructions(input)
        assertEquals("3716250", calculateFinalState(instructions.inputList()).split(",")[0])

    }

    @Test
    fun `part 2`() {
        val input = loadFromFile("/day2.txt")
        for (noun in 0..99) {
            for(verb in 0..99) {
                val instructions = Instructions(input, noun, verb)
                if("19690720".equals(calculateFinalState(instructions.inputList()).split(",")[0])) {
                    assertEquals(72, verb)
                    assertEquals(64, noun)
                    assertEquals(6472, 100 * noun + verb)
                    return
                }

            }
        }

    }

    private fun calculateFinalState(input: String): String {
        val toMutableList = stringToIntList(input)
        return calculateFinalState(toMutableList)
    }

    private fun calculateFinalState(inputInts: MutableList<Int>): String {
        var instructionPointer = 0
        var instructionSize = 0
        while (instructionSize != 1) {
            instructionSize = applyToIndex(inputInts, instructionPointer)
            instructionPointer += instructionSize
        }
        return inputInts.joinToString(",")


    }

    private fun applyToIndex(inputInts: MutableList<Int>, index: Int): Int {
        val opcode = inputInts[index]
        if(opcode == 99) {
            return 1
        }
        val left = inputInts[index + 1]
        val right = inputInts[index + 2]
        val outputPosition = inputInts[index + 3]
        if (opcode == 1)
            inputInts[outputPosition] = inputInts[left] + inputInts[right]
        else
            inputInts[outputPosition] = inputInts[left] * inputInts[right]
        return 4
    }

    private fun loadFromFile(fileName: String): String {
        return AdventOfCodeDay2Test::class.java.getResource(fileName).readText()
    }
}

public data class Instructions(val input : String, val noun : Int = 12, val verb: Int = 2) {
    public fun inputList(): MutableList<Int> {
        val inputInts = stringToIntList(input)
        inputInts[1] = noun
        inputInts[2] = verb
        return inputInts
    }

}

public fun stringToIntList(input: String) = input.trim().split(",").map { it.toInt() }.toMutableList()
