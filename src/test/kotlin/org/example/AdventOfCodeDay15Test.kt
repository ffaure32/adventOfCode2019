package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay15Test {

    @Test
    fun real() {
        val input = "/day15.txt".loadFromFile()
        val longQueue = initLongQueue(1)
        assertEquals(363, playArkanoid(input, longQueue).screen.countTiles(2L))
    }
}
