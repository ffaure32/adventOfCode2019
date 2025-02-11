import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class FullIntCodeTest {
    @Test
    fun day2Sample() {
        val intcode = IntCode(mutableListOf(1,12,2,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,10,19,2,9,19,23,2,23,10,27,1,6,27,31,1,31,6,35,2,35,10,39,1,39,5,43,2,6,43,47,2,47,10,51,1,51,6,55,1,55,6,59,1,9,59,63,1,63,9,67,1,67,6,71,2,71,13,75,1,75,5,79,1,79,9,83,2,6,83,87,1,87,5,91,2,6,91,95,1,95,9,99,2,6,99,103,1,5,103,107,1,6,107,111,1,111,10,115,2,115,13,119,1,119,6,123,1,123,2,127,1,127,5,0,99,2,14,0,0
        ))
        intcode.run()
        assertEquals(3716250L, intcode.memory[0])
    }

    @Test
    fun day2SamplePart2() {
        val input = listOf(
            1, 12, 2, 3, 1, 1, 2, 3, 1, 3, 4, 3, 1, 5, 0, 3, 2, 1, 10, 19, 2, 9, 19, 23, 2, 23, 10, 27, 1, 6, 27, 31, 1, 31, 6, 35, 2, 35, 10, 39, 1, 39, 5, 43, 2, 6, 43, 47, 2, 47, 10, 51, 1, 51, 6, 55, 1, 55, 6, 59, 1, 9, 59, 63, 1, 63, 9, 67, 1, 67, 6, 71, 2, 71, 13, 75, 1, 75, 5, 79, 1, 79, 9, 83, 2, 6, 83, 87, 1, 87, 5, 91, 2, 6, 91, 95, 1, 95, 9, 99, 2, 6, 99, 103, 1, 5, 103, 107, 1, 6, 107, 111, 1, 111, 10, 115, 2, 115, 13, 119, 1, 119, 6, 123, 1, 123, 2, 127, 1, 127, 5, 0, 99, 2, 14, 0, 0
        )
        val range = IntRange(0, 99)
        for(noun in range) {
            for(verb in range) {
                val mutable = input.toMutableList()
                mutable[1] = noun
                mutable[2] = verb
                val intcode = IntCode(mutable.map { it.toLong() }.toMutableList())
                intcode.run()
                if(intcode.memory[0] == 19690720L) {
                    assertEquals(6472, 100 * noun + verb)
                }
            }
        }
    }

    @Test
    fun day5Sample1() {
        val intcode = IntCode(mutableListOf(3,0,4,0,99), 123)
        intcode.run()
        assertEquals(99L, intcode.memory[4])
    }
    @Test
    fun day5Sample2() {
        val intcode = IntCode(mutableListOf(1101,100,-1,4,0))
        intcode.run()
        assertEquals(99L, intcode.memory[4])
    }
    @Test
    fun day5Sample3() {
        val intcode = IntCode(mutableListOf(1002,4,3,4,33))
        intcode.run()
        assertEquals(99L, intcode.memory[4])
    }
    @Test
    fun day5Sample1Part2() {
        val intcode = IntCode(mutableListOf(3,9,8,9,10,9,4,9,99,-1,8), 12)
        intcode.run()
        assertEquals(0L, intcode.output())
    }
    @Test
    fun day5Sample2Part2() {
        val intcode = IntCode(mutableListOf(3,9,8,9,10,9,4,9,99,-1,8), 8)
        intcode.run()
        assertEquals(1L, intcode.output())
    }
    @Test
    fun day5Sample3Part2() {
        val intcode = IntCode(mutableListOf(3,9,7,9,10,9,4,9,99,-1,8), 5)
        intcode.run()
        assertEquals(1L, intcode.output())
    }
    @Test
    fun day5Sample4Part2() {
        val intcode = IntCode(mutableListOf(3,9,7,9,10,9,4,9,99,-1,8), 8)
        intcode.run()
        assertEquals(0L, intcode.output())
    }
    @Test
    fun day5Sample5Part2() {
        val intcode = IntCode(mutableListOf(3,3,1108,-1,8,3,4,3,99), 8)
        intcode.run()
        assertEquals(1L, intcode.output())
    }
    @Test
    fun day5Sample6Part2() {
        val intcode = IntCode(mutableListOf(3,3,1108,-1,8,3,4,3,99), 4)
        intcode.run()
        assertEquals(0L, intcode.output())
    }
    @Test
    fun day5Sample7Part2() {
        val intcode = IntCode(mutableListOf(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9), 4)
        intcode.run()
        assertEquals(1L, intcode.output())
    }
    @Test
    fun day5Sample8Part2() {
        val intcode = IntCode(mutableListOf(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9), 0)
        intcode.run()
        assertEquals(0L, intcode.output())
    }

    @Test
    fun day5Part1() {
        val intcode = IntCode(input(5), LinkedList(listOf(1L)))
        intcode.run()
        assertEquals(13933662L, intcode.output())
    }
    @Test
    fun day5Part2() {
        val intcode = IntCode(input(5), 5)
        intcode.run()
        assertEquals(2369720L, intcode.output())
    }

    @Test
    fun day7Sample1() {
        val thruster = Thruster(listOf(3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0))
        assertEquals(43210, thruster.maxPhaseSetting())
    }
    @Test
    fun day7Sample2() {
        val thruster = Thruster(listOf(3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0))
        assertEquals(54321, thruster.maxPhaseSetting())
    }

    @Test
    fun day7Part1() {
        val thruster = Thruster(input(7))
        assertEquals(43812, thruster.maxPhaseSetting())
    }

    @Test
    fun day7Part2() {
        val thruster = Thruster(input(7))
        assertEquals(59597414, thruster.maxPhaseSettingWithLoop())
    }

    @Test
    fun day7SamplePart2() {
        val thruster = Thruster(listOf(3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5))
        assertEquals(139629729, thruster.maxPhaseSettingWithLoop())
    }
    @Test
    fun day7Sample2Part2() {
        val thruster = Thruster(listOf(3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10))
        assertEquals(18216, thruster.maxPhaseSettingWithLoop())
    }

    @Test
    fun day9Relative() {
        val intcode = IntCode(mutableListOf(109,19,204,-34, 99))
        intcode.relativeBase=2000
        intcode.memory[1985]=123L
        intcode.run()
        assertEquals(123L, intcode.output())
    }


    @Test
    fun day9Sample1() {
        val intcode = IntCode(mutableListOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99))
        intcode.run()
        assertEquals(99L, intcode.output())
    }

    @Test
    fun day9Sample() {
        val intcode = IntCode(mutableListOf(104,1125899906842624,99))
        intcode.run()
        assertEquals(1125899906842624, intcode.output())
    }

    @Test
    fun day9Part1() {
        val intcode = IntCode(input(9))
        intcode.run(1)
        assertEquals(3100786347, intcode.output())
    }

    @Test
    fun day9Part2() {
        val intcode = IntCode(input(9))
        intcode.run(2)
        assertEquals(87023, intcode.output())
    }

    @Test
    fun day11Part1() {
        val spaceJail = SpaceJail(input(11))
        spaceJail.draw()
        assertEquals(1985, spaceJail.map.size)
    }

    @Test
    fun day11Part2() {
        val spaceJail = SpaceJail(input(11),true)
        spaceJail.draw()
        spaceJail.print()
    }

    @Test
    fun day13Part1() {
        val arcade = Arcade(input(13))

        val tiles = arcade.buildTiles()

        assertEquals(363, tiles.map { it.second }.count { it == 2 })
    }

    @Test
    fun day13Part2() {
        val inputs = input(13)
        val freeInputs = listOf(2L)+inputs.drop(1)
        println(freeInputs)
        val joystickMoves = LinkedList(readFile("toto.txt")[0].split(',').map { it.toLong() })
        val arcade = Arcade(freeInputs, joystickMoves)

        val tiles = arcade.buildTiles()

        assertEquals(17159, tiles.filter { it.first == Coords2D(-1, 0) }.map { it.second }.last())
    }

    @Test
    fun day17Part1() {
        val ascii = AsciiProgramm(input(17))

        val program = ascii.buildScaffold()

        assertEquals(3608, program.align())
        println(program.visit())
    }

    @Test
    fun day17Part2() {
        val ascii = AsciiProgramm(input(17))

        val solution: List<Long> = listOf(
            65,44,66,44,65,44,67,44,65,44,66,44,67,44,65,44,66,44,67,10,82,44,56,44,82,44,49,48,44,82,44,49,48,10,82,44,52,44,82,44,56,44,82,44,49,48,44,82,44,49,50,10,82,44,49,50,44,82,44,52,44,76,44,49,50,44,76,44,49,50,10,110,10
        )
        // solution.map { print(it.toInt().toChar()) }
        val program = ascii.resolveMaze(solution)

        assertEquals(897426, program.output.last)
    }

    @Test
    fun day21Part1() {
        val spring = SpringProgram(input(21))
        val commands = listOf(
        "NOT A J",
        "NOT B T",
        "AND T J",
        "NOT A T",
        "OR T J",
        "NOT B T",
        "OR T J",
        "NOT C T",
        "OR T J",
        "AND D J",
        "WALK"
        )

        val instructions = commands.map { (it+'\n').toCharArray().toMutableList().map { it.code.toLong() } }.flatten()
        val program = spring.runDroid(instructions)

        //program.forEach { print(it.toInt().toChar()) }
        assertEquals(19349939, program.last)
    }

    @Test
    fun day21Part2() {
        val spring = SpringProgram(input(21))
        val commands = listOf(
            "NOT A J",
            "NOT B T",
            "AND T J",
            "NOT A T",
            "OR T J",
            "NOT B T",
            "OR T J",
            "NOT C T",
            "OR T J",
            "AND D J",
            "AND E T",
            "OR H T",
            "AND T J",
            "RUN"
        )

        val instructions = commands.map { (it+'\n').toCharArray().toMutableList().map { it.code.toLong() } }.flatten()
        val program = spring.runDroid(instructions)

        program.forEach { print(it.toInt().toChar()) }
        assertEquals(1142412777, program.last)
    }

    @Test
    fun day23Parts() {
        val networkInterfaceController = NetworkInterfaceController(input(23))
        networkInterfaceController.start()
        val (part1, part2) = networkInterfaceController.solution()
        assertEquals(19724, part1)
        assertEquals(15252, part2)
    }

    @Test
    fun day25Part1() {
        val spring = SmallDroid(input(25), buildCommands())
        val commands = listOf(
            "inventory"
        )

        //val instructions = commands.map { (it+'\n').toCharArray().toMutableList().map { it.code.toLong() } }.flatten()
        val program = spring.runDroid(listOf())

        val map = String(program.map { it.toInt().toChar() }.toCharArray())
        val passwordSentence = "\"Oh, hello! You should be able to get in by typing "
        val endString = map.drop(map.indexOf(passwordSentence)+passwordSentence.length)

        assertEquals(2214608912, endString.take(endString.indexOf(" ")).toLong())
    }

    fun buildCommands(): MutableList<String> {
        val commands = mutableListOf<String>()
        commands.add("north")
        commands.add("take tambourine")
        commands.add("east")
        commands.add("take astrolabe")
        commands.add("east")
        commands.add("north")
        commands.add("take klein bottle")
        commands.add("north")
        commands.add("take easter egg")
        commands.add("south")
        commands.add("south")
        commands.add("west")
        commands.add("south")
        commands.add("take shell")
        commands.add("north")
        commands.add("west")
        commands.add("south")
        commands.add("south")
        commands.add("south")
        commands.add("take hypercube")
        commands.add("north")
        commands.add("north")
        commands.add("west")
        commands.add("take dark matter")
        commands.add("west")
        commands.add("north")
        commands.add("west")
        commands.add("take coin")
        commands.add("south")
        val assets =
            setOf("coin", "dark matter", "hypercube", "tambourine", "shell", "astrolabe", "klein bottle", "easter egg")

        for(i in 1..8) {
            assets.combinations(i).toSet().forEach {
                assets.forEach { commands.add("drop $it") }
                it.forEach { commands.add("take $it") }
                commands.add("south")
            }
        }

        return commands
    }
}