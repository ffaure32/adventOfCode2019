package org.example

import java.util.*

enum class ParameterMode(val code: Int) {
    POSITION(0),
    IMMEDIATE(1),
    RELATIVE(2);
}

enum class OptCode(val code: Int, val instructionSize: Int) {
    ADD(1, 4),
    MULT(2, 4),
    STORE(3, 2),
    OUTPUT(4, 2),
    JUMP_IF_TRUE(5, 3),
    JUMP_IF_FALSE(6, 3),
    LESS_THAN(7, 4),
    EQUALS(8, 4),
    ADJUST_REL_BASE(9, 2),
    EXIT(99, 0)
}

data class Instruction(val operation: OptCode, val parameterModes: List<ParameterMode>)

enum class Status {
    WAITING,
    RUNNING,
    EXIT
}
class IntCodeComputer(val inputs: MutableList<Int>, val input: Queue<Int>) {
    val output = mutableListOf<Int>()
    var position = 0
    var status = Status.RUNNING
    fun applyInstructionsWithInput() {
        do {
            applyInstructionAtPosition()
        } while (status == Status.RUNNING)
    }

    fun applyInstructionsWithBlockingInput(vararg newInputs : Int) {
        newInputs.forEach { input.add(it) }
        var exit: Boolean
        do {
            exit = applyInstructionAtPosition()
        } while (status == Status.RUNNING)
    }

    fun applyInstructionAtPosition() : Boolean {
        val instructions = buildInstructions(inputs[position].toString())
        val inputList = InputList(inputs, instructions.parameterModes, position)
        when (instructions.operation) {
            OptCode.ADD -> {
                status = Status.RUNNING
                val left = inputList.getValue(0)
                val right = inputList.getValue(1)
                val outputPosition = inputList.getOutputValue(2)
                inputs[outputPosition] = left + right
            }
            OptCode.MULT -> {
                status = Status.RUNNING
                val left = inputList.getValue(0)
                val right = inputList.getValue(1)
                val outputPosition = inputList.getOutputValue(2)
                inputs[outputPosition] = left * right
            }
            OptCode.STORE -> {
                status = Status.RUNNING
                val outputPosition = inputList.getOutputValue(0)
                inputs[outputPosition] = input.remove()!!
            }
            OptCode.OUTPUT -> {
                val outputPosition = inputList.getOutputValue(0)
                output.add(inputs[outputPosition])
                println(output)
                status = Status.WAITING
            }
            OptCode.JUMP_IF_TRUE -> {
                status = Status.RUNNING
                val left = inputList.getValue(0)
                if (left != 0) {
                    position = inputList.getValue(1)
                    return false
                }
            }
            OptCode.JUMP_IF_FALSE -> {
                status = Status.RUNNING
                val left = inputList.getValue(0)
                if (left == 0) {
                    position = inputList.getValue(1)
                    return false
                }
            }
            OptCode.LESS_THAN -> {
                status = Status.RUNNING
                val left = inputList.getValue(0)
                val right = inputList.getValue(1)
                val outputPosition = inputList.getOutputValue(2)
                inputs[outputPosition] = if (left < right) 1 else 0
            }
            OptCode.EQUALS -> {
                status = Status.RUNNING
                val left = inputList.getValue(0)
                val right = inputList.getValue(1)
                val outputPosition = inputList.getOutputValue(2)
                inputs[outputPosition] = if (left == right) 1 else 0
            }
            else -> {
                status = Status.EXIT
                return true
            }
        }
        position += instructions.operation.instructionSize
        return false
    }
}

fun getParameterMode(code: Int): ParameterMode {
    return ParameterMode.values().find { code == it.code }!!
}

fun getOptCode(code: Int): OptCode {
    return OptCode.values().find { code == it.code }!!
}

fun applyInstructionAtPosition(inputInts: MutableList<Int>, position: Int, input: Queue<Int>): Int {
    val instructions = buildInstructions(inputInts[position].toString())
    val inputList = InputList(inputInts, instructions.parameterModes, position)
    when (instructions.operation) {
        OptCode.ADD -> {
            val left = inputList.getValue(0)
            val right = inputList.getValue(1)
            val outputPosition = inputList.getOutputValue(2)
            inputInts[outputPosition] = left + right
        }
        OptCode.MULT -> {
            val left = inputList.getValue(0)
            val right = inputList.getValue(1)
            val outputPosition = inputList.getOutputValue(2)
            inputInts[outputPosition] = left * right
        }
        OptCode.STORE -> {
            val outputPosition = inputList.getOutputValue(0)
            inputInts[outputPosition] = input.remove()!!
        }
        OptCode.OUTPUT -> {
            val outputPosition = inputList.getOutputValue(0)
            input.add(inputInts[outputPosition])
        }
        OptCode.JUMP_IF_TRUE -> {
            val left = inputList.getValue(0)
            if (left != 0) {
                return inputList.getValue(1)
            }
        }
        OptCode.JUMP_IF_FALSE -> {
            val left = inputList.getValue(0)
            if (left == 0) {
                return inputList.getValue(1)
            }
        }
        OptCode.LESS_THAN -> {
            val left = inputList.getValue(0)
            val right = inputList.getValue(1)
            val outputPosition = inputList.getOutputValue(2)
            inputInts[outputPosition] = if (left < right) 1 else 0
        }
        OptCode.EQUALS -> {
            val left = inputList.getValue(0)
            val right = inputList.getValue(1)
            val outputPosition = inputList.getOutputValue(2)
            inputInts[outputPosition] = if (left == right) 1 else 0
        }
        else -> {}
    }
    return position + instructions.operation.instructionSize
}
fun buildInstructions(instructionInput: String): Instruction {
    val opcode = if (instructionInput.length > 1) instructionInput.takeLast(2) else instructionInput.takeLast(1)
    val code = getOptCode(opcode.toInt())
    val instruction = instructionInput.padStart(code.instructionSize + 1, '0').toCharArray()
    val parameters = mutableListOf<ParameterMode>()
    for (i in 0..instruction.size - 3) {
        parameters.add(getParameterMode(instruction[instruction.size - 3 - i].toString().toInt()))
    }
    return Instruction(code, parameters)
}

class InputList(
    private val inputInts: MutableList<Int>,
    private val parameters: List<ParameterMode>,
    private val position: Int
) {
    fun getOutputValue(index: Int): Int {
        return getRealValue(parameters[index], position + index + 1)
    }

    fun getValue(index: Int): Int {
        return getRealValue(parameters[index], inputInts[position + index + 1])
    }

    private fun getRealValue(parameterMode: ParameterMode, indexOrValue: Int): Int {
        return if (parameterMode == ParameterMode.IMMEDIATE) {
            indexOrValue
        } else {
            inputInts[indexOrValue]
        }
    }
}

fun initQueue(vararg input : Int) : Queue<Int> {
    val queue = LinkedList<Int>()
    input.forEach { queue.add(it) }
    return queue
}
