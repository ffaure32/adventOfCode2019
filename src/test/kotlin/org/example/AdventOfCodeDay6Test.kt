package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay6Test {
    companion object {
        val ROOT : String = "COM"
    }
    private val rootObject = SpaceObject(ROOT, ROOT)

    @Test
    fun samples() {
        val input = "B2D)6YV"
        val child = parseSpaceObjects(input)
        assertEquals("B2D", child.parent)
        assertEquals("6YV", child.name)
    }

    @Test
    fun loadObjects() {
        val parseFile = parseFile()
        assertEquals(1606, parseFile.size)
    }

    @Test
    fun countAllSample() {
        val parseFile = parseSampleFile()
        val sum = parseFile.map { it.getParentCount(parseFile) }.sum()
        assertEquals(42, sum)
    }

    @Test
    fun countAll() {
        val parseFile = parseFile()
        val sum = parseFile.map { it.getParentCount(parseFile) }.sum()
        assertEquals(254447, sum)
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
    fun countParents() {
        val parseFile = parseFile()
        assertEquals(1606, parseFile.size)
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

    private fun parseFile() : Set<SpaceObject> {
        val inputFile = "/day6.txt".loadFromFile()
        val input = inputFile.split("\n")
        return input.map { parseSpaceObjects(it.trim()) }.toSet().plus(rootObject)
    }

    private fun parseSampleFile() : Set<SpaceObject> {
        val inputFile = "/day6Sample.txt".loadFromFile()
        val input = inputFile.split("\n")
        return input.map { parseSpaceObjects(it.trim()) }.toSet().plus(rootObject)
    }


}
