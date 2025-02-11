import java.util.*
import kotlin.concurrent.thread

enum class ParameterMode(val mode: Char) {
    POSITION('0'), IMMEDIATE('1'), RELATIVE('2')
}

fun getParameterMode(parameterMode: Char): ParameterMode {
    return ParameterMode.values().first { it.mode == parameterMode }
}

class OptCode(val instruction: Int, val parameterModes: List<ParameterMode> = listOf()) {
    fun getParameterMode(position: Long): ParameterMode {
        return if (position < parameterModes.size) parameterModes[position.toInt()] else ParameterMode.POSITION
    }
}


open class QueueCollector(val external: Boolean = false) {
    val output = LinkedList<Long>()

    open fun addOutput(value: Long) {
        output.add(value)
    }

    open fun nextOutput(): Long {
        return if (output.isEmpty()) 0L
        else if (external) output.remove()!!
        else output.last()
    }

    fun external(): Boolean {
        return external
    }
}


class IntCode(
    inputs: List<Long>,
    val input: Queue<Long> = LinkedList(),
    val collector: QueueCollector = QueueCollector()
) {
    var count = 0L

    constructor(inputs: List<Long>, uniqueInput: Long) : this(inputs, LinkedList(listOf(uniqueInput)))

    var instructionPointer = 0L
    var exit = false
    var next: IntCode? = null
    var relativeBase = 0
    val memory = initMemory(inputs)

    fun initMemory(inputs: List<Long>): MutableMap<Long, Long> {
        return inputs.mapIndexed { index, input -> index.toLong() to input }.toMap().toMutableMap()
    }

    fun getMemory(position: Long): Long {
        return memory.getOrPut(position, { 0L })
    }

    fun getParameter(optCode: OptCode, position: Long): Long {
        return getMemory(getPosition(optCode, position))
    }

    fun getPosition(optCode: OptCode, position: Long): Long {
        val parameterMode = optCode.getParameterMode(position - 1)
        return when (parameterMode) {
            ParameterMode.POSITION -> getMemory(instructionPointer + position)
            ParameterMode.RELATIVE -> getMemory(instructionPointer + position) + relativeBase
            ParameterMode.IMMEDIATE -> instructionPointer + position
        }
    }

    fun parseOpcode(opcode: Long): OptCode {
        return if (opcode >= 100) {
            val opCodeString = opcode.toString()
            OptCode(
                opCodeString.takeLast(2).toInt(),
                opCodeString.dropLast(2).reversed().map { getParameterMode(it) })
        } else {
            OptCode(opcode.toInt())
        }
    }

    fun run(newInput: Long) {
        input.add(newInput)
        run()
    }

    fun run() {
        while (!exit) {
            count++
            val opcode = getMemory(instructionPointer)
            val optCode = parseOpcode(opcode)
            when (optCode.instruction) {
                1 -> {
                    memory[getPosition(optCode, 3)] = getParameter(optCode, 1) + getParameter(optCode, 2)
                    instructionPointer += 4
                }

                2 -> {
                    memory[getPosition(optCode, 3)] = getParameter(optCode, 1) * getParameter(optCode, 2)
                    instructionPointer += 4
                }

                3 -> {
                    val arg = if (input.isEmpty()) collector.nextOutput() else input.remove()!!
                    memory[getPosition(optCode, 1)] = arg
                    instructionPointer += 2
                }

                4 -> {
                    val value = getParameter(optCode, 1)
                    collector.addOutput(value)
                    instructionPointer += 2
                    if (next != null) {
                        next!!.run(collector.nextOutput())
                    } else if (collector.external()) {
                        return
                    }
                }

                5 -> {
                    if (getParameter(optCode, 1) != 0L) {
                        instructionPointer = getParameter(optCode, 2)
                    } else {
                        instructionPointer += 3
                    }
                }

                6 -> {
                    if (getParameter(optCode, 1) == 0L) {
                        instructionPointer = getParameter(optCode, 2)
                    } else {
                        instructionPointer += 3
                    }
                }

                7 -> {
                    if (getParameter(optCode, 1) < getParameter(optCode, 2)) {
                        memory[getPosition(optCode, 3)] = 1
                    } else {
                        memory[getPosition(optCode, 3)] = 0
                    }
                    instructionPointer += 4
                }

                8 -> {
                    if (getParameter(optCode, 1) == getParameter(optCode, 2)) {
                        memory[getPosition(optCode, 3)] = 1
                    } else {
                        memory[getPosition(optCode, 3)] = 0
                    }
                    instructionPointer += 4
                }

                9 -> {
                    relativeBase += getParameter(optCode, 1).toInt()
                    instructionPointer += 2
                }

                99 -> exit = true
                else -> error("Unexpected code:"+optCode.instruction)
            }

        }
    }

    fun output(): Long {
        return collector.nextOutput()
    }

}

