package org.example

enum class ParameterMode(val code: Int) {
    POSITION(0),
    IMMEDIATE(1);
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
    EXIT(99, 0)
}

data class Instruction(val operation: OptCode, val parameterModes: List<ParameterMode>)

fun getParameterMode(code: Int): ParameterMode {
    return ParameterMode.values().find { code == it.code }!!
}

fun getOptCode(code: Int): OptCode {
    return OptCode.values().find { code == it.code }!!
}

fun applyInstructionAtPosition(inputInts: MutableList<Int>, position: Int, input: Int): Int {
    val instructions = buildInstructions(inputInts[position].toString())
    when (instructions.operation) {
        OptCode.ADD -> {
            val left = getRealValue(inputInts, instructions.parameterModes[0], inputInts[position + 1])
            val right = getRealValue(inputInts, instructions.parameterModes[1], inputInts[position + 2])
            val outputPosition = getRealValue(inputInts, instructions.parameterModes[2], position + 3)
            inputInts[outputPosition] = left + right
        }
        OptCode.MULT -> {
            val left = getRealValue(inputInts, instructions.parameterModes[0], inputInts[position + 1])
            val right = getRealValue(inputInts, instructions.parameterModes[1], inputInts[position + 2])
            val outputPosition = getRealValue(inputInts, instructions.parameterModes[2], position + 3)
            inputInts[outputPosition] = left * right
        }
        OptCode.STORE -> {
            val outputPosition = getRealValue(inputInts, instructions.parameterModes[0], position + 1)
            inputInts[outputPosition] = input
        }
        OptCode.OUTPUT -> {
            val outputPosition = getRealValue(inputInts, instructions.parameterModes[0], position + 1)
            println(inputInts[outputPosition])
        }
        OptCode.JUMP_IF_TRUE -> {
            val left = getRealValue(inputInts, instructions.parameterModes[0], inputInts[position + 1])
            if (left != 0) {
                return getRealValue(inputInts, instructions.parameterModes[1], inputInts[position + 2])
            }
        }
        OptCode.JUMP_IF_FALSE -> {
            val left = getRealValue(inputInts, instructions.parameterModes[0], inputInts[position + 1])
            if (left == 0) {
                return getRealValue(inputInts, instructions.parameterModes[1], inputInts[position + 2])
            }
        }
        OptCode.LESS_THAN -> {
            val left = getRealValue(inputInts, instructions.parameterModes[0], inputInts[position + 1])
            val right = getRealValue(inputInts, instructions.parameterModes[1], inputInts[position + 2])
            val outputPosition = getRealValue(inputInts, instructions.parameterModes[2], position + 3)
            inputInts[outputPosition] = if (left < right) 1 else 0
        }
        OptCode.EQUALS -> {
            val left = getRealValue(inputInts, instructions.parameterModes[0], inputInts[position + 1])
            val right = getRealValue(inputInts, instructions.parameterModes[1], inputInts[position + 2])
            val outputPosition = getRealValue(inputInts, instructions.parameterModes[2], position + 3)
            inputInts[outputPosition] = if (left == right) 1 else 0
        }

    }
    return position + instructions.operation.instructionSize
}

fun getRealValue(inputInts: MutableList<Int>, parameterMode: ParameterMode, indexOrValue: Int): Int {
    return if (parameterMode == ParameterMode.IMMEDIATE) {
        indexOrValue
    } else {
        inputInts[indexOrValue]
    }

}

fun buildInstructions(instructionInput: String): Instruction {
    val opcode = if (instructionInput.length > 1) instructionInput.takeLast(2) else instructionInput.takeLast(1)
    val code = getOptCode(String(opcode.toCharArray()).toInt())
    val instruction = instructionInput.padStart(code.instructionSize + 1, '0').toCharArray()
    val parameters = mutableListOf<ParameterMode>()
    for (i in 0..instruction.size - 3) {
        parameters.add(getParameterMode(instruction[instruction.size - 3 - i].toString().toInt()))
    }
    return Instruction(code, parameters)


}

fun applyToIndex(inputInts: MutableList<Int>, position: Int, input: Int): Int {
    val opcode = getOptCode(inputInts[position])
    when (opcode) {
        OptCode.ADD -> {
            val left = inputInts[position + 1]
            val right = inputInts[position + 2]
            val outputPosition = inputInts[position + 3]
            inputInts[outputPosition] = inputInts[left] + inputInts[right]
        }
        OptCode.MULT -> {
            val left = inputInts[position + 1]
            val right = inputInts[position + 2]
            val outputPosition = inputInts[position + 3]
            inputInts[outputPosition] = inputInts[left] * inputInts[right]
        }
        OptCode.STORE -> {
            val outputPosition = inputInts[position + 1]
            inputInts[outputPosition] = input
        }
        OptCode.OUTPUT -> {
            val value = inputInts[position + 1]
            // TODO output
            return inputInts[position + 2]
        }
    }
    return 0

}


