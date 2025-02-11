package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.math.min
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay18Test {

    @Test
    fun sample1() {
        val input = """
    #########
    #b.A.@.a#
    #########
        """.trimIndent()
        val tritonMaze = buildTritonMaze(input)
        val result = shortestPath(tritonMaze)
        assertEquals(8, result)
    }

    @Test
    fun sample2() {
        val input = """
########################
#f.D.E.e.C.b.A.@.a.B.c.#
######################.#
#d.....................#
########################
        """.trimIndent()
        val tritonMaze = buildTritonMaze(input)
        assertEquals(86, shortestPath(tritonMaze))
    }

    @Test
    fun sample3() {
        val input = """
########################
#...............b.C.D.f#
#.######################
#.....@.a.B.c.d.A.e.F.g#
########################
        """.trimIndent()
        val tritonMaze = buildTritonMaze(input)
        assertEquals(132, shortestPath(tritonMaze))
    }

    @Test
    fun sample4() {
        val input = """
    #################
    #i.G..c...e..H.p#
    ########.########
    #j.A..b...f..D.o#
    ########@########
    #k.E..a...g..B.n#
    ########.########
    #l.F..d...h..C.m#
    #################
        """.trimIndent()
        val tritonMaze = buildTritonMaze(input)
        assertEquals(136, shortestPath(tritonMaze))
    }

    @Test
    fun sample5() {
    val input = """
        ########################
        #@..............ac.GI.b#
        ###d#e#f################
        ###A#B#C################
        ###g#h#i################
        ########################
    """.trimIndent()
        val tritonMaze = buildTritonMaze(input)
        assertEquals(81, shortestPath(tritonMaze))
    }

    @Test
    fun real() {
        val input = "/day18.txt".loadFromFile()
        val tritonMaze = buildTritonMaze(input)
        assertEquals(4406, shortestPath(tritonMaze))
    }

    @Test
    fun realPart2() {
        val input = "/day18Tetra.txt".loadFromFile()
        val tritonMaze = buildTetraMaze(input)
        assertEquals(1964, shortestPath(tritonMaze))
    }

    var shortestPathLength = Long.MAX_VALUE
    val shorterPath = mutableMapOf<PathKey, Long>()
    fun shortestPath(maze : TritonMaze): Long {
        val path = Path()
        computeDistance(maze, maze.findAllKeys().size, path)
        return shortestPathLength
    }

    fun computeDistance(maze: TritonMaze, totalKeyCount : Int, path : Path) {
        val accessibleKeys = maze.findAccessibleKeys()
        for (keyPair in accessibleKeys) {
            val key = keyPair.first
            val newPathLength = path.steps + keyPair.second
            val newPath = Path(path.collectedKeys + key, key, newPathLength)
            if (shorterPathExists(newPath)) {
                continue
            } else {
                shorterPath[PathKey(newPath.collectedKeys, newPath.lastCollectedKey)] = newPathLength
            }
            if (newPathLength < shortestPathLength) {
                if (newPath.collectedKeys.size == totalKeyCount) {
                    shortestPathLength = min(shortestPathLength, newPathLength)
                    println("different paths " + shortestPathLength)

                }
                if (keyPair.second >= 0) {
                    val openDoor = maze.openDoor(key)
                    computeDistance(openDoor, totalKeyCount, newPath)
                }
            }
        }

    }

    fun shorterPathExists(path : Path) : Boolean {
        val shorterPath = shorterPath[PathKey(path.collectedKeys, path.lastCollectedKey)]
        return if(shorterPath != null) {
            shorterPath <=path.steps
        } else {
            false
        }
    }

    data class PathKey(val collectedKeys : Set<Char> = setOf(), val lastCollectedKey : Char)
    data class Path(val collectedKeys : Set<Char> = setOf(), val lastCollectedKey : Char = 'a', val steps : Long = 0L) {

    }
}
