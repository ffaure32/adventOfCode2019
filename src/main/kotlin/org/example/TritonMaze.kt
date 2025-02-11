package org.example

import java.util.*

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

fun buildTetraMaze(input: String): TritonMaze {
    val maze = mutableMapOf<Position, Char>()
    var y = 0
    var maxX = 0
    input.split("\n").forEach {
        for (x in 0 until it.length) {
            val value = it[x]
            val position = Position(x, y)
            if(value != '#') maze.put(position, value)
            maxX = it.length
        }
        y++
    }
    return TetraMaze(maze, Position(maxX/2, y/2))
}

enum class Square() {
  TOP_LEFT, TOP_RIGHT, DOWN_LEFT, DOWN_RIGHT;
    fun isInSquare(position: Position, middle : Position) : Boolean {
        return getSquare(position, middle) == this
    }
}

fun getSquare(position: Position, middle : Position) : Square {
    return when {
        position.x < middle.x && position.y < middle.y -> {
            Square.TOP_LEFT
        }

        position.x >= middle.x && position.y < middle.y -> {
            Square.TOP_RIGHT
        }

        position.x < middle.x && position.y >= middle.y -> {
            Square.DOWN_LEFT
        }

        else -> {
            Square.DOWN_RIGHT
        }
    }
}
class TetraMaze(maze: MutableMap<Position, Char>, val middle : Position) : TritonMaze(maze) {
    override fun getCurrentPosition(destination : Position): Position {
        val targetSquare = getSquare(destination, middle)
        return maze.filter { it.value == currentPositionChar }.keys.first { getSquare(it, middle) == targetSquare }
    }
    override fun newMaze(chars : Map<Position, Char>) : TritonMaze {
        return TetraMaze(chars.toMutableMap(), middle)
    }

}

open class TritonMaze constructor (maze: Map<Position, Char>) : Maze(maze.toMutableMap(), '@') {
    val keys = mutableMapOf<Char, Position>()
    val doors = mutableMapOf<Char, Position>()

    init {
        keys.putAll(maze.entries.filter { it.value.isLowerCase() }.associate { (k,v)-> v to k })
        doors.putAll(maze.entries.filter { it.value.isUpperCase() }.associate { (k,v)-> v to k })
    }

    fun findAllKeys(): Set<Char> {
        return keys.keys
    }


    fun findKeyPosition(c : Char): Position {
        return keys[c]!!
    }

    fun findDoorPosition(c : Char): Position? {
        return doors[c.uppercaseChar()]
    }


    fun findAccessibleKeys(): Set<Pair<Char, Int>> {
        val visited = mutableSetOf<Position>()
        val q = LinkedList<QueueNode>()
        q.addAll(maze.filter { it.value == currentPositionChar }.keys.map { QueueNode(it, 1) })
        val accessibleKeys = mutableSetOf<Pair<Char, Int>>()
        while(q.isNotEmpty()) {
            val curr = q.remove()
            val neighbours = getNeighbours(curr.position).filter { maze.containsKey(it) && !visited.contains(it) }
            accessibleKeys.addAll(neighbours.filter { maze[it]?.isLowerCase() == true }.map{ Pair(maze[it]!!,  curr.distance)})
            q.addAll( neighbours.filter { isFreePath(maze[it]) }.map {
                visited.add(it)
                QueueNode(it, curr.distance + 1) }
            )
        }
        return accessibleKeys
    }

    fun openDoor(char : Char) : TritonMaze {
        val newMaze = maze.toMutableMap()
        val position = findKeyPosition(char)
        newMaze[getCurrentPosition(position)] = freePathChar
        newMaze[position] = currentPositionChar
        val doorPosition = findDoorPosition(char)
        if(doorPosition != null) {
            newMaze[doorPosition] = freePathChar
        }
        return newMaze(newMaze)
    }

    open fun newMaze(chars : Map<Position, Char>) : TritonMaze {
        return TritonMaze(chars)
    }
}

