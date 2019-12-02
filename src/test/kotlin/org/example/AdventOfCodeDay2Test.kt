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

    private fun calculateFinalState(input: String): String {
        val inputInts = input.split(",").map { it.toInt()}.toMutableList()
        var index = 0
        var exit = false
        while (!exit) {
            exit = applyToIndex(inputInts, index)
            index += 4
        }
        return inputInts.joinToString(",")


    }

    private fun applyToIndex(inputInts: MutableList<Int>, index: Int): Boolean {
        val opcode = inputInts[index]
        if(opcode == 99) {
            return true
        }
        val left = inputInts[index + 1]
        val right = inputInts[index + 2]
        val outputPosition = inputInts[index + 3]
        if (opcode == 1)
            inputInts[outputPosition] = inputInts[left] + inputInts[right]
        else
            inputInts[outputPosition] = inputInts[left] * inputInts[right]
        return false
    }


    @Test
    fun `part 1`() {
        val input = loadFromFile("/day2.txt", 12, 2)
        assertEquals("3716250", calculateFinalState(input).split(",")[0])

    }

    @Test
    fun `part 2`() {
        for (noun in 0..99) {
            for(verb in 0..99) {
                val input = loadFromFile("/day2.txt", noun, verb)
                if("19690720".equals(calculateFinalState(input).split(",")[0])) {
                    assertEquals(72, verb)
                    assertEquals(64, noun)
                    assertEquals(6472, 100 * noun + verb)
                }

            }
        }

    }

    private fun loadFromFile(fileName: String, noun: Int, verb: Int): String {
        val content = AdventOfCodeDay2Test::class.java.getResource(fileName).readText()
        val mutableList = content.trim().split(",").map{it.toInt()}.toMutableList()
        mutableList[1] = noun
        mutableList[2] = verb
        return mutableList.joinToString(",")

    }

}