class AmplifierController(val phaseSettingSequence: List<Long>, val inputs: List<Long>) {
    fun run(): Long {
        val ampA = Amplifier(phaseSettingSequence[0], inputs)
        ampA.run(0)
        val ampB = Amplifier(phaseSettingSequence[1], inputs)
        ampB.run(ampA.output())
        val ampC = Amplifier(phaseSettingSequence[2], inputs)
        ampC.run(ampB.output())
        val ampD = Amplifier(phaseSettingSequence[3], inputs)
        ampD.run(ampC.output())
        val ampE = Amplifier(phaseSettingSequence[4], inputs)
        ampE.run(ampD.output())
        return ampE.output()
    }

    fun runWithLoop(): Long {
        val intCodeA = IntCode(inputs.toMutableList(), phaseSettingSequence[0])
        val intCodeB = IntCode(inputs.toMutableList(), phaseSettingSequence[1])
        val intCodeC = IntCode(inputs.toMutableList(), phaseSettingSequence[2])
        val intCodeD = IntCode(inputs.toMutableList(), phaseSettingSequence[3])
        val intCodeE = IntCode(inputs.toMutableList(), phaseSettingSequence[4])
        intCodeA.next = intCodeB
        intCodeB.next = intCodeC
        intCodeC.next = intCodeD
        intCodeD.next = intCodeE
        intCodeE.next = intCodeA

        intCodeA.run(0)
        return intCodeA.input.remove()!!
    }


}

class Thruster(val inputs: List<Long>) {
    fun maxPhaseSetting(): Long {
        val permutations = generatePermutationsList(setOf(0, 1, 2, 3, 4))
        return permutations.map {
            val amplifierController = AmplifierController(it, inputs)
            amplifierController.run()
        }.max()
    }

    fun maxPhaseSettingWithLoop(): Long {
        val permutations = generatePermutationsList(setOf(5, 6, 7, 8, 9))
        return permutations.maxOf {
            val amplifierController = AmplifierController(it, inputs)
            amplifierController.runWithLoop()
        }
    }

}

class Amplifier(phaseSetting: Long, inputs: List<Long>) {
    val intCode = IntCode(inputs.toMutableList(), phaseSetting)
    fun run(newInput: Long) {
        intCode.run(newInput)
    }

    fun output(): Long {
        return intCode.output()
    }

}

class SpaceJail(inputs: List<Long>, startWithWhite: Boolean = false) {
    var currentPosition = Coords2D(0, 0)
    var currentDirection = Directions.UP
    val map = mutableMapOf<Coords2D, Int>()
    val robotProgram = IntCode(inputs, LinkedList(), QueueCollector(true))

    init {
        if (startWithWhite) {
            map[currentPosition] = 1
        }
    }

    fun draw() {
        while (!robotProgram.exit) {
            move()
        }
    }

    fun move() {
        val isPainted = value()
        robotProgram.run(isPainted.toLong())
        var result = robotProgram.output()
        map[currentPosition] = result.toInt()
        robotProgram.run()
        result = robotProgram.output()
        currentDirection = if (result == 1L) currentDirection.turnRight() else currentDirection.turnLeft()
        currentPosition = currentDirection.move(currentPosition)
    }

    fun value(): Int {
        return map.getOrPut(currentPosition, { 0 })
    }

