package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay18Test {

    @Test
    fun sample() {
        val input = """
            #########
            #b.A.@.a#
            #########
        """.trimIndent()
        val tritonMaze = TritonMaze(input)

        assertEquals(2, tritonMaze.keys.size)
        assertEquals(1, tritonMaze.doors.size)
        assertEquals(27, tritonMaze.maze.size)
        assertEquals(3, tritonMaze.findAccessiblePositions().size)
        assertEquals(1, tritonMaze.findAccessibleKeys().size)
    }

    @Test
    fun real() {
        val input = "/day18.txt".loadFromFile()
        val tritonMaze = TritonMaze(input)

        assertEquals(1580, tritonMaze.findAccessiblePositions().size)
        assertEquals(10, tritonMaze.findAccessibleKeys().size)
    }
}
