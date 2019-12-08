package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay8Test {
    @Test
    fun sample() {
        val input = "123456789012"
        val layers = buildLayers(input, 3, 2)
        assertEquals(2, layers.map { it.count { it == '0' } }.size)
    }

    @Test
    fun real() {
        val input = "/day8.txt".loadFromFile()
        val layers = buildLayers(input, 25, 6)
        val computeOnesMultiplyTwos = computeResultPartOne(layers)
        assertEquals(2684, computeOnesMultiplyTwos)
    }

    private fun computeResultPartOne(layers: List<List<Char>>): Int {
        val lowestZero = getLayerWithLowestZeros(layers)
        assertEquals(1, lowestZero!!.size)
        val uniqueCharsList = lowestZero[0]
        return uniqueCharsList.count { it == '1' } * uniqueCharsList.count { it == '2' }
    }

    private fun getLayerWithLowestZeros(layers: List<List<Char>>): List<List<Char>>? {
        val sortedMapOfLayers = layers.groupBy { it.count { it == '0' } }.toSortedMap()
        return sortedMapOfLayers[sortedMapOfLayers.firstKey()]
    }

    @Test
    fun samplePartTwo() {
        val input = "0222112222120000"
        val width = 2
        val height = 2
        val layers = buildLayers(input, width, height)
        printLayers(layers, width, height)
    }

    @Test
    fun realPartTwo() {
        val input = "/day8.txt".loadFromFile()
        val width = 25
        val height = 6
        val layers = buildLayers(input, width, height)
        printLayers(layers, width, height)
    }

    private fun printLayers(layers: List<List<Char>>, width: Int, height: Int) {
        for(h in 0 until height) {
            for(w in 0 until width) {
                val index = width*h+w
                val message = layers.map { it[index] }.first { it != '2' }
                val toPrint = if(message == '0') ' ' else '#'
                print(toPrint)
            }
            println()
        }

    }

    private fun buildLayers(input: String, width : Int, height: Int): List<List<Char>> {
        val immutableInput = input.trim().toCharArray().toList()
        return immutableInput.chunked(width*height)
    }










}
