package org.example

import java.util.*

class PaintingRobotScreen(val input : Queue<Long>) :
    IntCodeInteraction {
    val robotPath = mutableMapOf<Position, Int>()
    private var currentPosition: Position = Position(0, 0)
    private var currentDirection: Direction =
        Direction.NORTH
    val output = mutableListOf<Long>()


    fun executeInstruction(paint : Int, turn : Int) :Int {
        robotPath.put(currentPosition, paint)
        currentDirection = currentDirection.turn(turn)
        currentPosition = forward()
        return computeOutput()
    }

    private fun computeOutput(): Int {
        return robotPath.getOrDefault(currentPosition, 0)
    }

    private fun forward(): Position {
        return when(currentDirection) {
            Direction.NORTH -> Position(
                currentPosition.x,
                currentPosition.y + 1
            )
            Direction.WEST -> Position(
                currentPosition.x - 1,
                currentPosition.y
            )
            Direction.SOUTH -> Position(
                currentPosition.x,
                currentPosition.y - 1
            )
            Direction.EAST -> Position(
                currentPosition.x + 1,
                currentPosition.y
            )
        }
    }

    override fun printScreen() {
        val minx = robotPath.keys.map{it.x}.min()!!
        val maxx = robotPath.keys.map{it.x}.max()!!
        val miny = robotPath.keys.map{it.y}.min()!!
        val maxy = robotPath.keys.map{it.y}.max()!!

        for(y in maxy downTo miny) {
            for(x in minx .. maxx) {
                val paint= robotPath[Position(x, y)]?.or(0)
                val color = if(paint == 0) '.' else '#'
                print(color)
            }
            println()
        }



    }

    override fun resultPart1(): Any {
        return output.map { it.toString()}.joinToString(",")
    }

    override fun specificStore(input: Queue<Long>) {
        // NOTHING TO DO
    }

    override fun specificOutput(droidResult: Long) {
        if(output.size == 2) {
            val newInput = executeInstruction(output[0].toInt(), output[1].toInt())
            input.add(newInput.toLong())
            output.clear()
        }
    }
}

enum class Direction {
    NORTH, WEST, SOUTH, EAST;
    fun turn(orientation : Int) : Direction {
        val values = values()
        var ordinal: Int
        // turn left
        if(orientation == 0) {
            ordinal = this.ordinal+1
            if(ordinal == values.size) {
                ordinal = 0
            }
        } else {
            ordinal = this.ordinal-1
            if(ordinal < 0) {
                ordinal = values.size-1
            }
        }
        return values[ordinal]
    }
}