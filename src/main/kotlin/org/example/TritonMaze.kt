package org.example

import java.util.LinkedList

fun buildTritonMaze(input: String): TritonMaze {
    val maze = mutableMapOf<Position, Char>()
    var y = 0
    input.split("\n").forEach {
        for (x in 0 until it.length) {
            val value = it[x]
            val position = Position(x, y)
            if(value != '#') maze.put(position, value)
        }
        y++
    }
    return TritonMaze(maze)
}

class TritonMaze constructor (maze: MutableMap<Position, Char>) : Maze(maze, '@') {


    val keys = mutableMapOf<Char, Position>()
    val doors = mutableMapOf<Char, Position>()
    val initPosition = getCurrentPosition()

    init {
        keys.putAll(maze.entries.filter { it.value.isLowerCase() }.associate { (k,v)-> v to k })
        doors.putAll(maze.entries.filter { it.value.isUpperCase() }.associate { (k,v)-> v to k })
    }

    fun findAccessibleKeys(): Set<Char> {
        return keys.keys
    }

    fun findKeyPosition(c : Char): Position {
        return keys[c]!!
    }

    fun findDoorPosition(c : Char): Position? {
        return doors[c.toUpperCase()]
    }

    fun hasDoor(findAccessibleKey: Char): Boolean {
        return maze.values.contains(findAccessibleKey.toUpperCase())
    }

    fun openDoor(char : Char) : TritonMaze {
        val newMaze = maze.toMutableMap()
        val position = findKeyPosition(char)
        newMaze[initPosition] = freePathChar
        newMaze[position] = currentPositionChar
        val doorPosition = findDoorPosition(char)
        if(doorPosition != null) {
            newMaze[doorPosition] = freePathChar
        }
        return TritonMaze(newMaze)
    }

/*    override fun isFreePath(char : Char?): Boolean {
        return super.isFreePath(char) || char != null && char.isLowerCase()
    }*/
}

