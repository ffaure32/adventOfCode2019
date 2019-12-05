package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.math.abs
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay3Test {
    data class Coords(val x: Int, val y: Int) {
        internal fun move(direction: Char) : Coords {
            return when (direction) {
                'R' -> Coords(x + 1, y)
                'U' -> Coords(x, y + 1)
                'L' -> Coords(x - 1, y)
                'D' -> Coords(x, y - 1)
                else -> throw Exception("unexpected direction")
            }
        }
    }

    class Line(private var currentPosition: Coords, var path: MutableList<Coords>) {
        private fun move(instruction: String) {
            val direction = instruction.get(0)
            val length = instruction.drop(1).toInt() - 1
            realMove(direction, length)
        }

        private fun realMove(direction : Char, length: Int) {
            var lastCoords: Coords = currentPosition
            for (i in 0..length) {
                lastCoords = lastCoords.move(direction)
                path.add(lastCoords)
            }
            currentPosition = lastCoords

        }

        fun moves(line: String) {
            line.split(",").forEach { this.move(it) }
        }
    }

    data class Moves(val firstLineMoves: String, val secondLineMoves: String)

    private val START: Coords = Coords(0, 0)
    private lateinit var firstLine: Line
    private lateinit var secondLine: Line
    private lateinit var moves : Moves

    @BeforeTest
    fun init() {
        firstLine = Line(START, mutableListOf(START))
        secondLine = Line(START, mutableListOf(START))
    }

    @Test
    fun `first sample part1`() {
        moves = Moves("R8,U5,L5,D3", "U7,R6,D4,L4")
        assertEquals(6, getManhattanDistance())
    }

    @Test
    fun `real data part1`() {
        loadMovesFromFile()
        assertEquals(529, getManhattanDistance())
    }

    @Test
    fun `first sample part 2`() {
        moves = Moves("R8,U5,L5,D3", "U7,R6,D4,L4")
        val index = calculateStepsToFirstIntersection()
        assertEquals(30, index)
    }

    @Test
    fun `second sample part 2`() {
        moves = Moves("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")
        val index = calculateStepsToFirstIntersection()
        assertEquals(610, index)
    }

    @Test
    fun `real file part 2`() {
        loadMovesFromFile()
        val index = calculateStepsToFirstIntersection()
        assertEquals(20386, index)
    }

    private fun calculateStepsToFirstIntersection(): Int {
        val intersect = getIntersection()
        var index = intersect.map {firstLine.path.indexOf(it)}.sorted()[1]
        index += secondLine.path.indexOf(firstLine.path[index])
        return index
    }

    private fun getManhattanDistance(): Int {
        val intersection = getIntersection()
        return (intersection.map { abs(it.x) + abs(it.y) }.sorted()[1])
    }

    private fun getIntersection(): Set<Coords> {
        firstLine.moves(moves.firstLineMoves)
        secondLine.moves(moves.secondLineMoves)
        return firstLine.path.intersect(secondLine.path)

    }

    private fun loadMovesFromFile() {
        val content = "/day3.txt".loadFromFile()
        moves = Moves(content.lines()[0].trim(), content.lines()[1].trim())
    }
}
