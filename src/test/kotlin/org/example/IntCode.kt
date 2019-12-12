package org.example

import java.util.*

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

class Panel() {
    val robotPath = mutableMapOf<Position, Int>()
    private var currentPosition: Position = Position(0,0)
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
            Direction.NORTH -> Position(currentPosition.x, currentPosition.y+1)
            Direction.WEST -> Position(currentPosition.x-1, currentPosition.y)
            Direction.SOUTH -> Position(currentPosition.x, currentPosition.y-1)
            Direction.EAST -> Position(currentPosition.x+1, currentPosition.y)
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

class BigIntCodeComputer(val inputs: MutableList<Long>, val input: Queue<Long>) {
    var position: Int = 0
    var relativeBase: Int = 0
    val output = mutableListOf<Long>()
    val panel = Panel()

    fun applyInstructionAtPosition() : Boolean {
        val instructions = buildInstructions(inputs[position].toString())
        val inputList = InputListLong(inputs, instructions.parameterModes, position, relativeBase)
        println(inputs[position].toString())
        when (instructions.operation) {
            OptCode.ADD -> {
                val left = inputList.getValue(0)
                val right = inputList.getValue(1)
                val outputPosition = computeOutputPosition(inputList, 2)
                inputs[outputPosition] = left + right
            }
            OptCode.MULT -> {
                val left = inputList.getValue(0)
                val right = inputList.getValue(1)
                val outputPosition = computeOutputPosition(inputList, 2)
                inputs[outputPosition] = left * right
            }
            OptCode.STORE -> {
                when(inputList.parameters[0]) {
                    ParameterMode.POSITION -> {
                        inputs[inputs[position+1].toInt()]= input.remove()!!
                    }
                    ParameterMode.IMMEDIATE -> {
                        inputs[position+1] = input.remove()!!
                    }
                    ParameterMode.RELATIVE -> {
                        inputs[inputs[position+1].toInt()+relativeBase] = input.remove()!!
                    }
                }
            }
            OptCode.OUTPUT -> {
                val outputPosition = computeOutputPosition(inputList, 0)
                output.add(inputs[outputPosition])
                if(output.size == 2) {
                    val newInput = panel.executeInstruction(output[0].toInt(), output[1].toInt())
                    input.add(newInput.toLong())
                    output.clear()
                }
            }
            OptCode.JUMP_IF_TRUE -> {
                val left = inputList.getValue(0)
                if (left != 0L) {
                    position = inputList.getValue(1).toInt()
                    return false
                }
            }
            OptCode.JUMP_IF_FALSE -> {
                val left = inputList.getValue(0)
                if (left == 0L) {
                    position = inputList.getValue(1).toInt()
                    return false
                }
            }
            OptCode.LESS_THAN -> {
                val left = inputList.getValue(0)
                val right = inputList.getValue(1)
                val outputPosition = computeOutputPosition(inputList, 2)
                inputs[outputPosition] = if (left < right) 1 else 0
            }
            OptCode.EQUALS -> {
                val left = inputList.getValue(0)
                val right = inputList.getValue(1)
                val outputPosition = computeOutputPosition(inputList, 2)
                inputs[outputPosition] = if (left == right) 1 else 0
            }
            OptCode.ADJUST_REL_BASE -> {
                when(inputList.parameters[0]) {
                    ParameterMode.POSITION -> {
                        relativeBase += inputs[inputs[position+1].toInt()].toInt()
                    }
                    ParameterMode.IMMEDIATE -> {
                        relativeBase += inputs[position+1].toInt()
                    }
                    ParameterMode.RELATIVE -> {
                        relativeBase += inputs[inputs[position+1].toInt()+relativeBase].toInt()
                    }
                }
            }
            else -> {
                print(panel.printMessage())
                return true
            }
        }
        position += instructions.operation.instructionSize
        return false
    }

    private fun computeOutputPosition(inputList: InputListLong, index : Int): Int {
        val indexPos = position + index + 1
        val outputPosition = when (inputList.parameters[index]) {
            ParameterMode.RELATIVE -> inputs[indexPos].toInt() + relativeBase
            ParameterMode.POSITION -> inputs[indexPos].toInt()
            ParameterMode.IMMEDIATE -> indexPos
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
    val parameters: List<ParameterMode>,
    private val position: Int,
    private val relativeBase: Int
) {

    fun getValue(index: Int): Long {
        val parameterMode = parameters[index]
        val indexOrValue = inputs[position + index + 1]
        return if (parameterMode == ParameterMode.IMMEDIATE) {
            indexOrValue
        } else if (parameterMode == ParameterMode.POSITION) {
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

