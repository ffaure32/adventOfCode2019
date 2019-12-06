package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay6Test {
    @Test
    fun samples() {
        val input = "B2D)6YV"
        val child = parseSpaceObjects(input)
        assertEquals("B2D", child.parent)
        assertEquals("6YV", child.name)
    }
    fun parseSpaceObjects(input : String) : SpaceObject {
        val objects = input.split(")")
        return SpaceObject(objects[1],objects[0])
    }
data class SpaceObject(val name : String, val parent : String)


}
