package org.example

import org.junit.Ignore
import org.junit.Test
import org.junit.jupiter.api.TestInstance
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
    @Ignore
    fun real() {
        val input = "/day18.txt".loadFromFile()
        val tritonMaze = buildTritonMaze(input)
        assertEquals(81, shortestPath(tritonMaze))
    }
    val finalPathes = mutableSetOf<Long>()
    val pathCounts= mutableSetOf<Path>()

    fun shortestPath(maze : TritonMaze): Long {
        val path = Path()
        computeDistance(maze, path)
        return finalPathes.min() ?: 0

    }
    fun computeDistance(maze: TritonMaze, path : Path) {
        val findAccessibleKeys = maze.findAccessibleKeys()
        for (findAccessibleKey in findAccessibleKeys) {
            val newSet = path.chars.toMutableList()
            newSet.add(findAccessibleKey)
            val count = path.length
            val shortestPath = maze.findShortestPath(maze.findKeyPosition(findAccessibleKey))
            val totalPath = count + shortestPath
            val newPath =  Path(newSet, totalPath)
            if(pathExists(newPath)) {
                return
            }
            if(totalPath >= 0 && totalPath < finalPathes.min() ?: totalPath+1 && totalPath < count) {
                pathCounts.add(newPath)
                if (findAccessibleKeys.size <= 1) {
                    finalPathes.add(totalPath)
                    println("min path:" + finalPathes.min())
                }
                if (shortestPath >= 0) {
                    val openDoor = maze.openDoor(findAccessibleKey)
                    computeDistance(openDoor, newPath)
                }
            }
        }

    }

    fun pathExists(path : Path) : Boolean {
        return pathCounts.find { it.chars.toSet() == path.chars.toSet() && it.chars.last() == path.chars.last() && it.length <= path.length } != null
    }
    class Path(val chars : MutableList<Char> = mutableListOf(), var length : Long = 0L) {

    }
}
