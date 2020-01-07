package org.example

import java.util.*

class DroidScreen() : IntCodeInteraction {
    val currentPositionChar = 'D'
    var currentPosition = Position(0, 0)
    val screenInfo = mutableMapOf<Position, Char>(currentPosition to currentPositionChar)
    val maze = Maze(screenInfo, currentPositionChar)
    var movementCommand : Int = 1
    val oxygenSystem = mutableListOf<Position>()

    override fun printScreen() {
        println(maze.toString())
    }

    override fun resultPart1(): Any {
        return "c'est fini"
    }

    fun movementCommand(droidCommand: Int) {
        movementCommand = droidCommand
    }

    fun realMove(statusCode: Int) {
        val target = currentPosition.move(movementCommand)
        when(statusCode) {
            1 -> {
                if(screenInfo [currentPosition] != 'O') screenInfo [currentPosition] = '.'
                currentPosition = target
                if(screenInfo [target] != 'O') screenInfo [target] = 'D'
                attempt = 0
                lastMove = movementCommand
            }
            2 ->  {
                if(screenInfo [currentPosition] != 'O') screenInfo [currentPosition] = '.'
                currentPosition = target
                screenInfo [target] = 'O'
                oxygenSystem.add(target)
                attempt = 0
                lastMove = movementCommand
            }
            0 ->  {
                screenInfo [target] = '#'
            }
        }
    }

    override fun specificStore(input : Queue<Long>) {
        if(input.size == 0) {
            droidInteraction(input)
        }

    }
    var lastMove = 1
    var attempt = 1
    val northCommands = listOf<Int>(3, 1, 4, 2)
    val southCommands = listOf<Int>(4, 2, 3, 1)
    val eastCommands = listOf<Int>(1, 4, 2, 3)
    val westCommands = listOf<Int>(2, 3, 1, 4)

    private fun droidInteraction(input : Queue<Long>) {
        val droidCommand = when(lastMove) {
            1 -> northCommands[attempt]
            2 -> southCommands[attempt]
            3 -> westCommands[attempt]
            4 -> eastCommands[attempt]
            else -> 0
        }
        attempt++
        input.add(droidCommand.toLong())
        movementCommand(droidCommand)
    }

    override fun specificOutput(droidResult: Long) {
        realMove(droidResult.toInt())
        //printScreen()
    }
}