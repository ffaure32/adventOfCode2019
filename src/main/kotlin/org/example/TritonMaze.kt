package org.example

import java.util.*

fun buildTritonMaze(input: String): TritonMaze {
    val maze = mutableMapOf<Position, Char>()
    var y = 0
    input.split("\n").forEach {
        for (x in 0 until it.length) {
            val value = it[x]
            val position = Position(x, y)
            maze.put(position, value)
        }
        y++
    }
    return TritonMaze(maze)
}

public class TritonMaze constructor (val maze: Map<Position, Char>) {


    val keys = mutableMapOf<Char, Position>()
    val doors = mutableMapOf<Char, Position>()
    var currentPosition: Position

    init {
        keys.putAll(maze.entries.filter { it.value.isLowerCase() }.associate { (k,v)-> v to k })
        doors.putAll(maze.entries.filter { it.value.isUpperCase() }.associate { (k,v)-> v to k })
        currentPosition = maze.filter { it.value == '@' }.keys.first()!!
    }

    fun findAccessibleKeys(): Set<Char> {
        return maze.entries.filter{ it.value.isLowerCase() }.map { it.value }.toSet()
        // return findAccessiblePositions().map { maze[it]!! }.filter{ it.isLowerCase() }.toSet()
    }
    fun findKeyPosition(c : Char): Position {
        return maze.entries.find { it.value == c }!!.key
    }

    fun hasDoor(findAccessibleKey: Char): Boolean {
        return maze.values.contains(findAccessibleKey.toUpperCase())
    }

    fun openDoor(char : Char) : TritonMaze {
        val newMaze = maze.toMutableMap()
        val position = findKeyPosition(char)
        newMaze[findKeyPosition('@')] = '.'
        newMaze[position] = '@'
        if(hasDoor(char)) {
            newMaze[findKeyPosition(char.toUpperCase())] = '.'
        }
        return TritonMaze(newMaze)
    }

    fun findShortestPath(dest: Position): Int {
        val visited = mutableMapOf<Position, Boolean>()
        val q = LinkedList<QueueNode>()
        val s = QueueNode(currentPosition, 0)
        q.add(s)

        while(q.isNotEmpty()) {
            val curr = q.peek()
            val pos = curr.position
            if(pos == dest) {
                return curr.distance
            }

            q.remove()
            for (neighbour in getNeighbours(pos)) {
                if(maze[neighbour] != null) {
                    if ((maze[neighbour] == '.' || maze[neighbour]!!.isLowerCase())
                        && visited[neighbour] == null
                    ) {
                        visited[neighbour] = true
                        q.add(QueueNode(neighbour, curr.distance + 1))
                    }
                }
            }
        }
        return -1
    }

    override fun toString(): String {
        val builder = StringBuilder()
        val maxx = maze.map { it.key.x }.max()!!
        val maxy = maze.map { it.key.y }.max()!!
        for(y in 0 until maxy) {
            for(x in 0 until maxx) {
                builder.append(maze[Position(x, y)])
            }
            builder.append('\n')
        }
        return builder.toString()
    }



}

data class QueueNode(val position : Position, val distance : Int)

