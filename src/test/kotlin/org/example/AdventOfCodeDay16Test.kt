package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.math.abs
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay16Test {

    @Test
    fun firstSample() {
        val inputSignal = "12345678"
        assertEquals("48226158", applyPhases(1, inputSignal))
        assertEquals("34040438", applyPhases(2, inputSignal))
        assertEquals("03415518", applyPhases(3, inputSignal))
        assertEquals("01029498", applyPhases(4, inputSignal))

    }

    @Test
    fun complexSamples() {
        val inputSignal = "80871224585914546619083218645595"
        assertEquals("24176176", applyPhases(100, inputSignal).substring(0, 8))
    }

    @Test
    fun complexSample2() {
        val inputSignal = "19617804207202209144916044189917"
        assertEquals("73745418", applyPhases(100, inputSignal).substring(0, 8))
    }

    @Test
    fun realPart1() {
        val inputSignal = """
            59762677844514231707968927042008409969419694517232887554478298452757853493727797530143429199414189647594938168529426160403829916419900898120019486915163598950118118387983556244981478390010010743564233546700525501778401179747909200321695327752871489034092824259484940223162317182126390835917821347817200000199661513570119976546997597685636902767647085231978254788567397815205001371476581059051537484714721063777591837624734411735276719972310946108792993253386343825492200871313132544437143522345753202438698221420628103468831032567529341170616724533679890049900700498413379538865945324121019550366835772552195421407346881595591791012185841146868209045
        """.trimIndent()
        assertEquals("94935919", applyPhases(100, inputSignal).substring(0, 8))

    }

    @Test
    fun testPar2() {
        val inputSignal = "03036732577212944063491565474664".trimIndent().repeat(10000)
        assertEquals("94935919", applyPhases(100, inputSignal).substring(0, 8))

    }

    private fun applyPhases(numberOfApplies : Int, originalInput : String) : String {
        var input = originalInput
        for(i in 0 until numberOfApplies) {
            input = applyPhase(input)
        }
        return input
    }
    private fun applyPhase(
        inputSignal: String
    ): String {
        val basePattern = listOf(0, 1, 0, -1)
        inputSignal.forEach { it.toString().toInt() }
        var result = ""
        for (index in 1..inputSignal.length) {
            val patternToApply = generatePattern(index, basePattern, inputSignal.length)
            var sum = 0
            for (i in 1..inputSignal.length) {
                sum += inputSignal[i - 1].toString().toInt() * patternToApply[i % patternToApply.size]
            }
            val digit = abs(sum) % 10
            result += digit
        }
        return result
    }

    fun         generatePattern(index : Int, basePattern : List<Int>, maxSize : Int) : List<Int> {
        val result = mutableListOf<Int>()
        basePattern.forEach {
            for(i in 1..index) {
                if(result.size>maxSize) break
                result.add(it)
            }
        }
        return result
    }
}
