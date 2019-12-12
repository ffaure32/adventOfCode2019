package org.example

import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.math.abs
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdventOfCodeDay12Test {
    class MoonPosition(
        var x: Long
        , var y: Long
        , var z: Long
    ) {
        fun energy(): Long {
            return abs(x) + abs(y) + abs(z)
        }

        fun isZeroX(): Boolean {
            return isZero(x)
        }

        fun isZeroY(): Boolean {
            return isZero(y)
        }

        fun isZeroZ(): Boolean {
            return isZero(z)
        }

        private fun isZero(value: Long): Boolean {
            return value == 0L
        }
    }

    class Moon(
        x: Long
        , y: Long
        , z: Long
    ) {
        var position = MoonPosition(x, y, z)
        var velocity = MoonPosition(0, 0, 0)

        fun totalEnergy(): Long {
            return position.energy() * velocity.energy()
        }

        fun move(otherMoons: Set<MoonPosition>) {
            applygravity(otherMoons)
            applyVelocity()
        }

        fun isZeroX(): Boolean {
            return this.position.isZeroX() && this.velocity.isZeroX()
        }

        fun isZeroY(): Boolean {
            return this.position.isZeroY() && this.velocity.isZeroY()
        }

        fun isZeroZ(): Boolean {
            return this.position.isZeroZ() && this.velocity.isZeroZ()
        }

        private fun applyVelocity() {
            this.position.x += this.velocity.x
            this.position.y += this.velocity.y
            this.position.z += this.velocity.z
        }

        private fun applygravity(otherMoons: Set<MoonPosition>) {
            otherMoons.forEach {
                if (this.position.x < it.x) this.velocity.x++
                if (this.position.x > it.x) this.velocity.x--
                if (this.position.y < it.y) this.velocity.y++
                if (this.position.y > it.y) this.velocity.y--
                if (this.position.z < it.z) this.velocity.z++
                if (this.position.z > it.z) this.velocity.z--
            }
        }
    }

    @Test
    fun firstSample() {
        val moon1 = Moon(-1, 0, 2)
        val moon2 = Moon(2, -10, -7)
        val moon3 = Moon(4, -8, 8)
        val moon4 = Moon(3, 5, z = -1)
        val max = 10
        val expected: Long = 179
        val moons = setOf<Moon>(moon1, moon2, moon3, moon4)

        val totalEnergy = computeEnergy(max, moons)
        assertEquals(expected, totalEnergy)
    }

    @Test
    fun realExample() {
        val moon1 = Moon(13, 9, 5)
        val moon2 = Moon(8, 14, -2)
        val moon3 = Moon(-5, 4, 11)
        val moon4 = Moon(2, -6, z = 1)
        val max = 1000
        val expected: Long = 6490
        val moons = setOf<Moon>(moon1, moon2, moon3, moon4)
        val totalEnergy = computeEnergy(max, moons)
        assertEquals(expected, totalEnergy)
    }

    private fun computeEnergy(
        max: Int,
        moons : Set<Moon>): Long {
        for (i in 1..max) {
            moveStep(moons)
        }
        return moons.map{ it.totalEnergy()}.sum()
    }

    @Test
    fun realExamplePart2() {
        val moon1 = Moon(13, 9, 5)
        val moon2 = Moon(8, 14, -2)
        val moon3 = Moon(-5, 4, 11)
        val moon4 = Moon(2, -6, z = 1)
        val expected: Long = 277068010964808
        val moons = setOf<Moon>(moon1, moon2, moon3, moon4)
        val totalEnergy = calculateFrequency(moons)
        assertEquals(expected, totalEnergy)
    }

    @Test
    fun firstSamplePart2() {
        val moon1 = Moon(-1, 0, 2)
        val moon2 = Moon(2, -10, -7)
        val moon3 = Moon(4, -8, 8)
        val moon4 = Moon(3, 5, z = -1)
        val moons = setOf<Moon>(moon1, moon2, moon3, moon4)
        val expected: Long = 2772
        val totalEnergy = calculateFrequency(moons)
        assertEquals(expected, totalEnergy)
    }

    @Test
    fun testPPCM() {
        assertEquals(17, gcd(221, 782))
        assertEquals(10166L, ppcm(setOf(221L, 782L)))
    }
    private fun calculateFrequency(moons: Set<Moon>): Long {
        var notFound = true
        var count = 0L
        val founds = mutableMapOf<String, Long>()
        val initialPositions = moons.map { it to MoonPosition(it.position.x, it.position.y, it.position.z) }.toMap()
        while (notFound) {
            moveStep(moons)
            count++
            if (moons.all { it.position.x == initialPositions.get(it)!!.x }
                && moons.all { it.velocity.isZeroX() }) {
                founds.putIfAbsent("posx", count)
            }
            if (moons.all { it.position.y == initialPositions.get(it)!!.y }
                && moons.all { it.velocity.isZeroY() }) {
                founds.putIfAbsent("posy", count)
            }
            if (moons.all { it.position.z == initialPositions.get(it)!!.z }
                && moons.all { it.velocity.isZeroZ() }) {
                founds.putIfAbsent("posz", count)
            }
/*
            if (moons.all { it.velocity.isZeroX() }) {
                founds.putIfAbsent("velx", count)
            }
            if (moons.all { it.velocity.isZeroY() }) {
                founds.putIfAbsent("vely", count)
            }
            if (moons.all { it.velocity.isZeroZ() }) {
                founds.putIfAbsent("velz", count)
            }
*/
            notFound = founds.size < 3

        }
        val array = founds.map { it.value }.toSet()
        return ppcm(array)
    }

    private fun moveStep(moons: Set<Moon>) {
        var moonPositions = moons.map {
            it to moons.minus(it).map {
                MoonPosition(
                    it.position.x,
                    it.position.y,
                    it.position.z
                )
            }
        }.toMap()
        moonPositions.forEach { it.key.move(it.value.toSet()) }
    }


}

fun ppcm(numbers : Set<Long>): Long {
    return numbers.fold(1L) {pcm, it -> calcpcm(it, pcm)}
}

fun calcpcm(it: Long, pcm: Long): Long {
    return it*pcm/gcd(it, pcm)
}