    fun print() {
        val sortedX = map.keys.map { it.x }.sorted()
        val sortedY = map.keys.map { it.y }.sorted()
        for (y in sortedY.first()..sortedY.last()) {
            for (x in sortedX.first()..sortedX.last()) {
                val content = map.getOrDefault(Coords2D(x, y), 0)
                if (content == 1) print('#') else print('.')
            }
            println()
        }
    }
}

class Arcade(val inputs: List<Long>, val joystickMoves: Queue<Long> = LinkedList()) {
    val collector = QueueCollector()
    val arcade = IntCode(inputs, joystickMoves, collector)

    fun buildTiles(): List<Pair<Coords2D, Int>> {
        arcade.run()
        return (collector.output.chunked(3, { Pair(Coords2D(it[0].toInt(), it[1].toInt()), it[2].toInt()) }))
    }

}

class AsciiProgramm(val inputs: List<Long>) {
    val collector = QueueCollector()
    val program = IntCode(inputs, LinkedList(), collector)
    fun buildScaffold(): Scaffold {
        program.run()
        return Scaffold(collector.output)
    }

    fun resolveMaze(solution: List<Long>): QueueCollector {
        val newInputs = listOf(2L) + inputs.drop(1)
        val newCollector = QueueCollector()
        val maze = IntCode(newInputs, LinkedList(solution), newCollector)
        maze.run()
        return newCollector
    }

}

val cursor = listOf(
    '^' to Directions.UP,
    '>' to Directions.RIGHT,
    '<' to Directions.LEFT,
    'v' to Directions.DOWN
).map { it.first.code.toLong() to it.second }.toMap()

class Scaffold(inputs: List<Long>) {
    val path = mutableSetOf<Coords2D>()
    var currentPosition: TileNode = TileNode(Coords2D(0, 0), Directions.UP)

    init {
        var x = 0
        var y = 0
        inputs.forEach {
            if (it == 35L) {
                path.add(Coords2D(x, y))
            } else if (cursor.keys.contains(it)) {
                currentPosition = TileNode(Coords2D(x, y), cursor[it]!!)
                println(currentPosition)
            }

            if (it == 10L) {
                x = 0
                y++
            } else {
                x++
            }
        }
    }

    fun intersections(): List<Coords2D> {
        return path.filter { c -> SQUARED_DIRECTIONS.all { path.contains(it.move(c)) } }
    }

    fun edges(): List<Coords2D> {
        return path.filter { c -> SQUARED_DIRECTIONS.filter { path.contains(it.move(c)) }.size == 1 }
    }

    fun visit(): List<Char> {
        val commands = mutableListOf<Char>()
        val edges = edges()
        val end = edges.first { e -> SQUARED_DIRECTIONS.none { currentPosition.coords == it.move(e) } }
        currentPosition = TileNode(currentPosition.coords, Directions.RIGHT)
        commands.add('R')
        while (currentPosition.coords != end) {
            var forward = 1
            while (path.contains(currentPosition.neighbour().coords)) {
                forward++
                currentPosition = currentPosition.neighbour()
            }
            commands.addAll(forward.toString().toCharArray().toMutableList())
            if (path.contains(currentPosition.leftNeighbour().coords)) {
                commands.add('L')
                currentPosition = currentPosition.leftNeighbour()
            } else if (path.contains(currentPosition.rightNeighbour().coords)) {
                commands.add('R')
                currentPosition = currentPosition.rightNeighbour()
            }
        }
        return commands
    }

    fun align(): Int {
        return intersections().sumOf { it.x * it.y }
    }

    fun printPath(coords: Set<Coords2D>) {
        val maxX = coords.maxOf { it.x }
        val maxY = coords.maxOf { it.y }
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                if (coords.contains(Coords2D(x, y))) print('#') else print('.')
            }
            println()
        }
    }
}

class SpringProgram(val inputs: List<Long>) {
    val collector = QueueCollector()
    fun runDroid(instructions: List<Long>): LinkedList<Long> {
        val program = IntCode(inputs, LinkedList(instructions), collector)
        program.run()
        return collector.output
    }
}

data class Packet(val computerAddress: Int, val x: Long, val y: Long) {

}

