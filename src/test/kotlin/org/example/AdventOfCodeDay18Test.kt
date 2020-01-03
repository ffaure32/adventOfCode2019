package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay18Test {

    @Test
    fun sample() {
        val input = """
########################
#...............b.C.D.f#
#.######################
#.....@.a.B.c.d.A.e.F.g#
########################
        """.trimIndent()
        val tritonMaze = buildTritonMaze(input)
        computeDistance(tritonMaze, 0L)
    }

    @Test
    fun real() {
        val input = "/day18.txt".loadFromFile()
        val tritonMaze = buildTritonMaze(input)
        computeDistance(tritonMaze, 0L)
    }

    val pathes = mutableSetOf<Long>()
    fun computeDistance(maze: TritonMaze, acc : Long) {
        val findAccessibleKeys = maze.findAccessibleKeys()
        for (findAccessibleKey in findAccessibleKeys) {
            val shortestPath = maze.findShortestPath(maze.currentPosition, maze.findKeyPosition(findAccessibleKey))
            println("size:"+findAccessibleKeys.size+" "+ shortestPath)
            if(findAccessibleKeys.size <= 1) {
                pathes.add(acc+shortestPath)
            }
            if(shortestPath>=0) {
                println(findAccessibleKey+" "+ shortestPath)
                if(maze.hasDoor(findAccessibleKey)) {
                    val openDoor = maze.openDoor(findAccessibleKey)
                    computeDistance(openDoor, acc + shortestPath)
                }
            }
        }
        println("chemin min:"+pathes.min())

    }
}
