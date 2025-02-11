import kotlin.math.abs

data class Coords2D(val x: Int, val y: Int) : Node {
    fun valid(maxX : Int, maxY : Int, minX :Int = 0, minY : Int = 0) : Boolean {
        return x >= minX && x <= maxX && y>= minY && y <= maxY
    }
    fun inMatrix(max : Int) : Boolean {
        return valid(max, max)
    }

    fun distance(other : Coords2D) : Int {
        val diff = this - other
        return abs(diff.x)+abs(diff.y)
    }

    fun coords3D(level: Int) : Coords3D {
        return Coords3D(x, y, level)
    }
}

data class Coords3D(val x: Int, val y: Int, val z: Int) : Node {
    fun coords2D() : Coords2D {
        return Coords2D(x, y)
    }
}

operator fun Coords2D.plus(other: Coords2D) = Coords2D(x + other.x, y + other.y)
operator fun Coords2D.minus(other: Coords2D) = Coords2D(x - other.x, y - other.y)
operator fun Coords2D.times(other: Coords2D) = Coords2D(x * other.x, y * other.y)

enum class Directions {
    TOP_LEFT {
        override fun next(actual: Coords2D): Coords2D {
            return Coords2D(actual.x-1, actual.y-1)
        }
    },
    UP {
        override fun next(actual: Coords2D): Coords2D {
            return Coords2D(actual.x, actual.y-1)
        }
        override fun move(actual: Coords2D): Coords2D {
            return Coords2D(actual.x, actual.y-1)
        }
        override fun turnRight(): Directions {
            return RIGHT
        }
        override fun turnLeft(): Directions {
            return LEFT
        }
    },
    TOP_RIGHT {
        override fun next(actual: Coords2D): Coords2D {
            return Coords2D(actual.x+1, actual.y-1)
        }
    },
    RIGHT {
        override fun next(actual: Coords2D): Coords2D {
            return Coords2D(actual.x+1, actual.y)
        }
        override fun move(actual: Coords2D): Coords2D {
            return Coords2D(actual.x+1, actual.y)
        }
        override fun turnRight(): Directions {
            return DOWN
        }
        override fun turnLeft(): Directions {
            return UP
        }
    },
    DOWN_RIGHT {
        override fun next(actual: Coords2D): Coords2D {
            return Coords2D(actual.x+1, actual.y+1)
        }
    },
    DOWN {
        override fun next(actual: Coords2D): Coords2D {
            return Coords2D(actual.x, actual.y+1)
        }
        override fun move(actual: Coords2D): Coords2D {
            return Coords2D(actual.x, actual.y+1)
        }
        override fun turnRight(): Directions {
            return LEFT
        }
        override fun turnLeft(): Directions {
            return RIGHT
        }
    },
    DOWN_LEFT {
        override fun next(actual: Coords2D): Coords2D {
            return Coords2D(actual.x-1, actual.y+1)
        }
    },
    LEFT {
        override fun next(actual: Coords2D): Coords2D {
            return Coords2D(actual.x-1, actual.y)
        }
        override fun move(actual: Coords2D): Coords2D {
            return Coords2D(actual.x-1, actual.y)
        }
        override fun turnRight(): Directions {
            return UP
        }
        override fun turnLeft(): Directions {
            return DOWN
        }
    };
    abstract fun next(actual : Coords2D) : Coords2D

    open fun move(actual : Coords2D) : Coords2D {
        return actual
    }

    open fun turnRight() : Directions {
        return this
    }

    open fun turnLeft() : Directions {
        return this
    }

}

val SQUARED_DIRECTIONS = listOf(Directions.UP, Directions.RIGHT, Directions.DOWN, Directions.LEFT)
val HORIZONTAL_DIRECTIONS = listOf(Directions.LEFT, Directions.RIGHT)
val VERTICAL_DIRECTIONS = listOf(Directions.UP, Directions.DOWN)

data class TileNode(val coords : Coords2D, val direction : Directions) : Node {
    fun neighbour(): TileNode {
        return TileNode(direction.next(coords), direction)
    }
    fun turns(): List<TileNode> {
        return listOf(TileNode(coords, direction.turnRight()), TileNode(coords, direction.turnLeft()))
    }

    fun leftNeighbour() : TileNode {
        return TileNode(coords, direction.turnLeft()).neighbour()
    }

    fun rightNeighbour() : TileNode {
        return TileNode(coords, direction.turnRight()).neighbour()
    }
}