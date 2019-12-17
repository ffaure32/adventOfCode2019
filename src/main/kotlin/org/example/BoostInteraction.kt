package org.example

import java.util.*

class BoostInteraction() :
    IntCodeInteraction {
    val output = mutableListOf<Long>()



    override fun printScreen() {
        print(output)
    }

    override fun resultPart1(): Any {
        return output.map { it.toString()}.joinToString(",")
    }

    override fun specificStore(input: Queue<Long>) {
        // NOTHING TO DO
    }

    override fun specificOutput(result: Long) {
        output.add(result)
    }
}

