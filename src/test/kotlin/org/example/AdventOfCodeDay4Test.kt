package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay4Test {
    @Test
    fun `first sample part1`() {
        var count = 0
        for (i in 245318..765747) {
            if(isValid(i)) {
                count++
            }
        }
        assertEquals(1079, count)
    }

    @Test
    fun `first sample part2`() {
        var count = 0
        for (i in 245318..765747) {
            if(isValidPart2(i)) {
                count++
            }
        }
        assertEquals(699, count)
    }

    private fun isValid(password: Int): Boolean {
        val stringPassword = password.toString()
        val set = stringPassword.toSet()
        val list = stringPassword.toList()
        if(list != list.sorted())
            return false
        if(list.size == set.size)
            return false
        return true
    }

    private fun isValidPart2(password: Int): Boolean {
        val stringPassword = password.toString()
        val list = stringPassword.toList()
        val valid = stringPassword.asSequence().groupBy { it }.values.filter { it.size == 2 }.any()
        if(list != list.sorted())
            return false
        if(!valid)
            return false
        return true
    }


}
