package org.example

class TritonMaze(input: String) {
    fun findAccessibleKeys(): Set<Char> {
        return findAccessiblePositions().map { maze[it]!! }.filter { it.isLowerCase() }.toSet()
    }

    fun findAccessiblePositions(): Set<Position> {
        val alreadyVisited = mutableSetOf<Position>()
        return accessiblePositions(currentPosition, alreadyVisited)
    }

    fun accessiblePositions(position: Position, alreadyVisited: MutableSet<Position>): MutableSet<Position> {
        val result = mutableSetOf<Position>()
        val neighbours = getNeighbours(position).minus(alreadyVisited)
        alreadyVisited.addAll(neighbours)
        val currentFilter = neighbours.filter {
            val char = maze.getOrDefault(it, '#')
            char.isLowerCase() || char == '.'
        }.toSet()
        result.addAll(currentFilter)
        currentFilter.forEach {
            result.addAll(accessiblePositions(it, alreadyVisited))
        }
        return result
    }

    private fun getNeighbours(position: Position): Set<Position> {
        val neighbours = mutableSetOf<Position>()
        neighbours.add(Position(position.x, position.y + 1))
        neighbours.add(Position(position.x, position.y - 1))
        neighbours.add(Position(position.x + 1, position.y))
        neighbours.add(Position(position.x - 1, position.y))
        return neighbours
    }

    val maze = mutableMapOf<Position, Char>()
    val keys = mutableMapOf<Char, Position>()
    val doors = mutableMapOf<Char, Position>()
    var currentPosition: Position

    init {
        var y = 0
        input.split("\n").forEach {
            for (x in 0 until it.length) {
                val value = it[x]
                val position = Position(x, y)
                maze.put(position, value)
                if (value.isLowerCase()) keys.put(value, position)
                else if (value.isUpperCase()) doors.put(value, position)
            }
            y++
        }
        currentPosition = maze.filter { it.value == '@' }.keys.first()!!
    }
}

