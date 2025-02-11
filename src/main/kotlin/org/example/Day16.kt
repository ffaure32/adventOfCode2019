class Day16 {
    private val input = readInput(16)

    fun part1(): Int {
        val fft = parseInput()
        fft.phases(100)
        return fft.firstEightDigits()
    }

    fun part2(): Int {
        val fft = parseInput(10000)
        fft.phases2(100)
        return fft.firstEightDigits()
    }

    private fun parseInput(repeats : Int = 1): FlawedFrequencyTransmission {
        val toParse = input[0].repeat(repeats)
        return FlawedFrequencyTransmission(toParse.toCharArray().map { it.digitToInt() })
    }
}

class FlawedFrequencyTransmission(var signal : List<Int>) {

    fun phases(count : Int) {
        for(i in 1..count) {
            phase()
        }
    }

    fun phases2(count : Int) {
        val offset = signal.take(7).joinToString("").toInt()
        signal = signal.drop(offset)
        for(i in 1..count) {
            phase2()
        }
    }

    private fun phase() {
        signal = signal.mapIndexed { line, _ ->
            val digitsCount = DigitCount()
            signal.drop(line).forEachIndexed { index, i ->
                when (((index / (line+1))+1) % 4) {
                    1 -> digitsCount.add(i)
                    3 -> digitsCount.remove(i)
                }
            }
            digitsCount.total()
        }
    }
    private fun phase2() {
        var sum = 0
        signal = signal.reversed().map {
            sum += it
            sum.toString().last().digitToInt()
        }.reversed()
    }

    fun firstEightDigits() : Int {
        return signal.take(8).joinToString("").toInt()
    }

}

class DigitCount {

    private val counts = IntRange(0, 9).associateWith{ 0L }.toMutableMap()

    fun add(digit : Int) {
        counts[digit] = counts[digit]!!+1
    }

    fun remove(digit : Int) {
        counts[digit] = counts[digit]!!-1
    }

    fun total(): Int {
        return counts.map { it.key * it.value }.sum().toString().last().digitToInt()
    }
}
