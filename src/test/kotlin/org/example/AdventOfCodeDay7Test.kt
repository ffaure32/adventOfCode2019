package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.test.Ignore
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay7Test {
    @Test
    fun real() {
        val input = "/day7.txt".loadFromFile()
        assertEquals(43812, calculateThrustersFromAmplifiers(input))
    }

    @Test
    fun sample() {
        assertEquals(43210, calculateThrustersFromAmplifiers("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0"))
        assertEquals(54321, calculateThrustersFromAmplifiers("3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0"))
        assertEquals(65210, calculateThrustersFromAmplifiers("3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0"))
    }

    @Test
    fun samplePart2() {
        assertEquals(139629729, calculateThrustersFromAmplifiersWithFeedbackLoop("3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5"))
        //assertEquals(54321, calculateThrustersFromAmplifiers("3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0"))
        //assertEquals(65210, calculateThrustersFromAmplifiers("3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0"))
    }


    private fun calculateThrustersFromAmplifiersWithFeedbackLoop(input: String): Int {
        val mutableInput = input.trim().split(",").map { it.toInt() }.toMutableList()
        //val permutations = generatePermutationsList(setOf(5, 6, 7, 8, 9))
        val permutations = mutableListOf<List<Int>>()
        permutations.add(listOf(9,8,7,6,5))
        var totalInputs = mutableSetOf<Int>()

        permutations.forEach {
            val ampli1 = IntCodeComputer(mutableInput, initQueue(it[0], 0))
            ampli1.applyInstructionsWithBlockingInput()
            val ampli2 = IntCodeComputer(mutableInput, initQueue(it[1], ampli1.output[0]))
            ampli2.applyInstructionsWithBlockingInput()
            val ampli3 = IntCodeComputer(mutableInput, initQueue(it[2], ampli2.output[0]))
            ampli3.applyInstructionsWithBlockingInput()
            val ampli4 = IntCodeComputer(mutableInput, initQueue(it[3], ampli3.output[0]))
            ampli4.applyInstructionsWithBlockingInput()
            val ampli5 = IntCodeComputer(mutableInput, initQueue(it[4], ampli4.output[0]))
            ampli5.applyInstructionsWithBlockingInput()
            var exit : Boolean = false
            while(!exit) {
                ampli1.applyInstructionsWithBlockingInput(ampli5.output[0])
                exit = (ampli1.status == Status.EXIT)
                ampli2.applyInstructionsWithBlockingInput(ampli1.output[0])
                exit = (ampli2.status == Status.EXIT)
                ampli3.applyInstructionsWithBlockingInput(ampli2.output[0])
                exit = (ampli3.status == Status.EXIT)
                ampli4.applyInstructionsWithBlockingInput(ampli3.output[0])
                exit = (ampli4.status == Status.EXIT)
                ampli5.applyInstructionsWithBlockingInput(ampli4.output[0])
                exit = (ampli5.status == Status.EXIT)

            }
        }
        return 12
    }

    private fun calculateThrustersFromAmplifiers(input: String): Int {
        val mutableInput = input.trim().split(",").map { it.toInt() }.toMutableList()
        val permutations = generatePermutationsList(setOf(0, 1, 2, 3, 4))
        var totalInputs = mutableSetOf<Int>()
        permutations.forEach {
            val ampli1 = IntCodeComputer(mutableInput, initQueue(it[0], 0))
            ampli1.applyInstructionsWithInput()
            val ampli2 = IntCodeComputer(mutableInput, initQueue(it[1], ampli1.output[0]))
            ampli2.applyInstructionsWithInput()
            val ampli3 = IntCodeComputer(mutableInput, initQueue(it[2], ampli2.output[0]))
            ampli3.applyInstructionsWithInput()
            val ampli4 = IntCodeComputer(mutableInput, initQueue(it[3], ampli3.output[0]))
            ampli4.applyInstructionsWithInput()
            val ampli5 = IntCodeComputer(mutableInput, initQueue(it[4], ampli4.output[0]))
            ampli5.applyInstructionsWithInput()
            totalInputs.add(ampli5.output[0])
        }
        return (totalInputs.sorted().last())
    }

    private fun getAmplifierOutput(input : MutableList<Int>, queue : Queue<Int>) : Int {
        applyInstructionsWithInput(input, queue)
        return queue.remove()!!
    }

    private fun applyInstructionsWithInput(inputInts: MutableList<Int>, firstInput: Queue<Int>): MutableList<Int> {
        var instructionPointer = 0
        var instructionSize = -1
        while (instructionSize != 0) {
            val newInstructionPointer = applyInstructionAtPosition(inputInts, instructionPointer, firstInput)
            instructionSize = newInstructionPointer - instructionPointer
            instructionPointer = newInstructionPointer
        }
        return inputInts
    }

    @Test
    fun print1234() {
        print(generatePermutationsList(setOf(0,1,2,3,4)))
    }

    fun generatePermutationsList(initNumbers: Set<Int>): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        generatePermutations(initNumbers.size, initNumbers.toMutableList(), result)
        return result.toList()
    }
    fun generatePermutations(n: Int, elements: MutableList<Int>, result :MutableList<List<Int>>) {
        if (n == 1) {
            result.add(elements.toList())
        } else {
            for (i in 0 until n - 1) {
                generatePermutations(n - 1, elements, result)
                if (n % 2 == 0) {
                    swap(elements, i, n - 1)
                } else {
                    swap(elements, 0, n - 1)
                }
            }
            generatePermutations(n - 1, elements, result)
        }
    }

    private fun swap(elements: MutableList<Int>, a: Int, b: Int) {
        val tmp = elements[a]
        elements[a] = elements[b]
        elements[b] = tmp
    }
}
