package org.example

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

class TritonMaze constructor (maze: MutableMap<Position, Char>) : Maze(maze, '@') {


    val keys = mutableMapOf<Char, Position>()
    val doors = mutableMapOf<Char, Position>()

    init {
        keys.putAll(maze.entries.filter { it.value.isLowerCase() }.associate { (k,v)-> v to k })
        doors.putAll(maze.entries.filter { it.value.isUpperCase() }.associate { (k,v)-> v to k })
        currentPosition = maze.filter { it.value == currentPositionChar }.keys.first()!!
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
        newMaze[findKeyPosition(currentPositionChar)] = freePathChar
        newMaze[position] = currentPositionChar
        if(hasDoor(char)) {
            newMaze[findKeyPosition(char.toUpperCase())] = freePathChar
        }
        return TritonMaze(newMaze)
    }

    override fun isFreePath(char : Char?): Boolean {
        return super.isFreePath(char) || char != null && char.isLowerCase()
    }
}

