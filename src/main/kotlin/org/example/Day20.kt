class Day20 {
    private val input = readInput(20)

    fun part1(): Int {
        val (passages, portals) = parseInput()

        val donutMaze = DonutMaze(passages, portals)
        return donutMaze.shortestPath()
    }

    fun part2(): Int {
        val (passages, portals) = parseInput()

        val donutMaze = DonutMaze(passages, portals)
        return donutMaze.shortestPathWithLevels()
    }

    private fun parseInput(): Pair<MutableSet<Coords2D>, MutableMap<String, MutableList<Coords2D>>> {
        val passages = mutableSetOf<Coords2D>()
        val portals = mutableMapOf<String, MutableList<Coords2D>>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '.') passages.add(Coords2D(x, y))
                else if (isLetter(c)) {
                    val right = getVal(x + 1, y)
                    val down = getVal(x, y + 1)
                    if (isLetter(right)) {
                        val portalKey = "" + c + right
                        val list = portals.getOrPut(portalKey, { mutableListOf() })
                        if (getVal(x + 2, y) == '.') {
                            list.add(Coords2D(x + 2, y))
                        } else {
                            list.add(Coords2D(x - 1, y))
                        }
                    } else if (isLetter(down)) {
                        val portalKey = "" + c + down
                        val list = portals.getOrPut(portalKey, { mutableListOf() })
                        if (getVal(x, y + 2) == '.') {
                            list.add(Coords2D(x, y + 2))
                        } else {
                            list.add(Coords2D(x, y - 1))
                        }

                    }
                }
            }
        }
        return Pair(passages, portals)
    }

    fun getVal(x: Int, y : Int): Char {
        return if(y<input.size && x<input[y].length) {
            input[y][x]
        } else {
            ' '
        }
    }

    fun isLetter(c : Char): Boolean {
        return !listOf('.', '#', ' ').contains(c)
    }

}

class DonutMaze(val passages : Set<Coords2D>, val portals : Map<String, List<Coords2D>>) {
    val minX = passages.minOf { it.x }
    val maxX = passages.maxOf { it.x }
    val minY = passages.minOf { it.y }
    val maxY = passages.maxOf { it.y }
    fun shortestPath(): Int {
        val passageNeighbours = passages.associateWith { p ->
            val neighboursList = SQUARED_DIRECTIONS.map {
                it.move(p)
            }.filter { passages.contains(it) }
                .map { Pair(it, 1) }.toMutableList()
            neighboursList.addAll(portals.entries.filter { e -> e.value.contains(p) }.map { e -> e.value.filter { it != p } }.map { it.map { Pair(it, 1) } }.flatten())
            neighboursList
        } as Map<Node, List<Pair<Node, Int>>>

        val dijkstra = dijkstraWithLoops(passageNeighbours, portals["AA"]!!.first())
        return dijkstra[portals["ZZ"]!!.first()]!!
    }

    fun isInternalRing(c :Coords2D): Boolean {
        return c.x > minX && c.x < maxX && c.y > minY && c.y < maxY
    }

    fun shortestPathWithLevels(depth : Int = 50) : Int {
        val passageWithPortals = mutableMapOf<Node, MutableList<Pair<Node, Int>>>()
        portals.values.flatten().toSet().forEach {
            val out = weightedPaths(it)

            out.forEach { c, w ->

                val others = portals.values.first { it.contains(c) }.filter { it != c }
                if(others.isNotEmpty()) {
                    val other = others.first()
                    for (i in 0..depth) {
                        if(isInternalRing(other)) {
                            val node = it.coords3D(i+1)
                            val target = other.coords3D(i)
                            val orPut = passageWithPortals.getOrPut(node, { mutableListOf() })
                            orPut.add(Pair(target, w+1))
                        } else {
                            val node = it.coords3D(i)
                            val target = other.coords3D(i+1)
                            val orPut = passageWithPortals.getOrPut(node, { mutableListOf() })
                            orPut.add(Pair(target, w+1))
                        }
                    }
                } else {
                    for (i in 0..depth) {
                        val node = it.coords3D(i)
                        val orPut = passageWithPortals.getOrPut(node, { mutableListOf() })
                        orPut.add(Pair(c.coords3D(i), w))
                    }
                }

            }
        }
        val dijkstra = dijkstraWithLoops(passageWithPortals, portals["AA"]!!.first().coords3D(0))
        return dijkstra[portals["ZZ"]!!.first().coords3D(0)]!!
    }

    fun weightedPaths(start : Coords2D): Map<Coords2D, Int> {
        val portalCoords = portals.values.flatten()
        var step = 1
        var currentCoords = listOf(listOf(start))
        val destinations = mutableMapOf<Coords2D, Int>()
        while(currentCoords.isNotEmpty()) {
            currentCoords = currentCoords.map { p ->
                val neighboursList = SQUARED_DIRECTIONS.map {
                    it.move(p.last())
                }.filter { passages.contains(it) && !p.contains(it) }
                    .map { p+it }
                neighboursList
            }.flatten()
            currentCoords.filter { portalCoords.contains(it.last()) }.forEach { destinations[it.last()] = step }
            step++
        }
        return destinations.toMap()
    }

}