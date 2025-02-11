class Day1918 {
    private val input = readInput(18)

    fun part2(): Int {
        val triton = parseInput()

        return triton.shortestPath()
    }

    private fun parseInput(): TritonMaze {
        val keys = mutableMapOf<Coords2D, Char>()
        val doors = mutableMapOf<Char, Coords2D>()
        val topLeftPath = mutableSetOf<Coords2D>()
        val topRightPath = mutableSetOf<Coords2D>()
        val downLeftPath = mutableSetOf<Coords2D>()
        val downRightPath = mutableSetOf<Coords2D>()
        var topLeftCursor : Coords2D? = null
        var topRightCursor : Coords2D? = null
        var downLeftCursor : Coords2D? = null
        var downRightCursor : Coords2D? = null
        val middleX = input[0].length/2
        val middleY = input.size/2
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val coords = Coords2D(x, y)
                when {
                    c.isUpperCase() ->  {
                        doors[c] = coords
                    }
                    c == '.' || c == '@' || c.isLowerCase() -> {
                        if(c.isLowerCase()) { keys[coords] = c }
                        when {
                            x < middleX && y < middleY -> {
                                topLeftPath.add(coords)
                                if(c == '@') topLeftCursor = coords
                            }

                            x >= middleX &&  y < middleY -> {
                                topRightPath.add(coords)
                                if(c == '@') topRightCursor = coords
                            }

                            x<middleX &&  y >= middleY -> {
                                downLeftPath.add(coords)
                                if(c == '@') downLeftCursor = coords
                            }

                            else -> {
                                downRightPath.add(coords)
                                if(c == '@') downRightCursor = coords
                            }
                        }
                    }
                }
            }
        }
        val vaults = listOf( Vault(topLeftCursor!!, topLeftPath),
                            Vault(topRightCursor!!, topRightPath),
                            Vault(downLeftCursor!!, downLeftPath),
                            Vault(downRightCursor!!, downRightPath))
        return TritonMaze(vaults, keys, doors, middleX, middleY)
    }
}


class Vault(var currentPosition : Coords2D, val passages : MutableSet<Coords2D>, var totalSteps : Int = 0) {

    fun moveToNextAvailableKey(keys: Set<Coords2D>): Pair<Coords2D, Int>? {
        var step = 1
        var currentCoords = listOf(listOf(currentPosition))
        while(currentCoords.isNotEmpty()) {
            currentCoords = currentCoords.map { p ->
                val neighboursList = SQUARED_DIRECTIONS.map {
                    it.move(p.last())
                }.filter { (passages.contains(it) && !p.contains(it))}
                    .map { p+it }
                neighboursList
            }.flatten()
            val lastSteps = currentCoords.map { it.last() }
            val foundKey = lastSteps.firstOrNull { keys.contains(it) }
            if(foundKey != null) {
                totalSteps += step
                currentPosition = foundKey
                return Pair(foundKey, step)
            } else {
                step++
            }
        }
        return null
    }

}

class TritonMaze(val passages: List<Vault>, val keys: MutableMap<Coords2D, Char>, val doors: MutableMap<Char, Coords2D>, val middleX: Int, val middleY: Int) {

    fun shortestPath(): Int {
        while (keys.isNotEmpty()) {
            passages.forEach {
                val foundKey = it.moveToNextAvailableKey(keys.keys)
                if(foundKey != null) {
                    val key = keys.remove(foundKey.first)!!
                    openDoor(key)
                }
            }
        }
        return passages.sumOf { it.totalSteps }
    }

    private fun openDoor(key: Char) {
        val doorCoords = doors[key.uppercaseChar()]
        if(doorCoords != null) {
            val x = doorCoords.x
            val y = doorCoords.y
            when {
                x < middleX && y < middleY -> {
                    passages[0].passages.add(doorCoords)
                }

                x >= middleX && y < middleY -> {
                    passages[1].passages.add(doorCoords)
                }

                x < middleX && y >= middleY -> {
                    passages[2].passages.add(doorCoords)
                }

                else -> {
                    passages[3].passages.add(doorCoords)
                }
            }
        }
    }
}