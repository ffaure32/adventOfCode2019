package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay13Test {

    @Test
    fun real() {
        val input = "/day13.txt".loadFromFile()
        val longQueue = initLongQueue()
        assertEquals(363, playArkanoid(input, longQueue).screen.countTiles(2L))
    }

    @Test
    fun realPart2() {
        val input = "/day13part2.txt".loadFromFile()
        val winnerMoves = "/winnerMoves".loadFromFile()
        val inputJoystick = winnerMoves.lines()[0].trim().split(",").map { it.toLong() }
        val longQueue = initLongQueue()
        longQueue.addAll(inputJoystick)
        assertEquals(17159, playArkanoid(input, longQueue).screen.currentScore)
    }
}
