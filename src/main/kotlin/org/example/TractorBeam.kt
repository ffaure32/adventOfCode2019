package org.example

import java.util.*

fun main(args: Array<String>) {
    val input = "/day19.txt".loadFromFile()
    val tractorSquare = TractorSquare(input, 100)
    tractorSquare.computeTotalCount(50)
    println(tractorSquare.count)
    for(y in 0 until 1200L) {
        val result = tractorSquare.xLoop(y)
        if(result>0) {
            println(result)
            break
        }
    }
}

class TractorSquare(val input : String, val size : Int) {
    val square = mutableSetOf<LongPosition>()
    var minX = 0L
    var maxX = 100L
    var count = 0L

    fun computeTotalCount(side : Long) {
        for(y in 0 until side) {
            tractorCount(y, side)
        }

    }
    fun tractorCount(y : Long, side : Long) {
        for(x in 0L until side) {
            count += computeTractorResult(x, y)
        }
    }

    fun xLoop(y : Long): Long {
        var previousValue = 0L
        for(x in minX until maxX) {
            val playTractorResult = computeTractorResult(x, y)
            if(previousValue == 0L && playTractorResult == 1L) {
                minX = x
            }
            if(previousValue == 1L && playTractorResult == 0L) {
                maxX = x+5
            }
            previousValue = playTractorResult
            if(playTractorResult == 1L) {
                square.add(LongPosition(x, y))
                if(findMaxSquare(square, x, y)) {
                    return getResult(x, y)
                }
            }
        }
        return -1L
    }

    private fun computeTractorResult(x: Long, y: Long): Long {
        val longQueue = initLongQueue(x, y)
        val playTractorResult = (playTractor(input, longQueue) as TractorBeamScreen).result
        return playTractorResult
    }

    private fun getResult(x: Long, y: Long) = ((adjustCoord(x)) * 10000 + adjustCoord(y))

    fun findMaxSquare(square: Set<LongPosition>, x: Long, y: Long) : Boolean {
        return square.contains(LongPosition(adjustCoord(x), y))
                && square.contains(LongPosition(adjustCoord(x), adjustCoord(y)))
                && square.contains(LongPosition(x, adjustCoord(y)))
    }

    fun adjustCoord(coord : Long) = coord -size +1
}




public fun playTractor(input: String, queue: Queue<Long>): IntCodeInteraction {
    val inputSplit = stringToLongList(input)

    val tractorBeamScreen = TractorBeamScreen()
    val intCodeComputer = BigIntCodeComputer(inputSplit, queue, tractorBeamScreen)
   var exit: Boolean
   do {
        exit = intCodeComputer.applyInstructionAtPosition()
    } while (!exit)
    return tractorBeamScreen
}
