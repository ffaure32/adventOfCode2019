import kotlin.math.pow

class Day24 {
    private val input = readInput(24)

    fun part1(): Long {
        val gol = GameOfLife(parseInput())
        gol.live()
        return gol.biodiversityRating()
    }

    fun part2(): Int {
        val gol = GameOfLife3D(parseInput3D())
        gol.live(200)
        return gol.countBugs()
    }

    private fun parseInput(): Map<Coords2D, Boolean> {
        val layout = mutableMapOf<Coords2D, Boolean>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val isBug = c == '#'
                layout[Coords2D(x, y)] = isBug
            }
        }
        return layout.toMap()
    }

    private fun parseInput3D(): Map<Coords3D, Boolean> {
        val layout = mutableMapOf<Coords3D, Boolean>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val isBug = c == '#'
                layout[Coords3D(x, y,0)] = isBug
                for(i in 1..101) {
                    layout[Coords3D(x, y, i)] = false
                    layout[Coords3D(x, y, -i)] = false
                }
            }
        }
        return layout.filter { it.key.x !=2 || it.key.y != 2 }.toMap()
    }
}

class GameOfLife3D(var layout : Map<Coords3D, Boolean>) {
    val length = layout.maxOf { it.key.x }+1

    fun live(time : Int) {
        for(i in 1..time) {
            layout = step()
        }
    }

    private fun step() = layout.map {
        val neighbours = findNeighbours(it.key)
        val infectedNeighbours  = neighbours.count { layout.getOrDefault(it, false) }
        val newState = if (it.value) {
            infectedNeighbours == 1
        } else {
            infectedNeighbours == 1 || infectedNeighbours == 2
        }
        it.key to newState
    }.toMap()

    private fun findNeighbours(cell: Coords3D): Set<Coords3D> {
        val coords2D = cell.coords2D()

        return SQUARED_DIRECTIONS.flatMap {
            val next = it.move(coords2D)
            when {
                next.x == 2 && next.y == 2 -> {
                    when(it) {
                        Directions.UP -> IntRange(0, 4).map { Coords3D(it, 4, cell.z+1) }
                        Directions.RIGHT -> IntRange(0, 4).map { Coords3D(0, it, cell.z+1) }
                        Directions.DOWN -> IntRange(0, 4).map { Coords3D(it, 0, cell.z+1) }
                        Directions.LEFT -> IntRange(0, 4).map { Coords3D(4, it, cell.z+1) }
                        else -> error("unexpectedDirection")
                    }.toSet()
                }
                next.x<0 -> {
                    setOf(Coords3D(1, 2, cell.z-1))
                }
                next.y<0 -> {
                    setOf(Coords3D(2, 1, cell.z-1))
                }
                next.x>4 -> {
                    setOf(Coords3D(3, 2, cell.z-1))
                }
                next.y>4 -> {
                    setOf(Coords3D(2, 3, cell.z-1))
                }
                else -> {
                    setOf(Coords3D(next.x, next.y, cell.z))
                }
            }
        }.toSet()
    }

    fun infested(): Set<Coords3D> {
        return layout.filter { it.value }.map { it.key }.toSet()
    }


    fun countBugs(): Int {
        return infested().size
    }

}

class GameOfLife(var layout : Map<Coords2D, Boolean>) {
    val history = mutableSetOf(infested(layout))
    val length = layout.maxOf { it.key.x }+1

    fun live() {
        var newState = true
        while(newState) {
            layout = step()
            newState = history.add(infested(layout))
        }
    }

    private fun step() = layout.map {
        val neighbours = SQUARED_DIRECTIONS.map { sd -> sd.move(it.key) }.count { layout.getOrDefault(it, false) }
        val newState = if (it.value) {
            neighbours == 1
        } else {
            neighbours == 1 || neighbours == 2
        }
        it.key to newState
    }.toMap()

    fun infested(currentLayout : Map<Coords2D, Boolean>): Set<Coords2D> {
        return layout.filter { it.value }.map { it.key }.toSet()
    }

    fun biodiversityRating() : Long {
        return layout.filter { it.value }.map{ it.key}.map { 2.toDouble().pow(it.x+it.y*length).toLong() }.sum()
    }
}

