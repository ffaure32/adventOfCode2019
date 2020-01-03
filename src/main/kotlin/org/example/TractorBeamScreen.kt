package org.example

import java.util.*

class TractorBeamScreen() : IntCodeInteraction {
    var result : Long = 0L
    override fun specificOutput(resultComputed: Long) {
        result = resultComputed
    }

    override fun printScreen() {
    }

    override fun resultPart1(): Any {
        return ""
    }

    override fun resultPart2(): Any {
        return ""
    }

    override fun specificStore(input: Queue<Long>) {
    }

}