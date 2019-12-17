package org.example

import java.util.*

class DroidScreen() : IntCodeInteraction {
    var currentPosition = LongPosition(0, 0)
    val screenInfo = mutableMapOf<LongPosition, Char>(currentPosition to 'D')
    var movementCommand : Long = 1
    val oxygenSystem = mutableListOf<LongPosition>()
    override fun printScreen() {
        val minX : Long = screenInfo.keys.map{it.x}.min()!!
        val minY = screenInfo.keys.map{ it.y }.min()!!
        val maxX : Long = screenInfo.keys.map{it.x}.max()!!
        val maxY = screenInfo.keys.map{ it.y }.max()!!
        for(y in maxY  downTo minY) {
            for(x in minX .. maxX) {
                val draw = screenInfo.getOrDefault(LongPosition(x, y), ' ')
                print(draw)
            }
            println()
        }
    }

    override fun resultPart1(): Any {
        return "c'est fini"
    }

    fun movementCommand(droidCommand: Long) {
        movementCommand = droidCommand
    }

    fun realMove(statusCode: Long) {
        val target = currentPosition.move(movementCommand)
        when(statusCode) {
            1L -> {
                screenInfo [currentPosition] = '.'
                currentPosition = target
                screenInfo [target] = 'D'
            }
            2L ->  {
                screenInfo [currentPosition] = '.'
                currentPosition = target
                screenInfo [target] = 'O'
                oxygenSystem.add(target)
            }
            0L ->  {
                screenInfo [target] = '#'
            }
        }
    }

    override fun specificStore(input : Queue<Long>) {
        if(input.size == 0) {
            droidInteraction(input)
        }

    }
    private fun droidInteraction(input : Queue<Long>) {
        val reader = Scanner(System.`in`)
        println("enter new command")
        val userInput = reader.next()
        var c = 's'
        if(userInput != null && userInput.isNotEmpty()) {
            c = userInput[0]
        }
        val droidCommand = if (c == 'q') {
            3L
        } else if (c == 'd') {
            4L
        } else if (c == 'z') {
            1L
        } else {
            2L
        }
        input.add(droidCommand)
        movementCommand(droidCommand)
    }

    override fun specificOutput(droidResult: Long) {
        realMove(droidResult)
        printScreen()
    }
}