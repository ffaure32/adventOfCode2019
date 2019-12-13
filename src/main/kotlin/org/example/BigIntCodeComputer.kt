package org.example

import java.util.*

data class LongPosition(val x: Long, val y : Long)
enum class Direction {
    NORTH, WEST, SOUTH, EAST;
    fun turn(orientation : Int) : Direction {
        val values = Direction.values()
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

class Screen() {
    val screenInfo = mutableMapOf<LongPosition, Long>()
    var currentScore = 0L
    fun draw(x : Long, y : Long, draw : Long) {
        if(x == -1L && y == 0L) {
            currentScore += draw
        } else {
            screenInfo.put(LongPosition(x, y), draw)
        }
    }

    fun countTiles(l: Long): Int {
        return screenInfo.count{it.value == l}
    }

    fun printScreen() {
        val maxX : Long = screenInfo.keys.map{it.x}.max()!!
        val maxY = screenInfo.keys.map{ it.y }.max()!!
        for(y in 0..maxY) {
            for(x in 0..maxX) {
                val draw = screenInfo.get(LongPosition(x, y))!!
                val toPrint = when(draw) {
                    1L -> '#'
                    2L -> 'B'
                    3L -> '-'
                    4L -> 'o'
                    else -> ' '
                }
                print(toPrint)
            }
            println()
        }
    }
}

class Panel() {
    val robotPath = mutableMapOf<Position, Int>()
    private var currentPosition: Position = Position(0, 0)
    private var currentDirection: Direction = Direction.NORTH

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
            Direction.NORTH -> Position(currentPosition.x, currentPosition.y + 1)
            Direction.WEST -> Position(currentPosition.x - 1, currentPosition.y)
            Direction.SOUTH -> Position(currentPosition.x, currentPosition.y - 1)
            Direction.EAST -> Position(currentPosition.x + 1, currentPosition.y)
        }
    }

    fun printMessage() {
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
}

data class Position(val x : Int, val y : Int)
public class BigIntCodeComputer(val inputs: MutableList<Long>, val input: Queue<Long>) {
    var position: Int = 0
    var relativeBase: Int = 0
    val output = mutableListOf<Long>()
    // val panel = Panel()
    val screen = Screen()

    fun applyInstructionAtPosition() : Boolean {
        val instructions = org.example.buildInstructions(inputs[position].toString())
        val inputList = InputListLong(inputs, instructions.parameterModes, position, relativeBase)
        when (instructions.operation) {
            org.example.OptCode.ADD -> {
                val left = inputList.getValue(0)
                val right = inputList.getValue(1)
                val outputPosition = computeOutputPosition(inputList, 2)
                inputs[outputPosition] = left + right
            }
            org.example.OptCode.MULT -> {
                val left = inputList.getValue(0)
                val right = inputList.getValue(1)
                val outputPosition = computeOutputPosition(inputList, 2)
                inputs[outputPosition] = left * right
            }
            org.example.OptCode.STORE -> {
                screen.printScreen()

                val reader = Scanner(System.`in`)
                println("enter new command")
                val userInput = reader.next()!![0]
                val joystick = if(userInput == 'q') {-1L} else if(userInput == 'd') {1L} else {0L}
                input.add(joystick)
                when(inputList.parameters[0]) {
                    org.example.ParameterMode.POSITION -> {
                        inputs[inputs[position+1].toInt()]= input.remove()!!
                    }
                    org.example.ParameterMode.IMMEDIATE -> {
                        inputs[position+1] = input.remove()!!
                    }
                    org.example.ParameterMode.RELATIVE -> {
                        inputs[inputs[position+1].toInt()+relativeBase] = input.remove()!!
                    }
                }
            }
            org.example.OptCode.OUTPUT -> {
                val outputPosition = computeOutputPosition(inputList, 0)
                output.add(inputs[outputPosition])
                if(output.size == 3) {
                    //val newInput = panel.executeInstruction(output[0].toInt(), output[1].toInt())
                    // input.add(newInput.toLong())
                    //output.clear()
                    screen.draw(output[0], output[1], output[2])
                    output.clear()
                }
            }
            org.example.OptCode.JUMP_IF_TRUE -> {
                val left = inputList.getValue(0)
                if (left != 0L) {
                    position = inputList.getValue(1).toInt()
                    return false
                }
            }
            org.example.OptCode.JUMP_IF_FALSE -> {
                val left = inputList.getValue(0)
                if (left == 0L) {
                    position = inputList.getValue(1).toInt()
                    return false
                }
            }
            org.example.OptCode.LESS_THAN -> {
                val left = inputList.getValue(0)
                val right = inputList.getValue(1)
                val outputPosition = computeOutputPosition(inputList, 2)
                inputs[outputPosition] = if (left < right) 1 else 0
            }
            org.example.OptCode.EQUALS -> {
                val left = inputList.getValue(0)
                val right = inputList.getValue(1)
                val outputPosition = computeOutputPosition(inputList, 2)
                inputs[outputPosition] = if (left == right) 1 else 0
            }
            org.example.OptCode.ADJUST_REL_BASE -> {
                when(inputList.parameters[0]) {
                    org.example.ParameterMode.POSITION -> {
                        relativeBase += inputs[inputs[position+1].toInt()].toInt()
                    }
                    org.example.ParameterMode.IMMEDIATE -> {
                        relativeBase += inputs[position+1].toInt()
                    }
                    org.example.ParameterMode.RELATIVE -> {
                        relativeBase += inputs[inputs[position+1].toInt()+relativeBase].toInt()
                    }
                }
            }
            else -> {
                return true
            }
        }
        position += instructions.operation.instructionSize
        return false
    }

    private fun computeOutputPosition(inputList: InputListLong, index : Int): Int {
        val indexPos = position + index + 1
        val outputPosition = when (inputList.parameters[index]) {
            org.example.ParameterMode.RELATIVE -> inputs[indexPos].toInt() + relativeBase
            org.example.ParameterMode.POSITION -> inputs[indexPos].toInt()
            org.example.ParameterMode.IMMEDIATE -> indexPos
        }
        extendMemory(inputs, outputPosition)
        return outputPosition
    }
}

fun initLongQueue(vararg input : Long) : Queue<Long> {
    val queue = LinkedList<Long>()
    input.forEach { queue.add(it) }
    return queue
}

class InputListLong(
    private val inputs: MutableList<Long>,
    val parameters: List<org.example.ParameterMode>,
    private val position: Int,
    private val relativeBase: Int
) {

    fun getValue(index: Int): Long {
        val parameterMode = parameters[index]
        val indexOrValue = inputs[position + index + 1]
        return if (parameterMode == org.example.ParameterMode.IMMEDIATE) {
            indexOrValue
        } else if (parameterMode == org.example.ParameterMode.POSITION) {
            getOrComplete(indexOrValue)
        } else {
            getOrComplete(indexOrValue + relativeBase)
        }
    }

    private fun getOrComplete(indexOrValue: Long): Long {
        extendMemory(inputs, indexOrValue.toInt())
        return inputs[indexOrValue.toInt()]
    }

}

fun extendMemory(inputs: MutableList<Long>, outputPosition: Int) {
    val toComplete = outputPosition-inputs.size
    if(toComplete>=0) {
        for(i in 0..toComplete) {
            inputs.add(0L)
        }
    }
}