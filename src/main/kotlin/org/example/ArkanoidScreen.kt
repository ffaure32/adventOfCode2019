package org.example

import java.util.*

class ArkanoidScreen() : IntCodeInteraction {
    val successPath = mutableListOf<Long>()
    val output = mutableListOf<Long>()
    val screenInfo = mutableMapOf<LongPosition, Long>()
    var currentScore = 0L
    fun draw(x : Long, y : Long, draw : Long) {
        if(x == -1L && y == 0L) {
            currentScore = draw
        } else {
            screenInfo.put(LongPosition(x, y), draw)
        }
    }

    fun countTiles(l: Long): Int {
        return screenInfo.count{it.value == l}
    }

    override fun printScreen() {
        val maxX : Long = screenInfo.keys.map{it.x}.max()!!
        val maxY = screenInfo.keys.map{ it.y }.max()!!
        for(y in 0..maxY) {
            for(x in 0..maxX) {
                val draw = screenInfo.get(LongPosition(x, y))!!
                val toPrint = when(draw) {
                    1L -> '#'
                    2L -> 'B'
                    3L -> '='
                    4L -> 'O'
                    else -> ' '
                }
                print(toPrint)
            }
            println()
        }
    }

    override fun resultPart1(): Any {
        return countTiles(2L)
    }

    override fun resultPart2(): Any {
        return currentScore
    }

    override fun specificStore(input: Queue<Long>) {
        printScreen()
        if(input.size == 0) {
            joystickInteraction(input)
        }
    }

    private fun joystickInteraction(input: Queue<Long>) {
        val reader = Scanner(System.`in`)
        println("enter new command")
        val userInput = reader.next()
        var c = 's'
        if(userInput != null && userInput.length>0) {
            c = userInput[0]
        }
        val joystick = if (c == 'q') {
            -1L
        } else if (c == 'd') {
            1L
        } else {
            0L
        }
        input.add(joystick)
        successPath.add(joystick)
    }

    override fun specificOutput(droidResult: Long) {
        output.add(droidResult)
        if(output.size == 3) {
            draw(output[0], output[1], output[2])
            output.clear()
        }
    }
}