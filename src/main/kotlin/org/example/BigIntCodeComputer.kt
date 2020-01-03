package org.example

import java.util.*

data class LongPosition(val x: Long, val y : Long) {
    fun move(direction : Long) : LongPosition {
        return when(direction) {
            1L -> LongPosition(x, y+1)
            2L -> LongPosition(x, y-1)
            3L -> LongPosition(x-1, y)
            4L -> LongPosition(x+1, y)
            else -> LongPosition(x, y)
        }
    }
}

interface IntCodeInteraction {
    fun specificStore(input : Queue<Long>)
    fun specificOutput(droidResult: Long)
    fun printScreen()
    fun resultPart1(): Any
    fun resultPart2(): Any {
        return resultPart1()
    }
}

data class Position(val x : Int, val y : Int)
class BigIntCodeComputer(val inputs: MutableList<Long>, val input: Queue<Long>, val intCodeInteraction : IntCodeInteraction) {
    var position: Int = 0
    var relativeBase: Int = 0

    fun applyInstructionAtPosition() : Boolean {

        val instructions = buildInstructions(inputs[position].toString())
        val inputList = InputListLong(inputs, instructions.parameterModes, position, relativeBase)
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
                intCodeInteraction.specificStore(input)
                when(inputList.parameters[0]) {
                    ParameterMode.POSITION -> {
                        inputs[inputs[position+1].toInt()]= input.remove()!!
                    }
                    ParameterMode.IMMEDIATE -> {
                        inputs[position+1] = input.remove()!!
                    }
                    ParameterMode.RELATIVE -> {
                        val index = inputs[position + 1].toInt() + relativeBase
                        extendMemory(inputs, index)
                        inputs[index] = input.remove()!!
                    }
                }
            }
            OptCode.OUTPUT -> {
                val outputPosition = computeOutputPosition(inputList, 0)
                intCodeInteraction.specificOutput(inputs[outputPosition])
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
                intCodeInteraction.printScreen()
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

    fun result() : Any {
        return intCodeInteraction.resultPart1()
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