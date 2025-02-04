package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay14Test {

    @Test
    fun readSimpleReaction() {
        val reaction = buildReaction("10 ORE => 10 A")
        assertEquals(10, reaction.outputChemical.quantity)
        assertEquals(1, reaction.inputChemicals.size)
    }

    @Test
    fun readDoubleReaction() {
        val reaction = buildReaction("7 A, 1 E => 1 FUEL")
        assertEquals(1, reaction.outputChemical.quantity)
        assertEquals(2, reaction.inputChemicals.size)
    }

    private fun buildReaction(reactionLitteral: String): Reaction {
        val split = reactionLitteral.split(" => ")
        val input = split[0].trim()
        val output = split[1].trim()
        val outputChemical = output.split(" ")
        val inputChemicals = input.split(", ").map {
            val inputChemical = it.split(" ")
            Chemical(inputChemical[0].toInt(), inputChemical[1])
        }.toSet()
        return Reaction(Chemical(outputChemical[0].toInt(), outputChemical[1]), inputChemicals)
    }


    @Test
    fun testLoadReactions() {
        val input = """
            9 ORE => 2 A
            8 ORE => 3 B
            7 ORE => 5 C
            3 A, 4 B => 1 AB
            5 B, 7 C => 1 BC
            4 C, 1 A => 1 CA
            2 AB, 3 BC, 4 CA => 1 FUEL
        """.trimIndent()
        val inputSplitted = input.split("\n")
        val reactions = Reactions(inputSplitted.map { buildReaction(it) }.toSet())
        reactions.computeNeeds()
        val primitiveQuantities = reactions.needs.getOrDefault("ORE",0)
        assertEquals(165, primitiveQuantities)

    }
    @Test
    fun needsForFirstSample() {
        val input = """
                10 ORE => 10 A
                1 ORE => 1 B
                7 A, 1 B => 1 C
                7 A, 1 C => 1 D
                7 A, 1 D => 1 E
                7 A, 1 E => 1 FUEL
        """.trimIndent()
        val inputSplitted = input.split("\n")
        val reactions = Reactions(inputSplitted.map { buildReaction(it) }.toSet())
        reactions.computeNeeds()
        val primitiveQuantities = reactions.needs.getOrDefault("ORE",0)
        assertEquals(31, primitiveQuantities)

    }

    @Test
    fun needsforThirdSample() {
        val input = """
            157 ORE => 5 NZVS
            165 ORE => 6 DCFZ
            44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL
            12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ
            179 ORE => 7 PSHF
            177 ORE => 5 HKGWZ
            7 DCFZ, 7 PSHF => 2 XJWVT
            165 ORE => 2 GPVTF
            3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT
        """.trimIndent()
        val inputSplitted = input.split("\n")
        val reactions = Reactions(inputSplitted.map { buildReaction(it) }.toSet())
        reactions.computeNeeds()
        val primitiveQuantities = reactions.needs.getOrDefault("ORE",0)
        assertEquals(13312, primitiveQuantities)
    }

    @Test
    fun needsforFourthSample() {
        val input = """
                2 VPVL, 7 FWMGM, 2 CXFTF, 11 MNCFX => 1 STKFG
                17 NVRVD, 3 JNWZP => 8 VPVL
                53 STKFG, 6 MNCFX, 46 VJHF, 81 HVMC, 68 CXFTF, 25 GNMV => 1 FUEL
                22 VJHF, 37 MNCFX => 5 FWMGM
                139 ORE => 4 NVRVD
                144 ORE => 7 JNWZP
                5 MNCFX, 7 RFSQX, 2 FWMGM, 2 VPVL, 19 CXFTF => 3 HVMC
                5 VJHF, 7 MNCFX, 9 VPVL, 37 CXFTF => 6 GNMV
                145 ORE => 6 MNCFX
                1 NVRVD => 8 CXFTF
                1 VJHF, 6 MNCFX => 4 RFSQX
                176 ORE => 6 VJHF
        """.trimIndent()
        val inputSplitted = input.split("\n")
        val reactions = Reactions(inputSplitted.map { buildReaction(it) }.toSet())
        reactions.computeNeeds()
        val primitiveQuantities = reactions.needs.getOrDefault("ORE",0)
        assertEquals(180697, primitiveQuantities)
    }

    @Test
    fun needsforFifthSample() {
        val input = """
            171 ORE => 8 CNZTR
            7 ZLQW, 3 BMBT, 9 XCVML, 26 XMNCP, 1 WPTQ, 2 MZWV, 1 RJRHP => 4 PLWSL
            114 ORE => 4 BHXH
            14 VRPVC => 6 BMBT
            6 BHXH, 18 KTJDG, 12 WPTQ, 7 PLWSL, 31 FHTLT, 37 ZDVW => 1 FUEL
            6 WPTQ, 2 BMBT, 8 ZLQW, 18 KTJDG, 1 XMNCP, 6 MZWV, 1 RJRHP => 6 FHTLT
            15 XDBXC, 2 LTCX, 1 VRPVC => 6 ZLQW
            13 WPTQ, 10 LTCX, 3 RJRHP, 14 XMNCP, 2 MZWV, 1 ZLQW => 1 ZDVW
            5 BMBT => 4 WPTQ
            189 ORE => 9 KTJDG
            1 MZWV, 17 XDBXC, 3 XCVML => 2 XMNCP
            12 VRPVC, 27 CNZTR => 2 XDBXC
            15 KTJDG, 12 BHXH => 5 XCVML
            3 BHXH, 2 VRPVC => 7 MZWV
            121 ORE => 7 VRPVC
            7 XCVML => 6 RJRHP
            5 BHXH, 4 VRPVC => 5 LTCX
        """.trimIndent()
        val inputSplitted = input.split("\n")
        val reactions = Reactions(inputSplitted.map { buildReaction(it) }.toSet())
        reactions.computeNeeds()
        val primitiveQuantities = reactions.needs.getOrDefault("ORE",0)
        assertEquals(2210736, primitiveQuantities)
    }


    @Test
    fun testLoadRealReactions() {
        val lines = "/day14.txt".loadFromFile().lines()
        val reactions = Reactions(lines.map { buildReaction(it) }.toSet())
        reactions.computeNeeds()
        val primitiveQuantities = reactions.needs.getOrDefault("ORE",0)
        assertEquals(362713, primitiveQuantities)
        println(reactions.wastes)
        println(reactions.reactionSet.filter { it.inputChemicals.map{ it.chemicalLitteral}.contains("ORE") }.map{ it.outputChemical})
    }

    @Test
    fun testConsumeATrillionORE() {
        val lines = "/day14.txt".loadFromFile().lines()
        val reactions = Reactions(lines.map { buildReaction(it) }.toSet())
        val result = reactions.computeTrillionORE()
        println(result -1)
    }

    @Test
    fun testTrillionSample() {
        val input = """
            157 ORE => 5 NZVS
            165 ORE => 6 DCFZ
            44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL
            12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ
            179 ORE => 7 PSHF
            177 ORE => 5 HKGWZ
            7 DCFZ, 7 PSHF => 2 XJWVT
            165 ORE => 2 GPVTF
            3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT
        """.trimIndent()
        val inputSplitted = input.split("\n")
        val reactions = Reactions(inputSplitted.map { buildReaction(it) }.toSet())
        val result = reactions.computeTrillionORE()
        println(result -1)
    }
    @Test
    fun testTrillionSample1() {
        val input = """
9 ORE => 2 A
8 ORE => 3 B
7 ORE => 5 C
3 A, 4 B => 1 AB
5 B, 7 C => 1 BC
4 C, 1 A => 1 CA
2 AB, 3 BC, 4 CA => 1 FUEL
        """.trimIndent()
        val inputSplitted = input.split("\n")
        val reactions = Reactions(inputSplitted.map { buildReaction(it) }.toSet())
        val result = reactions.computeTrillionORE()
        println(result -1)
    }

}