class NetworkInterfaceController(val inputs: List<Long>) {
    val computers = mutableMapOf<Int, NetworkComputer>()
    val packetQueue = PaquetsQueue(computers)

    init {
        for (i in 0..49) {
            computers[i] = NetworkComputer(i, inputs, packetQueue)
        }
    }

    fun start() {
        computers.values.map {
            thread() {
                it.start()
            }
        }.forEach { it.join() }
    }

    fun solution(): Pair<Long, Long> {
        return Pair(packetQueue.firstPart, packetQueue.secondPart)
    }
}

class PaquetsQueue(val computers: MutableMap<Int, NetworkComputer>) {
    val packets = mutableMapOf<Int, Queue<Packet>>()
    val currentPackets = mutableMapOf<Int, Packet>()
    var lastPacket : Packet? = null
    var firstPart = -1L
    var secondPart = -1L
    init {
        for(i in 0..49) {
            packets[i] = LinkedList()
        }
    }
    fun receivePacket(packet: Packet) {
        synchronized(packets) {
            if(packet.computerAddress == 255) {
                if(firstPart == -1L) firstPart = packet.y
                if(isIdle()) {
                    val sameY = (packet.y == lastPacket?.y)
                    lastPacket = packet
                    if(sameY) {
                        computers.values.forEach { it.stop() }
                        secondPart = packet.y
                    } else {
                        getPacket(0).add(lastPacket)
                    }
                } else {}
            } else {
                getPacket(packet.computerAddress).add(packet)
            }
        }
    }

    private fun isIdle() = currentPackets.isEmpty() && packets.values.all { it.isEmpty() }

    fun next(computerAddress: Int): Long {

        synchronized(currentPackets) {
            val currentPacket = currentPackets[computerAddress]
            if (currentPacket == null) {
                val queue = getPacket(computerAddress)
                if(queue.isNotEmpty()) {
                    val nextPacket = queue.remove()!!
                    currentPackets[computerAddress] = nextPacket
                    return nextPacket.x
                }
                return -1
            } else {
                currentPackets.remove(computerAddress)
                return currentPacket.y
            }
        }
    }

    fun getPacket(address : Int): Queue<Packet> {
        return packets.getOrElse(address, { error("Invalid Address") })
    }
}

class NetworkComputer(val networkAddress: Int, inputs: List<Long>, packetQueue: PaquetsQueue) {
    val collector = NetworkCollector(networkAddress, packetQueue)
    val program = IntCode(inputs, LinkedList(), collector)
    fun start() {
        program.run(networkAddress.toLong())
    }

    fun stop() {
        program.exit = true
    }
}

class NetworkCollector(val networkAddress: Int, val packetQueue: PaquetsQueue) : QueueCollector() {
    override fun addOutput(value: Long) {
        super.addOutput(value)
        if (output.size == 3) {
            packetQueue.receivePacket(Packet(output.remove().toInt(), output.remove(), output.remove()))
        }
    }

    override fun nextOutput(): Long {
        return packetQueue.next(networkAddress)
    }
}

class SmallDroid(val inputs: List<Long>, val commands : List<String>) {
    val inputQueue = LinkedList<Long>()
    val collector = DroidCollector(inputQueue, LinkedList(commands))
    fun runDroid(instructions: List<Long>): LinkedList<Long> {
        inputQueue.addAll(instructions)
        val program = IntCode(inputs, inputQueue, collector)
        program.run()
        return collector.output
    }
}

class DroidCollector(val inputQueue: LinkedList<Long>, val commands : Queue<String>) : QueueCollector() {
    override fun addOutput(value: Long) {
        super.addOutput(value)
        if(output.size>=7 && String(output.takeLast(8).map { it.toInt().toChar() }.toCharArray()) == "Command?") {
            val userInput = commands.remove()!!
            val instructions = (userInput+'\n').toCharArray().toMutableList().map { it.code.toLong() }
            inputQueue.addAll(instructions)
        }
    }
}

fun input(day: Int): List<Long> {
    return readInput(day)[0].split(',').map { it.toLong() }
}
