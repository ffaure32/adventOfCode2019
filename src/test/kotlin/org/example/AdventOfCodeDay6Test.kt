package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay6Test {

    @Test
    fun testParsing() {
        val input = "B2D)6YV"
        val child = parseSpaceObjects(input)
        assertEquals("B2D", child.parent)
        assertEquals("6YV", child.name)
    }

    @Test
    fun countAllSample() {
        val parseFile = parseSampleFile()
        val system = SpaceSystem(parseFile)
        assertEquals(42, system.countAllParents())
    }

    @Test
    fun countAll() {
        val parseFile = parseFile()
        val system = SpaceSystem(parseFile)
        assertEquals(254447, system.countAllParents())
    }

    @Test
    fun findByName() {
        val parseFile = parseFile()
        val system = SpaceSystem(parseFile)

        val spaceObject = system.findObjectsByName("YOU")
        assertEquals("YOU", spaceObject.name)
    }

    @Test
    fun findCommonParents() {
        val parseFile = parseSampleFile()
        val system = SpaceSystem(parseFile)

        assertEquals("D", system.findClosestParent("L", "I").name)
        assertEquals("B", system.findClosestParent("L", "H").name)
    }

    @Test
    fun testTransfersReal() {
        val parseFile = parseFile()
        val system = SpaceSystem(parseFile)

        val transfers =
            system.calculateTransferCount( "YOU", "SAN")
        assertEquals(445, transfers)
    }

    @Test
    fun testTransfers() {
        val parseFile = parseSampleFile()
        val system = SpaceSystem(parseFile)

        val transfers =
            system.calculateTransferCount( "H", "L")
        assertEquals(6, transfers)
    }

    @Test
    fun testFindParent() {
        val parseFile = parseFile()
        val system = SpaceSystem(parseFile)
        val first = parseFile.first()
        val parent = system.findParent(first)

        assertEquals(first.parent, parent.name)
    }


    private fun parseSpaceObjects(input: String): SpaceObject {
        val objects = input.split(")")
        return SpaceObject(objects[1], objects[0])
    }

    private fun parseFile() : MutableSet<SpaceObject> {
        val inputFile = "/day6.txt".loadFromFile()
        val input = inputFile.split("\n")
        return input.map { parseSpaceObjects(it.trim()) }.toMutableSet()
    }

    private fun parseSampleFile() : MutableSet<SpaceObject> {
        val inputFile = "/day6Sample.txt".loadFromFile()
        val input = inputFile.split("\n")
        return input.map { parseSpaceObjects(it.trim()) }.toMutableSet()
    }


}
