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
        assertEquals(2, calculateFuelNeededConsideringFuelMass(14))
        assertEquals(966, calculateFuelNeededConsideringFuelMass(1969))
        assertEquals(50346, calculateFuelNeededConsideringFuelMass(100756))
    }

    @Test
    fun `real with file part 2`() {
        assertEquals(4739374, calculateFuelNeededFromFilePart2("/day1.txt"))
    }

    private fun calculateFuelNeededConsideringFuelMass(moduleMass: Int): Int {
        var total = 0
        val subtotal = Math.max(0, calculateFuelNeeded(moduleMass))
        total += subtotal
        return if(subtotal == 0) total else total + calculateFuelNeededConsideringFuelMass(subtotal)
    }

    private fun calculateFuelNeededFromFilePart1(fileName: String): Int {
        return calculateFuelNeededFromFile(fileName, ::calculateFuelNeeded)
    }

    private fun calculateFuelNeededFromFilePart2(fileName: String): Int {
        return calculateFuelNeededFromFile(fileName, ::calculateFuelNeededConsideringFuelMass)
    }

    private fun calculateFuelNeededFromFile(fileName: String, calculateFuelFunction: (Int) -> Int): Int {
        val content = AdventOfCodeDay1Test::class.java.getResource(fileName).readText()
        return content.lines().map { it.toInt() }.fold(0) { tot, it -> tot + calculateFuelFunction.invoke(it) }
    }

    private fun calculateFuelNeeded(mass: Int): Int {
        return mass / 3 - 2
    }
}
