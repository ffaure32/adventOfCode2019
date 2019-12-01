package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay1Test {
    @Test
    fun `samples`() {
        assertEquals(2, calculateFuelNeeded(12))
        assertEquals(2, calculateFuelNeeded(14))
        assertEquals(654, calculateFuelNeeded(1969))
        assertEquals(33583, calculateFuelNeeded(100756))
    }

    @Test
    fun `samples with file`() {
        assertEquals(34241, calculateFuelNeededFromFilePart1("/day1Sample.txt"))
    }

    @Test
    fun `real with file`() {
        assertEquals(3161483, calculateFuelNeededFromFilePart1("/day1.txt"))
    }

    @Test
    fun `samples part 2`() {
        assertEquals(2, calculateFuelNeededPart2(14))
        assertEquals(966, calculateFuelNeededPart2(1969))
        assertEquals(50346, calculateFuelNeededPart2(100756))
    }

    @Test
    fun `real with file part 2`() {
        assertEquals(4739374, calculateFuelNeededFromFilePart2("/day1.txt"))
    }

    private fun calculateFuelNeededPart2(i: Int): Int {

        var total = 0
        var subtotal = i
        do {
            subtotal = calculateFuelNeeded(subtotal)
            total += Math.max(0, subtotal)
        } while (subtotal > 0)
        return total
    }

    private fun calculateFuelNeededFromFilePart1(fileName: String): Int {
        return calculateFuelNeededFromFile(fileName, ::calculateFuelNeeded)
    }

    private fun calculateFuelNeededFromFilePart2(fileName: String): Int {
        return calculateFuelNeededFromFile(fileName, ::calculateFuelNeededPart2)
    }

    private fun calculateFuelNeededFromFile(fileName: String, function:(Int)->Int): Int {
        val content = AdventOfCodeDay1Test::class.java.getResource(fileName).readText()
        var total = 0
        content.lines().forEach {
            total += function.invoke(it.toInt())
        }
        return total
        // return content.lines().map { it.toInt() }.reduce { tot, it -> tot+function.invoke(it)}
    }

    private fun calculateFuelNeeded(mass: Int): Int {
        return mass / 3 - 2
    }
}
